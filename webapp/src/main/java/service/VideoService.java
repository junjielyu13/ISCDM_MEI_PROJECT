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


public class VideoService {

    private final VideoDAO videoDao = new VideoDAO();
    private static String uploadDir = "/home/alumne/ISCDM_MEI_PROJECT/webapp/uploads/videos/";


    public boolean validVideo(Video video) {
        return videoDao.findById(video.getId()) != null;
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

        try (InputStream inputStream = videoPart.getInputStream();
             FileOutputStream fos = new FileOutputStream(uploadedFile)) {
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
}
