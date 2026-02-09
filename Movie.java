package com.example.primevideoclone.model;

/**
 * Data model representing a movie or TV show item.
 * Supports local assets for videos and drawable resources for posters.
 */
public class Movie {
    private int id;
    private String title;
    private String imageUrl; // For backward compatibility
    private int posterResId; // Drawable resource ID for poster
    private String videoUrl; // For backward compatibility
    private String videoAssetFileName; // Asset filename like "movies/Action Movies/action_border2_video_5.mp4"
    private String description;
    private String category;

    public Movie() {
    }

    public Movie(int id, String title, String imageUrl, String videoUrl, String description, String category) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.description = description;
        this.category = category;
    }

    public Movie(int id, String title, int posterResId, String videoAssetFileName, String description, String category) {
        this.id = id;
        this.title = title;
        this.posterResId = posterResId;
        this.videoAssetFileName = videoAssetFileName;
        this.description = description;
        this.category = category;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPosterResId() {
        return posterResId;
    }

    public void setPosterResId(int posterResId) {
        this.posterResId = posterResId;
    }

    public String getVideoAssetFileName() {
        return videoAssetFileName;
    }

    public void setVideoAssetFileName(String videoAssetFileName) {
        this.videoAssetFileName = videoAssetFileName;
    }
}
