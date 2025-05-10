<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Video" %>
<%@page import="dao.VideoDAO" %>
<%@page import="java.util.Optional" %>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<%
    String idParam = request.getParameter("id");
    int id = -1;
    Video video = null;

    try {
        id = Integer.parseInt(idParam);
        video = new VideoDAO().findById(id); 

        if (video == null) {
            response.sendRedirect("listVideos.jsp");
            return; 
        }
    } catch (Exception e) {
        response.sendRedirect("listVideos.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Edit Video</title>
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

            <div style="display: flex;
                 flex-direction: row;
                 align-items: center;
                 justify-content: center;">
                <h2>Edit Video</h2>
            </div>

            <form id="updateForm">
                <input type="hidden" name="id" value="<%= video.getId() %>"/>
                <label>Title:</label>
                <input type="text" name="title" value="<%= video.getTitle() %>" required /><br/>

                <label>Description:</label>
                <textarea name="description" required><%= video.getDescription() %></textarea><br/>

                <input type="submit" value="Update Video">
            </form>

            <div class="register-btn">
                <a href="listVideo.jsp">Cancel</a>
            </div>
        </div>
    </body>

    <script>
        document.getElementById('updateForm').addEventListener('submit', async function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const data = new URLSearchParams();
            for (const pair of formData) {
                data.append(pair[0], pair[1]);
            }

            const response = await fetch('updateVideoServlet', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data
            });

            if (response.ok) {
                window.location.href = 'listVideo.jsp';
            } else {
                alert('Error updating video');
            }
        });
    </script>

</html>
