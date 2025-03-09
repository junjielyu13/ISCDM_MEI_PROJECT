package dao;

import model.Video;
import config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {

    // Find a video by its ID
    public Video findById(int id) {
        String sql = "SELECT * FROM VIDEOS WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Video(rs.getInt("id"),
                                 rs.getString("title"),
                                 rs.getString("description"),
                                 rs.getString("url"),
                                 rs.getInt("user_id"),
                                 rs.getInt("views"),
                                 rs.getInt("duration"),
                                 rs.getString("format"),
                                 rs.getTimestamp("uploaded_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no video is found
    }

    // Save a new video
    public boolean save(Video video) {
        String sql = "INSERT INTO VIDEOS (title, description, url, user_id, views, duration, format, uploaded_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getDescription());
            stmt.setString(3, video.getUrl());
            stmt.setInt(4, video.getUserId());
            stmt.setInt(5, video.getViews());
            stmt.setInt(6, video.getDuration());
            stmt.setString(7, video.getFormat());
            stmt.setTimestamp(8, Timestamp.valueOf(video.getUploadedAt()));

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if insert fails
    }

    // Get all videos
    public List<Video> getAllVideos() {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM VIDEOS";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Video video = new Video(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getInt("user_id"),
                        rs.getInt("views"),
                        rs.getInt("duration"),
                        rs.getString("format"),
                        rs.getTimestamp("uploaded_at").toLocalDateTime()
                );
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }

    // Update a video's details (e.g., title, description, etc.)
    public boolean update(Video video) {
        String sql = "UPDATE VIDEOS SET title = ?, description = ?, url = ?, user_id = ?, views = ?, duration = ?, format = ?, uploaded_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getDescription());
            stmt.setString(3, video.getUrl());
            stmt.setInt(4, video.getUserId());
            stmt.setInt(5, video.getViews());
            stmt.setInt(6, video.getDuration());
            stmt.setString(7, video.getFormat());
            stmt.setTimestamp(8, Timestamp.valueOf(video.getUploadedAt()));
            stmt.setInt(9, video.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if update fails
    }

    // Delete a video by its ID
    public boolean delete(int id) {
        String sql = "DELETE FROM VIDEOS WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if delete fails
    }
}
