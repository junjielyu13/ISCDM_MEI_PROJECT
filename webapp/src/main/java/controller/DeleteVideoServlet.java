package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import service.VideoService;


@WebServlet(name = "deleteVideoServlet", urlPatterns = {"/jsp/deleteVideoServlet"})
public class DeleteVideoServlet  extends HttpServlet {

    private final VideoService videoService = new VideoService();

    @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            try {
                int videoId = Integer.parseInt(idParam);
                boolean deleted = videoService.deleteVideo(videoId);

                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_OK); // 200
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
        }
    }
}
