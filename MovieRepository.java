package com.example.primevideoclone.data;

import android.content.Context;

import com.example.primevideoclone.model.Category;
import com.example.primevideoclone.model.Movie;
import com.example.primevideoclone.util.AssetScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Repository class providing static/local movie data.
 * Auto-scans assets folder for videos and generates Movie objects.
 * This is a college project with no backend - all data is local.
 */
public class MovieRepository {
    
    private static MovieRepository instance;
    private List<Movie> allMovies;
    private Map<String, List<Movie>> moviesByCategory;
    private boolean initialized = false;
    
    private MovieRepository() {
    }
    
    public static synchronized MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }
    
    /**
     * Initialize repository by scanning assets.
     * Must be called before using the repository.
     */
    public void initialize(Context context) {
        if (!initialized) {
            allMovies = AssetScanner.scanAndCreateMovies(context);
            moviesByCategory = AssetScanner.groupMoviesByCategory(allMovies);
            initialized = true;
        }
    }
    
    /**
     * Get all categories with their movies.
     * Returns auto-generated data from assets.
     */
    public List<Category> getAllCategories() {
        if (!initialized) {
            return new ArrayList<>();
        }
        
        List<Category> categories = new ArrayList<>();
        int categoryId = 1;
        
        // Map category folder names to display names
        Map<String, String> categoryDisplayNames = new java.util.HashMap<>();
        categoryDisplayNames.put("Action Movies", "Action Movies");
        categoryDisplayNames.put("Comedy", "Comedy");
        categoryDisplayNames.put("Drama", "Drama");
        categoryDisplayNames.put("Horror", "Horror");
        categoryDisplayNames.put("Romance", "Romance");
        categoryDisplayNames.put("Science_fiction", "Science Fiction");
        categoryDisplayNames.put("Thriller Movies", "Thriller Movies");
        
        for (Map.Entry<String, List<Movie>> entry : moviesByCategory.entrySet()) {
            String categoryName = categoryDisplayNames.getOrDefault(entry.getKey(), entry.getKey());
            categories.add(new Category(categoryId++, categoryName, entry.getValue()));
        }
        
        return categories;
    }
    
    /**
     * Get banner movies for the home screen carousel.
     * Returns first movie from each category, or first movie of selected category.
     */
    public List<Movie> getBannerMovies(String category) {
        if (!initialized) {
            return new ArrayList<>();
        }
        
        if ("Home".equals(category)) {
            // Return first movie from each category for home banner
            List<Movie> banners = new ArrayList<>();
            for (List<Movie> movies : moviesByCategory.values()) {
                if (!movies.isEmpty()) {
                    banners.add(movies.get(0));
                }
            }
            return banners;
        } else {
            // Return movies from selected category
            String categoryKey = mapDisplayNameToKey(category);
            List<Movie> categoryMovies = moviesByCategory.get(categoryKey);
            if (categoryMovies != null && !categoryMovies.isEmpty()) {
                // Return first 4 movies as banners
                return categoryMovies.subList(0, Math.min(4, categoryMovies.size()));
            }
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Map display name to category key.
     */
    private String mapDisplayNameToKey(String displayName) {
        Map<String, String> mapping = new java.util.HashMap<>();
        mapping.put("Tv Shows", "Drama");
        mapping.put("Movies", "Action Movies");
        mapping.put("Kids", "Comedy");
        return mapping.getOrDefault(displayName, displayName);
    }
    
    /**
     * Get a movie by its ID from all categories.
     */
    public Movie getMovieById(int id) {
        if (!initialized || allMovies == null) {
            return null;
        }
        
        for (Movie movie : allMovies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        
        return null;
    }
}
