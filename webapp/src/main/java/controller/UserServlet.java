package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;

@WebServlet(name = "userServlet", urlPatterns = {"/jsp/userServlet"})
public class UserServlet extends HttpServlet { 

    private final UserService usuarioService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            if (action == null || action.trim().isEmpty()) {
                return;
            }

            switch (action) {
                case "login":
                    handleLogin(request, response, out, username, passwd);
                    break;
                case "Register":
                    handleRegister(request, response, out, username, passwd, email, name, surname);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String username, String passwd) throws ServletException, IOException {
        if (username == null || passwd == null || username.trim().isEmpty() || passwd.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios para el inicio de sesión.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        User user = new User(username, passwd);
        boolean existe = usuarioService.validarUsuario(user);

        if (existe) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(60 * 60);
        
            response.sendRedirect(request.getContextPath() + "/jsp/ejemplo.jsp");
        } else {
            request.setAttribute("error", "login failed");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String username, String passwd, String email, String name, String surname) throws ServletException, IOException {
        if (username == null || username.trim().isEmpty() || passwd == null || passwd.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Register Todos los campos son obligatorios para el registro.");
            request.getRequestDispatcher("/jsp/registroUsu.jsp").forward(request, response);
            return;
        }
        User newUser = new User(username, passwd, email, name, surname);
        boolean registrado = usuarioService.registrarUsuario(newUser);

        if (registrado) {
            request.setAttribute("success", "Usuario registrado correctamente. Por favor, <a href='login.jsp'>inicie sesión</a>.");
            request.getRequestDispatcher("/jsp/registroUsu.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error al registrar el usuario, el usuario ha registrado");
            request.getRequestDispatcher("/jsp/registroUsu.jsp").forward(request, response);
        }
    }
}
        