package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import model.User;
import model.Video;
import service.VideoService;

@WebServlet(name = "videoRegistrationServlet", urlPatterns = {"/jsp/videoRegistrationServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class VideoRegistrationServlet extends HttpServlet {

    private final VideoService videoService = new VideoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "User not logged in or does not exist.");
            request.getRequestDispatcher("/jsp/registrationVideo.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part videoPart = request.getPart("video");
        String fileName = videoPart.getSubmittedFileName();

        try {
            Video video = videoService.downloadVideo(title, description, videoPart, fileName, user);
            
            if (videoService.registerVideo(video)) {
                request.setAttribute("success", "Video uploaded and registered successfully!");
            } else {
                request.setAttribute("error", "Video registration failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid video file: " + e.getMessage());
        } catch (IOException e) {
            request.setAttribute("error", "Failed to upload video: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/registrationVideo.jsp").forward(request, response);
    }

}