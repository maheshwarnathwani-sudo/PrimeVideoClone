package com.example.primevideoclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primevideoclone.MainActivity;
import com.example.primevideoclone.R;
import com.example.primevideoclone.adapters.ItemRecycleAdapter;
import com.example.primevideoclone.model.Movie;
import com.example.primevideoclone.util.ImageLoader;
import com.example.primevideoclone.viewmodel.DetailsViewModel;

import java.util.List;

/**
 * Details screen displaying:
 * - Movie poster and title
 * - Description
 * - Play button
 * - Related movies
 */
public class DetailsFragment extends Fragment {
    
    private static final String ARG_MOVIE_ID = "movie_id";
    
    private DetailsViewModel viewModel;
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieDescription;
    private Button playButton;
    private RecyclerView relatedMoviesRecycler;
    private int movieId;
    
    public static DetailsFragment newInstance(int movieId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
        }
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(DetailsViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        moviePoster = view.findViewById(R.id.movie_poster);
        movieTitle = view.findViewById(R.id.movie_title);
        movieDescription = view.findViewById(R.id.movie_description);
        playButton = view.findViewById(R.id.play_button);
        relatedMoviesRecycler = view.findViewById(R.id.related_movies_recycler);
        
        // Setup play button
        playButton.setOnClickListener(v -> navigateToVideoPlayer(movieId));
        
        // Load movie data
        viewModel.loadMovie(movieId);
        
        // Observe ViewModel
        observeViewModel();
    }
    
    private void observeViewModel() {
        viewModel.getSelectedMovie().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                displayMovie(movie);
            }
        });
        
        viewModel.getRelatedMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                setupRelatedMovies(movies);
            }
        });
    }
    
    private void displayMovie(Movie movie) {
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
        
        // Load image using Glide - prefer posterResId over imageUrl
        if (movie.getPosterResId() != 0) {
            ImageLoader.loadImage(requireContext(), movie.getPosterResId(), moviePoster);
        } else {
            ImageLoader.loadImage(requireContext(), movie.getImageUrl(), moviePoster);
        }
    }
    
    private void setupRelatedMovies(List<Movie> movies) {
        relatedMoviesRecycler.setLayoutManager(
                new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        ItemRecycleAdapter adapter = new ItemRecycleAdapter(requireContext(), movies, movieId -> {
            // Navigate to details of related movie
            DetailsFragment fragment = DetailsFragment.newInstance(movieId);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToFragment(fragment);
            }
        });
        relatedMoviesRecycler.setAdapter(adapter);
    }
    
    private void navigateToVideoPlayer(int movieId) {
        VideoPlayerFragment fragment = VideoPlayerFragment.newInstance(movieId);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToFragment(fragment);
        }
    }
}
