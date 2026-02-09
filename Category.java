package com.example.primevideoclone.model;

import java.util.List;

/**
 * Data model representing a category of movies/shows.
 * Each category contains a list of movies.
 */
public class Category {
    private int id;
    private String title;
    private List<Movie> movies;

    public Category() {
    }

    public Category(int id, String title, List<Movie> movies) {
        this.id = id;
        this.title = title;
        this.movies = movies;
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

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
