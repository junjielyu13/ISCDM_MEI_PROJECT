<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Video" %>
<%@page import="service.VideoService" %>
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
        video = new VideoService().findById(id); 

        if (video == null) {
            response.sendRedirect("listVideos.jsp");
            return; 
        }
        new VideoService().incrementViews(id);
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
    <body class="container-list-video">
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
             justify-content: space-between;
             align-items: center;
             flex-wrap: nowrap;">

            <div class="register-btn">
                <a href="listVideo.jsp" class="btn">Back Video List</a>
            </div>

            <h2>Video: <%= video.getTitle() %></h2>

            <div>
            </div>
        </div>

        <div style="display: flex;
             justify-content: center;
             align-items: center;
             flex-wrap: nowrap;">

            <video width="640" height="360" controls>
                <source src="http://localhost:8080/webapp<%= video.getUrl() %>" type="video/mp4">
            </video>

        </div>

        <h3>Descriptions: <%= video.getDescription() %></h3>
        <h3>Views: <%= video.getViews() %></h3>

    </body>
</html>