package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Video;
import model.User;
import service.UserService;
import service.VideoService;
import util.DateTimeUtil;


@WebServlet(name = "videoListServlet", urlPatterns = {"/jsp/videoListServlet"})
public class VideoListServlet extends HttpServlet {

    private final VideoService videoService = new VideoService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Video> videoList = videoService.getAllVideo();
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession sessionObj = request.getSession();
        User userActual = (User) sessionObj.getAttribute("user");
        
        String tableRows = generateTableRows(videoList, userActual);
        response.getWriter().write(tableRows);
    }

    private String generateTableRows(List<Video> videoList, User userActual) {
        StringBuilder tableRows = new StringBuilder();
        

         
        if (videoList == null || videoList.isEmpty()) {
            return "<tr><td colspan='8' style='text-align:center;'>There are no videos registered</td></tr>";
        }
        
        for (Video video : videoList) {
            User user = userService.getUserByID(String.valueOf(video.getUserId()));
            String createDate = DateTimeUtil.formatIsoToLocal(video.getUploadedAt().toString());
            if (user != null) {
                tableRows.append("<tr>")
                         .append("<td>").append(video.getId()).append("</td>")
                         .append("<td>").append(video.getTitle()).append("</td>")
                         .append("<td>").append(user.getUsername()).append("</td>")
                         .append("<td>").append(createDate).append("</td>")
                         .append("<td>").append(video.getDuration()).append("</td>")
                         .append("<td>").append(video.getViews()).append("</td>")
                         .append("<td>").append(video.getDescription()).append("</td>")
                         .append("<td>").append(video.getFormat()).append("</td>")
                         .append("<td>")
                            .append("<button onclick=\"playVideo(").append(video.getId()).append(")\">Play</button> ");


                
                if (user.getIdUser() == userActual.getIdUser() ){
                    tableRows.append("<button onclick=\"editVideo(").append(video.getId()).append(")\">Edit</button> ")
                             .append("<button onclick=\"deleteVideo(").append(video.getId()).append(")\">Delete</button>");
                }
                
                tableRows.append("</td>")
                         .append("</tr>");
            }
            
            
        }
        
        return tableRows.toString();
    }
}
