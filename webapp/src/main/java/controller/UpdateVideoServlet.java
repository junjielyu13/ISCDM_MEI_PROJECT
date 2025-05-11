package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import model.Video;
import service.VideoService;
import util.DateTimeUtil;

@WebServlet(name = "updateVideoServlet", urlPatterns = {"/jsp/updateVideoServlet"})
public class UpdateVideoServlet extends HttpServlet {

    private final VideoService videoService = new VideoService();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = DateTimeUtil.parseFormData(request);

        int id = Integer.parseInt(params.get("id"));
        String title = params.get("title");
        String description = params.get("description");

        Video video = videoService.findById(id);

        if (video != null) {
            video.setTitle(title);
            video.setDescription(description);
            videoService.updateVideo(video);
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content for successful PUT
    }
}
