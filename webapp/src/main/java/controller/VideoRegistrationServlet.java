package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            request.setAttribute("error", "用户未登录或不存在");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Part videoPart = request.getPart("video");
        String fileName = videoPart.getSubmittedFileName();

        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("error", "请先选择要上传的视频文件。");
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
            request.setAttribute("error", "视频保存失败，请重试。");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }


        int videoDuration = getVideoDuration(absoluteFilePath);


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
            request.setAttribute("success", "视频上传并注册成功！");
        } else {
            request.setAttribute("error", "视频注册失败，请重试。");
        }
        request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
    }

    /**
     * 利用 ffmpeg 解析本地视频文件时长（秒）
     */
    public int getVideoDuration(String videoFilePath) {
        try {
            // 构建 ffmpeg 命令
            String cmd = "ffmpeg -i " + videoFilePath;

            // 执行命令
            Process process = Runtime.getRuntime().exec(cmd);

            // 从错误流中获取输出（ffmpeg会将处理信息输出到错误流）
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            int duration = 0;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Duration")) {
                    // 解析形如 "Duration: 00:01:23.45"
                    // 先截取 Duration 字符串所在部分，再根据冒号及 . 分割获取时、分、秒
                    String[] parts = line.split(",")[0].split(" ")[1].split(":");
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[2].split("\\.")[0]);
                    duration = hours * 3600 + minutes * 60 + seconds;
                    break;
                }
            }
            process.waitFor();
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
