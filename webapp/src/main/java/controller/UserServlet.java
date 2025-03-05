package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;

/**
 *
 * @author alumness
 */
@WebServlet(name = "userServlet", urlPatterns = {"/jsp/userServlet"})
public class UserServlet extends HttpServlet { 

    private final UserService usuarioService = new UserService();
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {    
                out.println("<html><body>Method not supported</body></html>");
        }
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String username = request.getParameter("user");
            String passwd = request.getParameter("passwd");
            
            User user = new User(username, passwd);
            boolean existe = usuarioService.validarUsuario(user);                        
            
            String result;
            if (existe) {
                result = "El usuario existe";
                // response.getWriter().write("El usuario existe");
            } else {
                result = "El usuario no exist";
                //response.getWriter().write("El usuario no existe");
            }
                    
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet servletEjemplo</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> " + result + " </h1>");
            out.println("</body>");
            out.println("</html>");
        }
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