package com.example.primevideoclone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.primevideoclone.data.MovieRepository;
import com.example.primevideoclone.model.Category;
import com.example.primevideoclone.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Movie Details screen.
 * Follows MVVM architecture - contains only business logic, no UI code.
 * 
 * Manages:
 * - Selected movie details
 * - Related movies from the same category
 */
public class DetailsViewModel extends AndroidViewModel {
    
    private MovieRepository repository;
    private MutableLiveData<Movie> selectedMovie;
    private MutableLiveData<List<Movie>> relatedMovies;
    
    public DetailsViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
        selectedMovie = new MutableLiveData<>();
        relatedMovies = new MutableLiveData<>();
        
        // Initialize repository with context
        repository.initialize(application);
    }
    
    /**
     * Load movie details by ID.
     * Also loads related movies from the same category.
     */
    public void loadMovie(int movieId) {
        Movie movie = repository.getMovieById(movieId);
        selectedMovie.setValue(movie);
        
        // Load related movies from the same category
        if (movie != null) {
            List<Movie> related = new ArrayList<>();
            for (Category category : repository.getAllCategories()) {
                for (Movie m : category.getMovies()) {
                    if (m.getId() != movieId && m.getCategory().equals(movie.getCategory())) {
                        related.add(m);
                        if (related.size() >= 5) break;
                    }
                }
                if (related.size() >= 5) break;
            }
            relatedMovies.setValue(related);
        } else {
            relatedMovies.setValue(new ArrayList<>());
        }
    }
    
    public LiveData<Movie> getSelectedMovie() {
        return selectedMovie;
    }
    
    public LiveData<List<Movie>> getRelatedMovies() {
        return relatedMovies;
    }
}
