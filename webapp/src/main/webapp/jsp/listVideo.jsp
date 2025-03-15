<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>List of Registered Videos</title>
    <link rel="stylesheet" href="../assets/index.css">
</head>
<body>

<div class="container-list-video">
    <div style="display: flex;
         justify-content: space-between;
         align-items: center;
         flex-wrap: nowrap;">
        <div class="user-info">
            <% 
                HttpSession sessionObj = request.getSession();
                User user = (User) sessionObj.getAttribute("user");
                if (Objects.nonNull(user)) {
            %>
                <p>Welcome, <%= user.getUsername() %>!  <a href="login.jsp">change user</a> </p>
            <% } else { %>
                <p>Not logged in, please, <a href='login.jsp'>log in</a>.</p>
            <% } %>
        </div>
        <div><h2>List of Registered Videos</h2></div>
        <div>
            <button class="container-list-video-add-btn btn" onclick="location.href='registrationVideo.jsp'">Add Video</button>
        </div>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Creation Date</th>
                <th>Duration (segons)</th>
                <th>Views</th>
                <th>Description</th>
                <th>Format</th>
            </tr>
        </thead>
        <tbody id="videoTableBody">
            <tr><td colspan="8" style="text-align:center;">Loading videos...</td></tr>
        </tbody>
    </table>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        fetch("videoListServlet") 
            .then(response => response.text()) 
            .then(html => {
                document.getElementById("videoTableBody").innerHTML = html;  
            })
            .catch(error => {
                console.error("Error al cargar videos:", error);
                document.getElementById("videoTableBody").innerHTML = "<tr><td colspan='8' style='text-align:center;color:red;'>Error al cargar videos</td></tr>";
            });
    });
</script>

</body>
</html>
