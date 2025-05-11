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

        String tableRows = videoService.generateTableRows(videoList, userActual);
        response.getWriter().write(tableRows);
    }

}
