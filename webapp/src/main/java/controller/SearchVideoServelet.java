package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import model.Video;
import service.UserService;
import service.VideoService;

@WebServlet(name = "searchVideoServlet", urlPatterns = {"/jsp/searchVideoServlet"})
public class SearchVideoServelet extends HttpServlet {

    private final VideoService videoService = new VideoService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Video> videoList = new ArrayList<>();

        String criterio = request.getParameter("criterio");
        String valorBusqueda = request.getParameter("valorBusqueda");

        switch (criterio) {
            case "autor":
                List<User> userList = userService.searchUsersByName(valorBusqueda);
                for (User user : userList) {
                    List<Video> videos = videoService.getVideosByUserId(user.getIdUser());
                    videoList.addAll(videos);
                }
                break;
            case "titulo":
                videoList = videoService.getVideosByTitle(valorBusqueda);
                break;
            case "fecha":
                videoList = videoService.getVideosByDate(valorBusqueda);
                break;
            default:
                break;
        }

        HttpSession sessionObj = request.getSession();
        User userActual = (User) sessionObj.getAttribute("user");

        String tableRows = videoService.generateTableRows(videoList, userActual);
        response.getWriter().write(tableRows);
    }
}
