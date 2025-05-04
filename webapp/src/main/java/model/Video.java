package model;


import java.time.LocalDateTime;

public class Video {
    private int id;
    private String title;
    private String description;
    private String url;
    private int userId;
    private int views;
    private int duration;
    private String format;
    private LocalDateTime uploadedAt;

    // Constructor
    public Video(int id, String title, String description, String url, int userId, int views, int duration, String format, LocalDateTime uploadedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.userId = userId;
        this.views = views;
        this.duration = duration;
        this.format = format;
        this.uploadedAt = uploadedAt;
    }
    
    public Video(String title, String description, String url, int userId, int views, int duration, String format, LocalDateTime uploadedAt) {    
        this.title = title;
        this.description = description;
        this.url = url;
        this.userId = userId;
        this.views = views;
        this.duration = duration;
        this.format = format;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}
