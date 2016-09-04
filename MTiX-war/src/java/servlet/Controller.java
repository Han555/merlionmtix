/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.LockManager;
import manager.LoginManager;
import manager.RegisterManager;
import manager.ResetPasswordManager;
import manager.UnlockManager;
import session.stateless.LockAccountSessionLocal;
import session.stateless.LoginSessionLocal;
import session.stateless.RegisterSessionLocal;
import session.stateless.ResetPasswordSessionLocal;
import session.stateless.UnlockAccountSessionLocal;

/**
 *
 * @author Student-ID
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller", "/Controller?*"})
public class Controller extends HttpServlet {

    @EJB
    private ResetPasswordSessionLocal resetPasswordSession;

    @EJB
    private UnlockAccountSessionLocal unlockAccountSession;

    @EJB
    private LockAccountSessionLocal lockAccountSession;

    @EJB
    private LoginSessionLocal loginSession;
    @EJB
    private RegisterSessionLocal registerSession;

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

        try {
            String action;
            RegisterManager registerManager = new RegisterManager(registerSession);
            LoginManager loginManager = new LoginManager(loginSession);
            LockManager lockManager = new LockManager(lockAccountSession);
            UnlockManager unlockManager = new UnlockManager(unlockAccountSession);
            ResetPasswordManager resetManager = new ResetPasswordManager(resetPasswordSession);

            action = request.getParameter("action");
            String name = request.getParameter("name");
            System.out.println("here special");
            if (name != null) {
                System.out.println("here special 2");
                if (action == null) {
                    System.out.println("here special 3");
                    action = "verify2";
                }
            }

            System.out.println("Action = " + action);

            if (action.equals("register")) {
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            } else if (action.equals("doRegistration")) {

                if (request.getParameter("cap1").equals(request.getParameter("cap2"))) {
                    if (!(registerManager.checkConflict(request.getParameter("userName")))) {
                        if (request.getParameter("password").equals(request.getParameter("passwordAgain"))) {
                            registerManager.register(request.getParameter("userName"), request.getParameter("password"), request.getParameter("mobileNumber"));

                            registerManager.sendEmail(request.getParameter("userName"), "is3102mtix@gmail.com", "http://localhost:8080/MTiX-war/Controller?" + "name=" + request.getParameter("userName"), "MTiX Account Verification", "smtp.gmail.com");
                            request.setAttribute("registered", "true");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        } else {
                            request.setAttribute("mismatch", "true");
                            request.getRequestDispatcher("/register.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("conflict", "true");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("captcha", "true");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
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
                        if(resetManager.resetted(username)) {
                            System.out.println("reset here 1");
                            request.setAttribute("username", username);
                            request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
                        }
                        if (lockManager.passThrough(username)) {
                            System.out.println("here new 1");
                            request.getRequestDispatcher("/home.jsp").forward(request, response);
                        }
                        if (lockManager.finalLock(username)) {
                            System.out.println("here 3");
                            request.setAttribute("locked", "true");
                            request.setAttribute("account", username);
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        } else {
                            System.out.println("here 4");
                            request.getRequestDispatcher("/home.jsp").forward(request, response);
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
            } else if (action.equals("resetPassword")) {
                System.out.println("reset here 2");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
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
                if(!(request.getParameter("newPass").equals(request.getParameter("newPass2")))) {
                    System.out.println("reset here 9");
                    request.setAttribute("unmatch", "true");
                    request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
                } else {
                    System.out.println("reset here 10");
                    resetManager.resetPassword(request.getParameter("userName"), request.getParameter("newPass"));
                    request.setAttribute("change", "true");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
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
