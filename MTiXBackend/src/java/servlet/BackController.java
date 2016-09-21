/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import PropertyManagement.ReservePropertyBeanLocal;
import PropertyManagement.SeatingPlanManagementBeanLocal;
import entity.Property;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.BulletinManager;
import manager.LockManager;
import manager.LogManager;
import manager.LoginManager;
import manager.MessageManager;
import manager.ProductManager;
import manager.RegisterManager;
import manager.ReservationManager;
import manager.ResetPasswordManager;
import manager.SeatingPlanManager;
import manager.UnlockManager;
import session.stateless.BulletinSessionLocal;
import session.stateless.LockAccountSessionLocal;
import session.stateless.LoginSessionLocal;
import session.stateless.MessageSessionLocal;
import session.stateless.ProductSessionLocal;
import session.stateless.RegisterSessionLocal;
import session.stateless.ResetPasswordSessionLocal;
import session.stateless.UnlockAccountSessionLocal;

/**
 *
 * @author Student-ID
 */
@WebServlet(name = "BackController", urlPatterns = {"/BackController", "/BackController?*"})
public class BackController extends HttpServlet {

    @EJB
    SeatingPlanManagementBeanLocal seatingPlanManagementBeanLocal;

    @EJB
    ReservePropertyBeanLocal reservePropertyBeanLocal;
    @EJB
    private ProductSessionLocal productSession;

    @EJB
    private BulletinSessionLocal bulletinSession;
    @EJB
    private MessageSessionLocal messageSession;

    @EJB
    private RegisterSessionLocal registerSession;
    @EJB
    private UnlockAccountSessionLocal unlockAccountSession;
    @EJB
    private ResetPasswordSessionLocal resetPasswordSession;
    @EJB
    private LockAccountSessionLocal lockAccountSession;
    @EJB
    private LoginSessionLocal loginSession;

    public String currentUser;
    public String subject = "";
    public String role = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            /* TODO output your page here. You may use following sample code. */
            LoginManager loginManager = new LoginManager(loginSession);
            LockManager lockManager = new LockManager(lockAccountSession);
            ResetPasswordManager resetManager = new ResetPasswordManager(resetPasswordSession);
            LogManager logManager = new LogManager();
            UnlockManager unlockManager = new UnlockManager(unlockAccountSession);
            RegisterManager registerManager = new RegisterManager(registerSession);
            MessageManager messageManager = new MessageManager(messageSession);
            BulletinManager bulletinManager = new BulletinManager(bulletinSession);
            ProductManager productManager = new ProductManager(productSession);
            SeatingPlanManager spm = new SeatingPlanManager(seatingPlanManagementBeanLocal);
            ReservationManager rm = new ReservationManager(reservePropertyBeanLocal);

            int page = 1;
            int recordsPerPage = 8;
            String action;
            action = request.getParameter("action");
            System.out.println("Action = " + action);

            String name = request.getParameter("name");
            System.out.println("here special");
            if (name != null) {
                System.out.println("here special 2");
                if (action == null) {
                    System.out.println("here special 3");
                    action = "verify2";
                }
            }

