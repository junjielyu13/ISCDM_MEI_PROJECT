package controller;

import filters.AuthFilter;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import model.User;
import service.UserService;

@WebServlet(name = "userServlet", urlPatterns = {"/jsp/userServlet"})
public class UserServlet extends HttpServlet { 

    private final UserService userService = new UserService();

    @Override
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            return;
        }

        switch (action) {
            case "login":
                handleLogin(request, response, email, passwd);
                break;
            case "Register":
                handleRegister(request, response, username, passwd, email, name, surname);
                break;
            default:
                break;
        }   
    }

 
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, String email, String passwd) throws IOException {
    response.setContentType("application/json;charset=UTF-8");

    if (email == null || passwd == null || email.trim().isEmpty() || passwd.trim().isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"error\": \"All fields are required for login.\"}");
        return;
    }

    User user = new User(email, passwd);
    boolean exists = userService.validUser(user);

    if (exists) {
        User sessionUser = userService.getUserByEmail(email);
        HttpSession session = request.getSession();
        session.setAttribute("user", sessionUser);
        session.setMaxInactiveInterval(60 * 60);

    String jwt = AuthFilter.generateToken(sessionUser.getEmail());

        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
    } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Login failed. Invalid credentials.\"}");
    }
}


    private void handleRegister(HttpServletRequest request, HttpServletResponse response, String username, String passwd, String email, String name, String surname) throws ServletException, IOException {
        if (username == null || username.trim().isEmpty() || passwd == null || passwd.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required for registration.");
            request.getRequestDispatcher("/jsp/registrationUser.jsp").forward(request, response);
            return;
        }
        User newUser = new User(username, passwd, email, name, surname);
        boolean registrado = userService.registerUser(newUser);

        if (registrado) { 
            request.setAttribute("success", "User registered successfully. Please, <a href='login.jsp'>log in</a>.");
            request.getRequestDispatcher("/jsp/registrationUser.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Error registering the user, the user has already been registered.");
            request.getRequestDispatcher("/jsp/registrationUser.jsp").forward(request, response);
        }
    }
}
        