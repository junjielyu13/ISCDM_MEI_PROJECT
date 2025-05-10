/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.SearchVideoServelet to edit this template
 */
package controller;

import dao.UserDAO;
import dao.VideoDAO;
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
import util.DateTimeUtil;


@WebServlet(name = "searchVideoServlet", urlPatterns = {"/jsp/searchVideoServlet"})
public class SearchVideoServelet extends HttpServlet {

    private final UserService userService = new UserService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Video> videoList = new ArrayList<>();

        String criterio = request.getParameter("criterio");
        String valorBusqueda = request.getParameter("valorBusqueda");

        VideoDAO dao = new VideoDAO();
        UserDAO userDao = new UserDAO();
        
        switch (criterio) {
            case "autor":
                List<User> userList = userDao.searchUsersByName(valorBusqueda);
                for (User user : userList) {
                    List<Video> videos = dao.getVideosByUserId(user.getIdUser());
                    videoList.addAll(videos);
                }
                break;
            case "titulo":
                videoList = dao.getVideosByTitle(valorBusqueda);
                break;
            case "fecha":
                videoList = dao.getVideosByDate(valorBusqueda);
                break;
            default:
                break;
        }
   
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
