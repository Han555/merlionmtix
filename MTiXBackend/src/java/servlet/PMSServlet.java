/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import session.stateless.ProductSessionLocal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.ProductManager;

/**
 *
 * @author JingYing
 */
public class PMSServlet extends HttpServlet {

    @EJB
    private ProductSessionLocal session;
    HttpSession httpSession;
    //  private SessionManager sessionManager = new SessionManager(session);

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

        ProductManager manager = new ProductManager(session);

        RequestDispatcher dispatcher = null;
        ServletContext servletContext = getServletContext();
        System.out.println("##############################");
        String page = request.getPathInfo();
        System.out.println(page);
        page = page.substring(1);
        System.out.println(page);

        if ("generateUser".equals(page)) {
            session.generateUser();
        } else if ("signInCompleted".equals(page)) {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            //Long userID = session.signIn(name, password);

            httpSession = request.getSession();
           // httpSession.setAttribute("user", userID);//userName will be stored as user
            response.reset();
            httpSession = request.getSession();
            System.out.print(httpSession.getAttribute("user").toString());
        } else if ("eventCreated".equals(page)) { //Create Event
            // ArrayList list = this.createEvent(request);
            manager.createEvent(request);
        } else if ("createSessionMain".equals(page)) {
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("createSession".equals(page)) {
            String info = request.getParameter("id");
            System.out.println(info);
            String[] idType = info.split(" ");
            Long id = Long.valueOf(idType[0]);
            int no = Integer.valueOf(request.getParameter("no"));
            ArrayList data = session.getSessionEvent(idType[1], id, no);
            request.setAttribute("data", data);
        } else if ("sessionCreated".equals(page)) {
            int errorChecking = manager.createSession(request);

            if (errorChecking == 0) {
                page = "sessionError";
            }
        } else if ("editMain".equals(page)) {
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("editSelectSessions".equals(page)) {
            String info = request.getParameter("id");
            System.out.println(info);
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            List<ArrayList> data = session.searchEventSessions(i, idType[1]);
            request.setAttribute("eventType", idType[1]);
            request.setAttribute("data", data);
        } else if ("editSessions".equals(page)) {
            Long i = Long.valueOf(request.getParameter("id"));
            String type = request.getParameter("type");
            ArrayList data = session.editSessions(i, type);
            request.setAttribute("data", data);
        } else if ("sessionEdited".equals(page)) {
            //ArrayList data = this.editSession(request);
            ArrayList data = manager.editSession(request);
            int errorcode = session.writeSession(data);
            if (errorcode == 0) {
                page = "sessionError";
            }
        } else if ("deleteMain".equals(page)) {
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("deleteSelectSession".equals(page)) {
            String info = request.getParameter("id");
            System.out.println(info);
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            List<ArrayList> data = session.searchEventSessions(i, idType[1]);
            request.setAttribute("eventType", idType[1]);
            request.setAttribute("data", data);
        } else if ("sessionDeleted".equals(page)) {
            String[] id = request.getParameterValues("id");
            session.deleteSessions(id);
        } else if ("subEventCreated".equals(page)) {
            manager.createSubEvent(request);
        } else if ("displaySeatsMain".equals(page)) {
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("displaySeats".equals(page)) {
            String info = request.getParameter("id");
            System.out.println(info);
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            List<ArrayList> data = session.searchEventSessions(i, idType[1]);
            List<ArrayList> price = session.getSessionsPricing(i, idType[1]);
            request.setAttribute("data", data);
            request.setAttribute("price", price);
        } else if ("viewPrice".equals(page)) {
            Long i = Long.valueOf(request.getParameter("id"));
            ArrayList data = session.getPricing(i);
            request.setAttribute("data", data);
        } else if ("seatsPriceCreated".equals(page)) {
            String apply = request.getParameter("apply");
            System.out.println(apply);
            Long i = Long.valueOf(request.getParameter("id"));
            System.out.println(i);
            double cat1 = Double.valueOf(request.getParameter("cat1"));
            double cat2 = Double.valueOf(request.getParameter("cat2"));
            double cat3 = Double.valueOf(request.getParameter("cat3"));
            //session.setPricing(i, cat1, cat2, cat3, apply);
        } else if ("promotion".equals(page)) {
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
            String type = request.getParameter("type");
            request.setAttribute("type", type);
            if (type.equals("3")) {
                page = "promotion_ticketMain";
            } else if (type.equals("6")) {
                page = "promotion_Customization";
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
            String name = request.getParameter("name");
            double discount = Double.valueOf(request.getParameter("discount"));
            String requirement = request.getParameter("requirement");
            String desc = request.getParameter("description");
            session.setPromotion_1(id, type, name, discount, requirement, desc);
        } else if ("promotion_ticket".equals(page)) {
            String info = request.getParameter("id");
            System.out.println(info);
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            String promotionType = request.getParameter("type");
            int category = session.getCategory(i, idType[1]);
            request.setAttribute("type", promotionType);
            request.setAttribute("info", info);
            request.setAttribute("category", category);
        } else if ("promotionCreated_Customization".equals(page)) {
            String[] type = request.getParameterValues("type");
            String[] id = request.getParameterValues("id");
            String name = request.getParameter("name");
            double discount = Double.valueOf(request.getParameter("discount"));
            String requirement = request.getParameter("requirement");
            String desc = request.getParameter("description");
            session.setPromotion_3(type, id, name, discount, requirement, desc);
        } else if ("editPromotionMain".equals(page)){
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("editSelectPromotion".equals(page)){
            String info = request.getParameter("id");
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            List<ArrayList> data = session.searchEventPromotion(i, idType[1]);
            request.setAttribute("eventType", idType[1]);
            request.setAttribute("data", data);
        } else if ("editPromotion".equals(page)){
            Long i = Long.valueOf(request.getParameter("id"));
            String type = request.getParameter("type");
            ArrayList data = session.editPromotion(i, type);
            request.setAttribute("data", data);
        } else if ("promotionEdited".equals(page)){
            manager.editPromotion(request);
        } else if ("deletePromotionMain".equals(page)){
            List<ArrayList> data = session.getEventList();
            request.setAttribute("data", data);
        } else if ("deleteSelectPromotion".equals(page)){
            String info = request.getParameter("id");
            String[] idType = info.split(" ");
            Long i = Long.valueOf(idType[0]);
            List<ArrayList> data = session.searchEventPromotion(i, idType[1]);
            request.setAttribute("data", data);
        } else if ("promotionDeleted".equals(page)){
            String[] id = request.getParameterValues("id");
            session.deletePromotion(id);
        }

        dispatcher = servletContext.getNamedDispatcher(page);
        dispatcher.forward(request, response);

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
