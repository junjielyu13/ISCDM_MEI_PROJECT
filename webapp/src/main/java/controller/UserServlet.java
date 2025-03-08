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

@WebServlet(name = "userServlet", urlPatterns = {"/jsp/userServlet"})
public class UserServlet extends HttpServlet { 

    private final UserService usuarioService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Bienvenido, elige una opción</h1>");
            out.println("<h2>Login</h2>");
            out.println("<form action='/jsp/userServlet' method='POST'>");
            out.println("Username: <input type='text' name='user' /><br>");
            out.println("Password: <input type='password' name='passwd' /><br>");
            out.println("<input type='submit' value='Login' name='action' />");
            out.println("</form>");
            out.println("<h2>Register</h2>");
            out.println("<form action='/jsp/userServlet' method='POST'>");
            out.println("Username: <input type='text' name='user' /><br>");
            out.println("Password: <input type='password' name='passwd' /><br>");
            out.println("Email: <input type='email' name='email' /><br>");
            out.println("<input type='submit' value='Register' name='action' />");
            out.println("</form>");
            out.println("</body></html>");
        }
    }

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

        // 输出响应的打印对象
        try (PrintWriter out = response.getWriter()) {
            // 处理没有传入 action 的情况
            if (action == null || action.trim().isEmpty()) {
                showErrorMessage(out, "Acción no válida");
                return;
            }

            // 根据动作进行不同的操作
            switch (action) {
                case "Login":
                    handleLogin(request, response, out, username, passwd);
                    break;
                case "Register":
                    handleRegister(request, response, out, username, passwd, email, name, surname);
                    break;
                default:
                    showErrorMessage(out, "Acción no válida");
                    break;
            }
        }
    }

    // 登录处理逻辑
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String username, String passwd) throws IOException {
        // 校验用户名和密码
        if (username == null || passwd == null || username.trim().isEmpty() || passwd.trim().isEmpty()) {
            showErrorMessage(out, "Todos los campos son obligatorios para el inicio de sesión.");
            return;
        }

        User user = new User(username, passwd);
        boolean existe = usuarioService.validarUsuario(user);

        if (existe) {
            out.println("<html><body><h1>Bienvenido, " + user.getUsername() + "!</h1></body></html>");
        } else {
            out.println("<html><body><h1>Usuario o contraseña incorrectos</h1></body></html>");
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

    private void showErrorMessage(PrintWriter out, String message) {
        out.println("<html><body><h1>Error: " + message + "</h1></body></html>");
    }
}
        