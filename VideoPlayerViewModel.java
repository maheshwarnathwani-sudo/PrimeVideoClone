package com.example.primevideoclone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.primevideoclone.data.MovieRepository;
import com.example.primevideoclone.model.Movie;

/**
 * ViewModel for the Video Player screen.
 * Follows MVVM architecture - contains only business logic, no UI code.
 * 
 * Manages:
 * - Current playing movie
 * - Video URL for ExoPlayer
 */
public class VideoPlayerViewModel extends AndroidViewModel {
    
    private MovieRepository repository;
    private MutableLiveData<Movie> currentMovie;
    
    public VideoPlayerViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
        currentMovie = new MutableLiveData<>();
        
        // Initialize repository with context
        repository.initialize(application);
    }
    
    /**
     * Load movie for playback.
     * In a real app, this would fetch the video URL from a server.
     * For this college project, we use local/static data.
     */
    public void loadMovie(int movieId) {
        Movie movie = repository.getMovieById(movieId);
        currentMovie.setValue(movie);
    }
    
    public LiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }
}
