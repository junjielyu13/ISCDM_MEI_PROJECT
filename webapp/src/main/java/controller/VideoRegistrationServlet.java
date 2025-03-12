package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "User not logged in or does not exist.");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Part videoPart = request.getPart("video");
        String fileName = videoPart.getSubmittedFileName();

        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("error", "Please select a video file to upload.");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        String uploadDir = "/home/alumne/ISCDM_MEI_PROJECT/webapp/uploads/videos/";
        File uploadDirFolder = new File(uploadDir);
        if (!uploadDirFolder.exists()) {
            uploadDirFolder.mkdirs();
        }

        File uploadedFile = new File(uploadDirFolder, uniqueFileName);
        String absoluteFilePath = uploadedFile.getAbsolutePath();

        try (InputStream inputStream = videoPart.getInputStream();
             FileOutputStream fos = new FileOutputStream(uploadedFile)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            request.setAttribute("error", "Failed to save the video. Please try again.");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        int videoDuration = getMp4Duration(absoluteFilePath);

        Video video = new Video(
                0, 
                title,
                description,
                "/uploads/videos/" + uniqueFileName, 
                user.getIdUser(),
                0, 
                videoDuration, 
                fileExtension,
                LocalDateTime.now()
        );

        boolean videoSaved = videoService.registerVideo(video);

        if (videoSaved) {
            request.setAttribute("success", "Video uploaded and registered successfully!");
        } else {
            request.setAttribute("error", "Video registration failed. Please try again.");
        }
        request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
    }

    public int getMp4Duration(String videoFilePath) {
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
