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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>reproduction</title>
        <link rel="stylesheet" href="../assets/index.css">
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <video width="640" height="360" controls>
            <source src="http://localhost:8080/webapp<%= video.getUrl() %>" type="video/mp4">
        </video>
        
        <a href="listVideo.jsp">to list</a>
    </body>
</html>