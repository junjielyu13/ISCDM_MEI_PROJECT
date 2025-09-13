<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>
        <link rel="stylesheet" href="../assets/index.css">
    </head>
    <body>

        <div class="container">
            <%
                HttpSession sessionObj = request.getSession();
                sessionObj.removeAttribute("user");
            %>

            <h2>Login</h2>

            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <form id="loginForm">
                <input type="hidden" name="action" value="login" />

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required />

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required />

                <button type="submit" class="btn">Login</button>
            </form>

            <div class="register-btn">
                <a href="registrationUser.jsp" class="btn">Register</a>
            </div>

        </div>

    </body>

    <script>
    document.getElementById("loginForm").addEventListener("submit", function(e) {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("userServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                action: "login",
                email: email,
                password: password
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem("jwt", data.token);
                console.log(data.token);
                window.location.href = "listVideo.jsp";
            } else {
                alert(data.error);
            }
        })
        .catch(err => {
            console.log("Login error:", err);
        });
    });
    </script>
</html>
