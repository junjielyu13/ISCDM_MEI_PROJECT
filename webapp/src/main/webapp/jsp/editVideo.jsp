<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Video" %>
<%@page import="dao.VideoDAO" %>
<%@page import="java.util.Optional" %>
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
    <div class="container">
        <h2>Edit Video</h2>
        <form action="updateVideoServlet" method="post">
            <input type="hidden" name="id" value="<%= video.getId() %>"/>
            <label>Title:</label>
            <input type="text" name="title" value="<%= video.getTitle() %>" required /><br/>

            <label>Description:</label>
            <textarea name="description" required><%= video.getDescription() %></textarea><br/>

            <button type="submit">Save Changes</button>
            <a href="listVideo.jsp">Cancel</a>
        </form>
    </div>
</body>
</html>
