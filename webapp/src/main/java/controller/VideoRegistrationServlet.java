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

/**
 *
 */
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
        // 获取提交的用户信息和其他表单字段
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            // 如果没有登录，或者session中没有user，做相应处理
            request.setAttribute("error", "用户未登录或不存在");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("titulo");
        String description = request.getParameter("descripcion");

        // 从表单中获取上传的视频
        Part videoPart = request.getPart("video");
        String fileName = videoPart.getSubmittedFileName();

        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("error", "请先选择要上传的视频文件。");
            request.getRequestDispatcher("/jsp/registroVid.jsp").forward(request, response);
            return;
        }

        // 生成唯一文件名，防止重复
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 指定保存视频的服务器路径 (此路径仅作示例，请根据你的实际部署进行修改)
        String uploadDir = "/home/alumne/ISCDM_MEI_PROJECT/webapp/uploads/videos/";
        File uploadDirFolder = new File(uploadDir);
        if (!uploadDirFolder.exists()) {
            uploadDirFolder.mkdirs(); // 若目录不存在则创建
        }

        // 准备目标文件对象
        File uploadedFile = new File(uploadDirFolder, uniqueFileName);
        String absoluteFilePath = uploadedFile.getAbsolutePath();

        // 通过流的方式将 Part 中的数据写到目标文件，而非使用 videoPart.write(...)
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

        // 获取视频时长（可选，如果不需要可省略）
        int videoDuration = getVideoDuration(absoluteFilePath);

        // 创建 Video 对象并设置相关属性
        Video video = new Video(
                0, // 数据库自增ID
                title,
                description,
                "/uploads/videos/" + uniqueFileName, // 在DB中仅保存相对路径
                user.getIdUser(),
                0, // 默认 0 次观看
                videoDuration, 
                fileExtension,
                LocalDateTime.now()
        );

        // 通过 Service 保存到数据库
        boolean videoSaved = videoService.registrarVideo(video);

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
