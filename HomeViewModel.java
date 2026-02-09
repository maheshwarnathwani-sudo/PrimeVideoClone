package com.example.primevideoclone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.primevideoclone.data.MovieRepository;
import com.example.primevideoclone.model.Category;
import com.example.primevideoclone.model.Movie;

import java.util.List;

/**
 * ViewModel for the Home screen.
 * Follows MVVM architecture - contains only business logic, no UI code.
 * 
 * Manages:
 * - Categories and movies list
 * - Banner movies for carousel
 * - Selected category tab
 */
public class HomeViewModel extends AndroidViewModel {
    
    private MovieRepository repository;
    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<Movie>> bannerMovies;
    private MutableLiveData<String> selectedCategory;
    
    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
        categories = new MutableLiveData<>();
        bannerMovies = new MutableLiveData<>();
        selectedCategory = new MutableLiveData<>();
        
        // Initialize repository with context
        repository.initialize(application);
        
        loadData();
    }
    
    /**
     * Load all categories and initial banner movies.
     */
    private void loadData() {
        categories.setValue(repository.getAllCategories());
        bannerMovies.setValue(repository.getBannerMovies("Home"));
        selectedCategory.setValue("Home");
    }
    
    public LiveData<List<Category>> getCategories() {
        return categories;
    }
    
    public LiveData<List<Movie>> getBannerMovies() {
        return bannerMovies;
    }
    
    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }
    
    /**
     * Update selected category and load corresponding banner movies.
     * Called when user switches tabs.
     */
    public void selectCategory(String category) {
        selectedCategory.setValue(category);
        bannerMovies.setValue(repository.getBannerMovies(category));
    }
    
    /**
     * Get a movie by ID (useful for navigation).
     */
    public Movie getMovieById(int id) {
        return repository.getMovieById(id);
    }
}
