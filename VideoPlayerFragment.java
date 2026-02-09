package com.example.primevideoclone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.primevideoclone.R;
import com.example.primevideoclone.model.Movie;
import com.example.primevideoclone.viewmodel.VideoPlayerViewModel;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.ui.PlayerView;

/**
 * Video player screen using ExoPlayer.
 * Displays video playback with ExoPlayer controls.
 * 
 * Note: For this college project, we use a sample video URL.
 * In production, you would load actual video URLs from your data source.
 */
public class VideoPlayerFragment extends Fragment {
    
    private static final String ARG_MOVIE_ID = "movie_id";
    
    private VideoPlayerViewModel viewModel;
    private PlayerView playerView;
    private TextView movieTitleOverlay;
    private ExoPlayer player;
    private int movieId;
    
    public static VideoPlayerFragment newInstance(int movieId) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
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
                .get(VideoPlayerViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        playerView = view.findViewById(R.id.player_view);
        movieTitleOverlay = view.findViewById(R.id.movie_title_overlay);
        
        // Load movie data
        viewModel.loadMovie(movieId);
        
        // Observe ViewModel and initialize player when movie is loaded
        viewModel.getCurrentMovie().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                movieTitleOverlay.setText(movie.getTitle());
                // Initialize player after movie is loaded
                if (player == null) {
                    initializePlayer();
                }
            }
        });
    }
    
    private void initializePlayer() {
        player = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);
        
        // Get movie and play from assets
        Movie movie = viewModel.getCurrentMovie().getValue();
        if (movie != null && movie.getVideoAssetFileName() != null && !movie.getVideoAssetFileName().isEmpty()) {
            // Play video from assets: asset:///movies/Category/filename.mp4
            // Media3 ExoPlayer supports asset:/// URI scheme
            String assetUri = "asset:///" + movie.getVideoAssetFileName();
            MediaItem mediaItem = MediaItem.fromUri(assetUri);
            player.setMediaItem(mediaItem);
        } else {
            // Fallback to sample video if asset not found
            String videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            player.setMediaItem(mediaItem);
        }
        
        player.prepare();
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }
    
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
