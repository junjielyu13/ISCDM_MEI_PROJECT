package controller;

import filters.AuthFilter;
import crypto.XMLCrypto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Video;
import model.User;
import service.UserService;
import service.VideoService;

@WebServlet(name = "videoListServlet", urlPatterns = {"/jsp/videoListServlet"})
public class VideoListServlet extends HttpServlet {

    private static final String xmlPath = "/home/alumne/ISCDM_MEI_PROJECT/webapp/src/main/java/crypto/xml/";

    private final VideoService videoService = new VideoService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            XMLCrypto.encryptXML(xmlPath + "didlFilm1.xml", xmlPath + "didlFilm1.encrypted.xml", "Resource");
            XMLCrypto.decryptXML(xmlPath + "didlFilm1.encrypted.xml", xmlPath + "didlFilm1.decrypted.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header.");
            return;
        }

        String token = authHeader.substring(7);

        User userActual = null;
        try {
            String email = AuthFilter.getEmailFromToken(token);
            userActual = userService.getUserByEmail(email);
            if (userActual == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid user.");
                return;
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token.");
            return;
        }

        List<Video> videoList = videoService.getAllVideo();
        String tableRows = videoService.generateTableRows(videoList, userActual);
        response.getWriter().write(tableRows);
    }

}
