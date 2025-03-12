package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Video;
import service.VideoService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "videoListServlet", urlPatterns = {"/jsp/videoListServlet"})
public class VideoListServlet extends HttpServlet {

    private final VideoService videoService = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Video> videoList = videoService.getAllVideo();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        StringBuilder tableRows = new StringBuilder();
        if (videoList != null && !videoList.isEmpty()) {
            for (Video video : videoList) {
                tableRows.append("<tr>")
                         .append("<td>").append(video.getId()).append("</td>")
                         .append("<td>").append(video.getTitle()).append("</td>")
                         .append("<td>").append(video.getUserId()).append("</td>")
                         .append("<td>").append(video.getUploadedAt()).append("</td>")
                         .append("<td>").append(video.getDuration()).append("</td>")
                         .append("<td>").append(video.getViews()).append("</td>")
                         .append("<td>").append(video.getDescription()).append("</td>")
                         .append("<td>").append(video.getFormat()).append("</td>")
                         .append("</tr>");
            }
        } else {
            tableRows.append("<tr><td colspan='8' style='text-align:center;'>There are no videos registered</td></tr>");
        }

        response.getWriter().write(tableRows.toString());
    }
   
}
