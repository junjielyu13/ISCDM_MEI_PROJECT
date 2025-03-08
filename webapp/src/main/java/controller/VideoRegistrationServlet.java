package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import model.Video;
import service.VideoService;

/**
 *
 * @author alumne
 */
@WebServlet(name = "videoRegistrationServlet", urlPatterns = {"/jsp/videoRegistrationServlet"})
public class VideoRegistrationServlet extends HttpServlet {
    
    private final VideoService videoService = new VideoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get form parameters (title, description, format, etc.)
        String title = request.getParameter("titulo");
        String author = request.getParameter("autor");
        String description = request.getParameter("descripcion");
        String format = request.getParameter("formato");

        // Get the uploaded video file
        Part videoPart = request.getPart("video");
        String fileName = videoPart.getSubmittedFileName();

        // Ensure the video file is valid and not empty
        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("error", "Please select a valid video file.");
            request.getRequestDispatcher("/jsp/yourUploadPage.jsp").forward(request, response);
            return;
        }

        // Generate a unique file name to avoid overwriting
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Specify the folder where videos will be stored (e.g., /uploads/videos)
        String uploadDir = getServletContext().getRealPath("/uploads/videos");
        File uploadDirFolder = new File(uploadDir);

        if (!uploadDirFolder.exists()) {
            uploadDirFolder.mkdirs(); // Create the folder if it doesn't exist
        }

        // Save the video file to the server
        File uploadedFile = new File(uploadDir, uniqueFileName);
        videoPart.write(uploadedFile.getAbsolutePath());

        // Create a Video object and populate its properties
        Video video = new Video(
                0, // ID will be generated automatically
                title,
                description,
                "/uploads/videos/" + uniqueFileName, // Store the relative path of the video
                Integer.parseInt(author), // Assuming author is a user ID
                0, // Initially, the video has 0 views
                0, // Initially, the video has 0 duration (this could be updated later if needed)
                format,
                null // You could add logic to automatically set the uploaded time if required
        );

        // Use the VideoService to save the video to the database
        boolean videoSaved = videoService.registrarVideo(video);

        if (videoSaved) {
            // Redirect to a success page or display a success message
            response.sendRedirect("/jsp/successPage.jsp"); // Redirect to success page
        } else {
            // Handle errors if the video registration fails
            request.setAttribute("error", "Error occurred while registering the video. Please try again.");
            request.getRequestDispatcher("/jsp/yourUploadPage.jsp").forward(request, response);
        }
    }
}
