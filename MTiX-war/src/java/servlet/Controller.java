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
import manager.LoginManager;
import manager.RegisterManager;
import session.stateless.LoginSessionLocal;
import session.stateless.RegisterSessionLocal;

/**
 *
 * @author Student-ID
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller", "/Controller?*"})
public class Controller extends HttpServlet {

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
            RegisterManager registerManager = new RegisterManager(registerSession);
            LoginManager loginManager = new LoginManager(loginSession);

            String action = request.getParameter("action");

            System.out.println("Action = " + action);

            if (action.equals("register")) {
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            } else if (action.equals("doRegistration")) {

                if (!(registerManager.checkConflict(request.getParameter("userName")))) {
                    if (request.getParameter("password").equals(request.getParameter("passwordAgain"))) {
                        registerManager.register(request.getParameter("userName"), request.getParameter("password"), request.getParameter("mobileNumber"));
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    } else {
                        request.setAttribute("mismatch", "true");
                        request.getRequestDispatcher("/register.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("conflict", "true");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
            } else if (action.equals("doLogin")) {
                System.out.println(request.getParameter("userName") + "      " + request.getParameter("password"));
                String username = request.getParameter("userName");
                String password = request.getParameter("password");

                if (loginManager.checkVerification(username)) {
                    request.setAttribute("verification", "true");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                } else {
                    if (loginManager.identify(username, password)) {
                        request.getRequestDispatcher("/home.jsp").forward(request, response);
                    }
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
