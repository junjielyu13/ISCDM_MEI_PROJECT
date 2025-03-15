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
    
    <form action="userServlet" method="post">
        <input type="hidden" name="action" value="login" />
        
        <label for="usuario">Email:</label>
        <input type="email" id="email" name="email" required />

        <label for="password">Password: </label>
        <input type="password" id="password" name="password" required />

        <button type="submit" class="btn">Login</button>
    </form>

    <div class="register-btn">
        <a href="registrationUser.jsp" class="btn">Register</a>
    </div>

</div>

</body>
</html>
