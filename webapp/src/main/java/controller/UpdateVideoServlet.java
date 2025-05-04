package controller;


import dao.VideoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Video;

@WebServlet(name = "updateVideoServlet ", urlPatterns = {"/jsp/updateVideoServlet"})
public class UpdateVideoServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");

            VideoDAO dao = new VideoDAO();
            Video video = dao.findById(id);

            if (video != null) {
                video.setTitle(title);
                video.setDescription(description);
                dao.update(video);
            }

            response.sendRedirect("listVideo.jsp");
        }
}
