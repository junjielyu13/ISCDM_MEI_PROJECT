<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Video Registration</title>
    <link rel="stylesheet" href="../assets/index.css">
</head>
<body>

    <div class="form-container">
        
        <div class="user-info">
            <%
                HttpSession sessionObj = request.getSession();
                User user = (User) sessionObj.getAttribute("user");
                if (Objects.isNull(user)) {
                    response.sendRedirect("privacy.jsp");
                    return;
                }
            %>
            <p>Welcome, <%= user.getUsername() %>!  <a href="login.jsp">change user</a> </p>
        </div>
        
        <h2>Register Video</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

 
        <form id="videoForm" action="videoRegistrationServlet" method="post" enctype="multipart/form-data">

            <label for="titulo">Title:</label>
            <input type="text" id="title" name="title" required>

            
            <label for="descripcion">Description:</label>
            <textarea id="descripcion" name="description" rows="4" required></textarea>

            <label for="video">Select Video:</label>
            <input type="file" id="video" name="video" accept="video/*" required>

            <input type="submit" value="Register Video">
            
        </form>
        
        <div class="register-btn">
            <a href="listVideo.jsp" class="btn">View Video List</a>
        </div>
        
        <div class="register-btn">
            <a href="search.jsp" class="btn">Search</a>
        </div>
        
    </div>
</body>
</html>
