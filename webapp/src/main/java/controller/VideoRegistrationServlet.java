package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;
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
    private static final String UPLOAD_DIR = "/home/alumne/ISCDM_MEI_PROJECT/webapp/uploads/videos/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            forwardWithError(request, response, "User not logged in or does not exist.");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part videoPart = request.getPart("video");

        if (videoPart == null || videoPart.getSubmittedFileName().isEmpty()) {
            forwardWithError(request, response, "Please select a video file to upload.");
            return;
        }

        String filePath = saveVideoFile(videoPart);
        if (filePath == null) {
            forwardWithError(request, response, "Failed to save the video. Please try again.");
            return;
        }

        int videoDuration = getMp4Duration(filePath);
        Video video = new Video(title, description, filePath, user.getIdUser(), 0, videoDuration, getFileExtension(filePath), LocalDateTime.now());
        
        if (videoService.registerVideo(video)) {
            request.setAttribute("success", "Video uploaded and registered successfully!");
        } else {
            request.setAttribute("error", "Video registration failed. Please try again.");
        }
        request.getRequestDispatcher("/jsp/registrationVideo.jsp").forward(request, response);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/jsp/registrationVideo.jsp").forward(request, response);
    }

    private String saveVideoFile(Part videoPart) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID() + getFileExtension(videoPart.getSubmittedFileName());
            File uploadedFile = new File(uploadDir, fileName);
            try (InputStream inputStream = videoPart.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadedFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            return "/uploads/videos/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private int getMp4Duration(String videoFilePath) {
        try (RandomAccessFile file = new RandomAccessFile(videoFilePath, "r")) {
            long fileLength = file.length();
            long pos = 0;

            while (pos < fileLength - 8) {
                file.seek(pos);
                int size = file.readInt();
                byte[] typeBytes = new byte[4];
                file.read(typeBytes);
                String type = new String(typeBytes);

                if ("moov".equals(type)) {
                    long moovEnd = pos + size;
                    while (file.getFilePointer() < moovEnd - 8) {
                        int boxSize = file.readInt();
                        byte[] boxTypeBytes = new byte[4];
                        file.read(boxTypeBytes);
                        String boxType = new String(boxTypeBytes);

                        if ("mvhd".equals(boxType)) {
                            file.skipBytes(12);
                            int timescale = file.readInt();
                            int duration = file.readInt();
                            return duration / timescale;
                        }
                        file.skipBytes(boxSize - 8);
                    }
                }
                pos += size;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
