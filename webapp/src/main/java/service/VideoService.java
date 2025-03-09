package service;

import dao.VideoDAO;
import model.Video;

import java.util.List;

public class VideoService {

    private final VideoDAO videoDao = new VideoDAO();

    // Validate if a video with the same URL already exists
    public boolean validarVideo(Video video) {
        return videoDao.findById(video.getId()) != null;
    }

    // Register a new video
    public boolean registrarVideo(Video video) {
        if (videoDao.findById(video.getId()) != null) {
            return false; // Video already exists
        }
        return videoDao.save(video);
    }

    // Get all videos
    public List<Video> obtenerTodosVideos() {
        return videoDao.getAllVideos();
    }

    // Update video details
    public boolean actualizarVideo(Video video) {
        return videoDao.update(video);
    }

    // Delete a video by its ID
    public boolean eliminarVideo(int id) {
        return videoDao.delete(id);
    }
}
