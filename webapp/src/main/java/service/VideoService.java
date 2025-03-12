package service;

import dao.VideoDAO;
import model.Video;

import java.util.List;

public class VideoService {

    private final VideoDAO videoDao = new VideoDAO();

    public boolean validVideo(Video video) {
        return videoDao.findById(video.getId()) != null;
    }

    public boolean registerVideo(Video video) {
        if (videoDao.findById(video.getId()) != null) {
            return false; 
        }
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
}
