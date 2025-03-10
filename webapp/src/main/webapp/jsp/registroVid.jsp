<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Video</title>
    <link rel="stylesheet" href="../assets/index_1.css">
</head>
<body>

    <div class="form-container">
        
        <div class="user-info">
            <% 
                HttpSession sessionObj = request.getSession();
                User user = (User) sessionObj.getAttribute("user");
                if (Objects.nonNull(user)) {
            %>
                <p>Bienvenido, <%= user.getUsername() %>!</p>
            <% } else { %>
                <p>No ha iniciado sesión.</p>
            <% } %>
        </div>
        
        <h2>Registrar Video</h2>

        <!-- Display error message if it exists -->
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <!-- Display success message if it exists -->
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>

 
        <!-- Updated Form to Support Video Upload -->
        <form id="videoForm" action="videoRegistrationServlet" method="post" enctype="multipart/form-data">
            <!-- Identificador -->

            <!-- Título -->
            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo" required>

            <!-- Descripción -->
            <label for="descripcion">Descripción:</label>
            <textarea id="descripcion" name="descripcion" rows="4" required></textarea>

            <!-- Video File Upload -->
            <label for="video">Seleccionar Video:</label>
            <input type="file" id="video" name="video" accept="video/*" required>

            <!-- Submit Button -->
            <input type="submit" value="Registrar Video">
            
        </form>
        
        <button onclick="location.href='listadoVid.jsp'">Ver Lista de Video</button>
        
    </div>
</body>
</html>
