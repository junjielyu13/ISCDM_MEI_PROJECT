package service;

import dao.VideoDAO;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import model.Video;
import model.User;

import java.util.List;
import java.util.UUID;
import util.DateTimeUtil;

public class VideoService {

    private final UserService userService = new UserService();
    private final VideoDAO videoDao = new VideoDAO();
    private static String uploadDir = "/home/alumne/ISCDM_MEI_PROJECT/webapp/src/main/webapp/uploads/videos/";

    public boolean validVideo(Video video) {
        return videoDao.findById(video.getId()) != null;
    }

    public Video findById(int id) {
        return videoDao.findById(id);
    }

    public boolean registerVideo(Video video) {
        return videoDao.save(video);
    }

    public List<Video> getAllVideo() {
        return videoDao.getAllVideos();
    }

    public boolean updateVideo(Video video) {
        return videoDao.update(video);
    }

    public boolean deleteVideo(int id) {
        return videoDao.delete(id);
    }

    public List<Video> getVideosByTitle(String title) {
        return videoDao.getVideosByTitle(title);
    }

    public List<Video> getVideosByUserId(int id) {
        return videoDao.getVideosByUserId(id);
    }

    public List<Video> getVideosByDate(String dateStr) {
        return videoDao.getVideosByDate(dateStr);
    }

    public boolean incrementViews(int id) {
        return videoDao.incrementViews(id);
    }

    public Video downloadVideo(String title, String description, Part videoPart, String fileName, User user)
            throws IOException, IllegalArgumentException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        File uploadDirFolder = new File(uploadDir);
        if (!uploadDirFolder.exists()) {
            uploadDirFolder.mkdirs();
        }

        File uploadedFile = new File(uploadDirFolder, uniqueFileName);
        String absoluteFilePath = uploadedFile.getAbsolutePath();

        try (InputStream inputStream = videoPart.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadedFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        // IOException will be thrown automatically if it occurs

        Video video = new Video(
                0,
                title,
                description,
                "/uploads/videos/" + uniqueFileName,
                user.getIdUser(),
                0,
                getMp4Duration(absoluteFilePath),
                fileExtension,
                LocalDateTime.now()
        );

        return video;
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

    public String generateTableRows(List<Video> videoList, User userActual) {
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

                if (user.getIdUser() == userActual.getIdUser()) {
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