            if (action.equals("resetPassword")) {
                System.out.println("reset here 2");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            } else if (action.equals("doLogin")) {
                System.out.println(request.getParameter("userName") + "      " + request.getParameter("password"));
                String username = request.getParameter("userName");
                String password = request.getParameter("password");

                if (loginManager.checkVerification(username)) {
                    System.out.println("here 1");
                    request.setAttribute("verification", "true");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                } else {
                    if (loginManager.identify(username, password)) {
                        System.out.println("here 2");
                        if (resetManager.resetted(username)) {
                            System.out.println("reset here 1");
                            request.setAttribute("username", username);
                            request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
                        }
                        if (lockManager.passThrough(username)) {
                            role = loginManager.getRoles(username);
                            System.out.println("role: " + role);
                            if (loginManager.getRoles(username).equals("super administrator") || loginManager.getRoles(username).equals("property manager") || loginManager.getRoles(username).equals("product manager")) {
                                System.out.println("here new 1");
                                logManager.logMessage(username + " logged in.");
                                currentUser = username;
                                request.setAttribute("username", username);
                                request.setAttribute("role", role);
                                request.getRequestDispatcher("/home.jsp").forward(request, response);
                            } else {
                                request.setAttribute("role", "true");
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                        } else if (lockManager.finalLock(username)) {
                            System.out.println("here 3");
                            request.setAttribute("locked", "true");
                            request.setAttribute("account", username);
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        } else {
                            System.out.println("here 4");
                            role = loginManager.getRoles(username);
                            System.out.println("role: " + role);
                            if (loginManager.getRoles(username).equals("super administrator") || loginManager.getRoles(username).equals("property manager") || loginManager.getRoles(username).equals("product manager")) {
                                System.out.println("here new 1");
                                logManager.logMessage(username + " logged in.");
                                currentUser = username;
                                request.setAttribute("username", username);
                                request.setAttribute("role", role);
                                request.getRequestDispatcher("/home.jsp").forward(request, response);
                            } else {
                                request.setAttribute("role", "true");
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                            //logManager.logMessage(username + " logged in.");

                        }
                    } else if (lockManager.checkLock(username, password)) {
                        System.out.println("here 5");
                        if (lockManager.lockAccount(username)) {
                            System.out.println("here 6");
                            if (lockManager.finalLock(username)) {
                                System.out.println("here 7");
                                request.setAttribute("locked", "true");
                                request.setAttribute("account", username);
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            } else {
                                System.out.println("here 8");
                                request.setAttribute("halflock", "true");
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                        } else {
                            System.out.println("here 9");
                            lockManager.insertLock(username);
                            request.setAttribute("halflock", "true");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        }
                    } else {
                        System.out.println("here 10");
                        request.setAttribute("wronguser", "true");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    }
                }
            } else if (action.equals("verify")) {
                System.out.println("userName in verify: " + request.getParameter("userName"));
                String verifyUser = request.getParameter("userName");

                if (registerManager.checkOldPassword(request.getParameter("userName"), request.getParameter("oldPass"))) {
                    if (request.getParameter("newPass").equals(request.getParameter("newPass2"))) {
                        registerManager.verify(request.getParameter("userName"));
                        registerManager.changePassword(request.getParameter("userName"), request.getParameter("newPass"));
                        request.setAttribute("accountverified", "true");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    } else {
                        request.setAttribute("matchpass", "true");
                        request.setAttribute("verifyUser", verifyUser);
                        request.getRequestDispatcher("/verification.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("oldpass", "true");
                    request.setAttribute("verifyUser", verifyUser);
                    request.getRequestDispatcher("/verification.jsp").forward(request, response);
                }
            } else if (action.equals("verify2")) {
                request.getRequestDispatcher("/verification.jsp").forward(request, response);
            } else if (action.equals("unlock")) {
                System.out.println("here special 4");
                request.setAttribute("username", name);
                request.getRequestDispatcher("/unlockAccount.jsp").forward(request, response);
            } else if (action.equals("unlocking")) {
                String username = request.getParameter("userName");
                System.out.println("unlocking " + username);
                unlockManager.unlock(username);
                request.setAttribute("unlock", "true");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else if (action.equals("createAccount")) {
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/createAccount.jsp").forward(request, response);
            } else if (action.equals("creating")) {
                System.out.println("creating: " + request.getParameter("username") + "    " + request.getParameter("role") + "    " + request.getParameter("mobileNumber"));
                if (registerManager.checkConflict(request.getParameter("username"))) {
                    request.setAttribute("username", currentUser);
                    request.setAttribute("conflict", "true");
                    request.getRequestDispatcher("/createAccount.jsp").forward(request, response);
                } else {
                    registerManager.adminCreate(request.getParameter("username"), request.getParameter("role"), request.getParameter("mobileNumber"));
                    request.setAttribute("username", currentUser);
                    request.setAttribute("created", "true");
                    request.getRequestDispatcher("/createAccount.jsp").forward(request, response);
                }
            } else if (action.equals("home")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/home.jsp").forward(request, response);
            } else if (action.equals("sendResetPassword")) {
                System.out.println("reset here 3");
                String username = request.getParameter("userName");
                if (resetManager.checkUserAccount(username)) {
                    System.out.println("reset here 4");
                    if (resetManager.checkLockPresence(username)) {
                        System.out.println("reset here 5");
                        request.setAttribute("lockreset", "true");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    } else {
                        System.out.println("reset here 6");
                        resetManager.reset(username);
                        request.setAttribute("reset", "true");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    }
                } else {
                    System.out.println("reset here 7");
                    request.setAttribute("absent", "true");
                    request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                }
            } else if (action.equals("change")) {
                System.out.println("reset here 8");
                if (!(request.getParameter("newPass").equals(request.getParameter("newPass2")))) {
                    System.out.println("reset here 9");
                    request.setAttribute("unmatch", "true");
                    request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
                } else {
                    System.out.println("reset here 10");
                    resetManager.resetPassword(request.getParameter("userName"), request.getParameter("newPass"));
                    request.setAttribute("change", "true");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            } else if (action.equals("message")) {
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                ArrayList<ArrayList<String>> inbox = messageManager.getInbox(currentUser);
                int noOfRecords = inbox.size();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                ArrayList<ArrayList<String>> inboxPage = messageManager.inboxPage(inbox, (page - 1) * recordsPerPage, recordsPerPage);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("recordSize", String.valueOf(inboxPage.size()));
                request.setAttribute("currentPage", page);
                request.setAttribute("username", currentUser);
                request.setAttribute("inbox", inboxPage);
                request.getRequestDispatcher("/message.jsp").forward(request, response);
            } else if (action.equals("compose")) {
                System.out.println("username: " + currentUser);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/compose.jsp").forward(request, response);
            } else if (action.equals("createMessage")) {
                System.out.println("Entered message");
                System.out.println("From: " + currentUser);

                System.out.println("To: " + request.getParameter("to"));
                if (messageManager.sendMessage(currentUser, request.getParameter("to"), request.getParameter("subject"), request.getParameter("message"))) {
                    System.out.println("Entered message 2");
                    request.setAttribute("sent", "true");
                    request.setAttribute("role", role);
                    request.setAttribute("username", currentUser);
                    request.getRequestDispatcher("/compose.jsp").forward(request, response);
                } else {
                    System.out.println("Entered message 3");
                    request.setAttribute("missend", "true");
                    request.getRequestDispatcher("/compose.jsp").forward(request, response);
                }
            } else if (action.equals("readMessage")) {
                System.out.println("Message ID: " + request.getParameter("messageid"));
                request.setAttribute("role", role);
                request.setAttribute("message", messageManager.getMessage(request.getParameter("messageid")));
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/readMessage.jsp").forward(request, response);
            } else if (action.equals("replyMessage")) {
                request.setAttribute("role", role);
                request.setAttribute("receiver", request.getParameter("receiver"));
                request.setAttribute("username", request.getParameter("username"));
                subject = "Re: " + messageManager.getMessage(request.getParameter("messageid")).get(1);
                System.out.println("subject: " + "Re: " + messageManager.getMessage(request.getParameter("messageid")).get(1));
                request.setAttribute("subject", "Re: " + messageManager.getMessage(request.getParameter("messageid")).get(1));
                request.getRequestDispatcher("/replyMessage.jsp").forward(request, response);
            } else if (action.equals("replyResult")) {
                messageManager.sendMessage(request.getParameter("username"), request.getParameter("receiver"), subject, request.getParameter("reply"));
                request.setAttribute("reply", "true");
                request.setAttribute("role", role);

                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                ArrayList<ArrayList<String>> inbox = messageManager.getInbox(currentUser);
                int noOfRecords = inbox.size();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                ArrayList<ArrayList<String>> inboxPage = messageManager.inboxPage(inbox, (page - 1) * recordsPerPage, recordsPerPage);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("recordSize", String.valueOf(inboxPage.size()));
                request.setAttribute("currentPage", page);
                request.setAttribute("username", currentUser);
                request.setAttribute("inbox", inboxPage);
                request.getRequestDispatcher("/message.jsp").forward(request, response);
            } else if (action.equals("bulletinBoard")) {
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                ArrayList<ArrayList<String>> board = bulletinManager.getBoard();
                int noOfRecords = board.size();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                ArrayList<ArrayList<String>> boardPage = bulletinManager.boardPage(board, (page - 1) * recordsPerPage, recordsPerPage);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("recordSize", String.valueOf(boardPage.size()));
                request.setAttribute("currentPage", page);
                request.setAttribute("board", boardPage);
                request.setAttribute("username", currentUser);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/bulletinBoard.jsp").forward(request, response);
            } else if (action.equals("composeBulletin")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/composeBulletin.jsp").forward(request, response);
            } else if (action.equals("createBulletin")) {
                request.setAttribute("role", role);
                bulletinManager.releaseMessage(request.getParameter("message"), request.getParameter("subject"));
                request.setAttribute("created", "true");

                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                ArrayList<ArrayList<String>> board = bulletinManager.getBoard();
                int noOfRecords = board.size();
                int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                ArrayList<ArrayList<String>> boardPage = bulletinManager.boardPage(board, (page - 1) * recordsPerPage, recordsPerPage);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("recordSize", String.valueOf(boardPage.size()));
                request.setAttribute("currentPage", page);
                request.setAttribute("board", boardPage);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/bulletinBoard.jsp").forward(request, response);
            } else if (action.equals("readBulletin")) {
                request.setAttribute("role", role);
                ArrayList<String> message = bulletinManager.retrieveMessage(request.getParameter("messageid"));
                request.setAttribute("username", currentUser);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/readBulletin.jsp").forward(request, response);
            } else if (action.equals("productMain")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/productMain.jsp").forward(request, response);
            } else if (action.equals("propertyMain")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/propertyMain.jsp").forward(request, response);
            } else if (action.equals("sessionMain")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                String email = request.getParameter("id");
              //  productSession.signIn(email);
                request.getRequestDispatcher("/sessionMain.jsp").forward(request, response);
            } else if (action.equals("promotionMain")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/promotionOptions.jsp").forward(request, response);
            } else if (action.equals("editPromo")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("data", data);
                request.getRequestDispatcher("/editPromotionMain.jsp").forward(request, response);
            } else if (action.equals("configuration")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/configuration.jsp").forward(request, response);
            } else if (action.equals("createSessionMain")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/createSessionMain.jsp").forward(request, response);
            } else if (action.equals("testCreateEvent")) {
                request.getRequestDispatcher("/testCreateEvent.jsp").forward(request, response);
            } else if (action.equals("createEvent")) {
                productManager.createEvent(request);
                request.setAttribute("created", "true");
                request.getRequestDispatcher("/testCreateEvent.jsp").forward(request, response);
            } else if (action.equals("createSession")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                String info = request.getParameter("id");
                System.out.println(info);
                String[] idType = info.split(" ");
                Long id = Long.valueOf(idType[0]);
                int no = Integer.valueOf(request.getParameter("no"));
                ArrayList data = productSession.getSessionEvent(idType[1], id, no);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/createSession.jsp").forward(request, response);
            } else if (action.equals("sessionCreated")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                int errorChecking = productManager.createSession(request);

                if (errorChecking == 0) {
                    request.setAttribute("error", "true");
                    request.getRequestDispatcher("/createSessionMain.jsp").forward(request, response);
                } else {
                    request.setAttribute("success", "true");
                    request.getRequestDispatcher("/createSessionMain.jsp").forward(request, response);
                }
            } else if (action.equals("deleteMain")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/deleteMain.jsp").forward(request, response);
            } else if (action.equals("deleteSelectSession")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                String info = request.getParameter("id");
                System.out.println(info);
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventSessions(i, idType[1]);
                request.setAttribute("eventType", idType[1]);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/deleteSelectSession.jsp").forward(request, response);
            } else if (action.equals("sessionDeleted")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                String[] id = request.getParameterValues("id");
                productSession.deleteSessions(id);
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("data", data);
                request.setAttribute("deleteSuccess", "true");
                request.getRequestDispatcher("/deleteMain.jsp").forward(request, response);
            } else if (action.equals("editMain")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/editMain.jsp").forward(request, response);
            } else if (action.equals("editSelectSessions")) {
                String info = request.getParameter("id");
                System.out.println(info);
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventSessions(i, idType[1]);
                request.setAttribute("eventType", idType[1]);
                request.setAttribute("data", data);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/editSelectSessions.jsp").forward(request, response);
            } else if (action.equals("editSessions")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                Long i = Long.valueOf(request.getParameter("id"));
                String type = request.getParameter("type");
                ArrayList data = productSession.editSessions(i, type);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/editSessions.jsp").forward(request, response);
            } else if (action.equals("sessionEdited")) {
                //ArrayList data = this.editSession(request);
                ArrayList data = productManager.editSession(request);
                int errorcode = productSession.writeSession(data);

                List<ArrayList> data2 = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data2);
                if (errorcode == 0) {
                    request.setAttribute("error", "true");
                    request.getRequestDispatcher("/editMain.jsp").forward(request, response);
                } else {
                    request.setAttribute("success", "true");
                    request.getRequestDispatcher("/editMain.jsp").forward(request, response);
                }
            } else if (action.equals("promotionCreated")) {
                String[] id;
                String type = request.getParameter("type");
                if (type.equals("3")) {
                    String info = request.getParameter("id");
                    id = new String[1];
                    id[0] = info;
                } else {
                    id = request.getParameterValues("id");
                }
                String name2 = request.getParameter("name");
                double discount = Double.valueOf(request.getParameter("discount"));
                String requirement = request.getParameter("requirement");
                String desc = request.getParameter("description");
                productSession.setPromotion_1(id, type, name2, discount, requirement, desc);
                request.setAttribute("promotionCreated1245", "true");
                request.getRequestDispatcher("/promotionMain.jsp").forward(request, response);
            } else if (action.equals("setPromotion")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/promotionMain.jsp").forward(request, response);
            } else if (action.equals("promotion")) {
                System.out.println("Entered promotion");
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                String type = request.getParameter("type");
                request.setAttribute("type", type);
                System.out.println("Type: " + type);
                if (type.equals("3")) {
                    request.getRequestDispatcher("/promotion_ticketMain.jsp").forward(request, response);
                } else if (type.equals("6")) {
                    request.getRequestDispatcher("/promotion_Customization.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/promotion.jsp").forward(request, response);
                }
            } else if ("promotionCreated".equals(page)) {
                String[] id;
                String type = request.getParameter("type");
                if (type.equals("3")) {
                    String info = request.getParameter("id");
                    id = new String[1];
                    id[0] = info;
                } else {
                    id = request.getParameterValues("id");
                }
                String name2 = request.getParameter("name");
                double discount = Double.valueOf(request.getParameter("discount"));
                String requirement = request.getParameter("requirement");
                String desc = request.getParameter("description");
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("promoteCreated", "true");
                productSession.setPromotion_1(id, type, name2, discount, requirement, desc);
                request.getRequestDispatcher("/promotionMain.jsp").forward(request, response);
            } else if (action.equals("promotion_ticket")) {
                String info = request.getParameter("id");
                System.out.println(info);
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                String promotionType = request.getParameter("type");
                int category = productSession.getCategory(i, idType[1]);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("type", promotionType);
                request.setAttribute("info", info);
                request.setAttribute("category", category);
                request.getRequestDispatcher("/promotion_ticket.jsp").forward(request, response);
            } else if (action.equals("promotionCreated_Customization")) {
                String[] type = request.getParameterValues("type");
                String[] id = request.getParameterValues("id");
                String name2 = request.getParameter("name");
                double discount = Double.valueOf(request.getParameter("discount"));
                String requirement = request.getParameter("requirement");
                String desc = request.getParameter("description");
                productSession.setPromotion_3(type, id, name2, discount, requirement, desc);

                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("customized", "true");
                request.getRequestDispatcher("/promotionMain.jsp").forward(request, response);

            } else if (action.equals("editSelectPromotion")) {
                String info = request.getParameter("id");
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventPromotion(i, idType[1]);
                request.setAttribute("eventType", idType[1]);
                request.setAttribute("data", data);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/editSelectPromotion.jsp").forward(request, response);
            } else if (action.equals("editPromotion")) {
                Long i = Long.valueOf(request.getParameter("id"));
                String type = request.getParameter("type");
                ArrayList data = productSession.editPromotion(i, type);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/editPromotion.jsp").forward(request, response);
            } else if (action.equals("promotionEdited")) {
                productManager.editPromotion(request);
                String id = request.getParameter("eventId").toString() + " " + request.getParameter("eventType");
                String[] idType = id.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventPromotion(i, idType[1]);
                request.setAttribute("eventType", idType[1]);
                request.setAttribute("data", data);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("edited", "true");
                request.getRequestDispatcher("/editSelectPromotion.jsp").forward(request, response);
            } else if (action.equals("deletePromotionMain")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/deletePromotionMain.jsp").forward(request, response);
            } else if (action.equals("deleteSelectPromotion")) {
                String info = request.getParameter("id");
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventPromotion(i, idType[1]);
                request.setAttribute("data", data);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/deleteSelectPromotion.jsp").forward(request, response);
            } else if (action.equals("promotionDeleted")) {
                String[] id = request.getParameterValues("id");
                productSession.deletePromotion(id);

                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.setAttribute("deleted", true);
                request.getRequestDispatcher("/deletePromotionMain.jsp").forward(request, response);
            } else if (action.equals("viewAllProperty")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/viewAllProperty.jsp").forward(request, response);
            } else if (action.equals("concertHallLayout")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/concertHallLayout.jsp").forward(request, response);
            } else if (action.equals("reservationSearch")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/reservationSearch.jsp").forward(request, response);
            } else if (action.equals("reservationSearchResult")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                try {
                    List<Property> properties = rm.getReservationSearchResult(request);
                    List<Property> pRList = rm.checkRecommendation(properties, request);
                    request.setAttribute("pList", properties);
                    request.setAttribute("pRList", pRList);
                    request.getRequestDispatcher("/reservationSearchResult.jsp").forward(request, response);
                } catch (ParseException ex) {
                    Logger.getLogger(BackController.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.getRequestDispatcher("/reservationSearch.jsp").forward(request, response);
            } else if (action.equals("concertHallSelected")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.getRequestDispatcher("/concertHallSelected.jsp").forward(request, response);
            } else if (action.equals("maintenance")) {
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("properties", spm.getAllProperties());
                request.getRequestDispatcher("/maintenance.jsp").forward(request, response);
            } else if (action.equals("generateUser")) {
                productSession.generateUser();
            } else if (action.equals("displaySeatsMain")) {
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.getRequestDispatcher("/displaySeatsMain.jsp").forward(request, response);
            } else if (action.equals("displaySeats")) {
                String info = request.getParameter("id");
                System.out.println(info);
                String[] idType = info.split(" ");
                Long i = Long.valueOf(idType[0]);
                List<ArrayList> data = productSession.searchEventSessions(i, idType[1]);
                List<ArrayList> price = productSession.getSessionsPricing(i, idType[1]);
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.setAttribute("price", price);
                request.getRequestDispatcher("/displaySeats.jsp").forward(request, response);
            } else if (action.equals("seatsPriceCreated")) {
                String apply = request.getParameter("apply");
                System.out.println(apply);
                Long i = Long.valueOf(request.getParameter("id"));
                Integer noCat = Integer.valueOf(request.getParameter("noCat").toString());
                ArrayList <Double> cat = new ArrayList <Double>();
                
                for (int j = 1; j <= noCat; j++){
                    cat.add(Double.valueOf(request.getParameter("cat" + j)));
                }
                productSession.setPricing(i, cat, noCat, apply);
                List<ArrayList> data = productSession.getEventList();
                request.setAttribute("role", role);
                request.setAttribute("username", currentUser);
                request.setAttribute("data", data);
                request.setAttribute("success", "true");
                request.getRequestDispatcher("/displaySeatsMain.jsp").forward(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
