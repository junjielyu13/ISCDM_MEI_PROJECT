package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import model.User;
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
        User user = (User) request.getSession().getAttribute("user");
        String title = request.getParameter("titulo");
        String description = request.getParameter("descripcion");
        

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
        int videoDuration = this.getVideoDuration(uploadedFile.getAbsolutePath());


        // Create a Video object and populate its properties
        Video video = new Video(
                0, // ID will be generated automatically
                title,
                description,
                "/uploads/videos/" + uniqueFileName, // Store the relative path of the video
                user.getIdUser(), // Assuming author is a user ID
                0, // Initially, the video has 0 views
                videoDuration, // Initially, the video has 0 duration (this could be updated later if needed)
                fileExtension,
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
    
    public int getVideoDuration(String videoFilePath) {
        try {
            // Command to get video information (e.g., duration)
            String cmd = "ffmpeg -i " + videoFilePath;

            // Execute the command
            Process process = Runtime.getRuntime().exec(cmd);
            
            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            int duration = 0;
            
            // Parse the output to find the duration (in seconds)
            while ((line = reader.readLine()) != null) {
                if (line.contains("Duration")) {
                    String[] parts = line.split(",")[0].split(" ")[1].split(":");
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[2].split("\\.")[0]);
                    duration = hours * 3600 + minutes * 60 + seconds;
                    break;
                }
            }
            process.waitFor();
            return duration; // Duration in seconds
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if there is an error or no duration found
    }
}
