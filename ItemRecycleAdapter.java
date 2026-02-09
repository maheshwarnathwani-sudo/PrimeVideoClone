package com.example.primevideoclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primevideoclone.R;
import com.example.primevideoclone.model.Movie;
import com.example.primevideoclone.util.ImageLoader;
import com.example.primevideoclone.util.RecyclerViewAnimator;

import java.util.List;

/**
 * RecyclerView adapter for displaying movie items in horizontal lists.
 */
public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemViewHolder> {
    
    private Context context;
    private List<Movie> movies;
    private OnMovieClickListener listener;
    
    public interface OnMovieClickListener {
        void onMovieClick(int movieId);
    }
    
    public ItemRecycleAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cat_recycle_row_items, parent, false);
        return new ItemViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Movie movie = movies.get(position);
        // Use posterResId if available, otherwise fallback to imageUrl
        if (movie.getPosterResId() != 0) {
            ImageLoader.loadImage(context, movie.getPosterResId(), holder.itemImage);
        } else {
            ImageLoader.loadImage(context, movie.getImageUrl(), holder.itemImage);
        }
        
        // Apply animation
        RecyclerViewAnimator.applyScaleAndFadeAnimation(
            holder.itemView,
            0.8f, 1.0f,  // Scale from 80% to 100%
            0.5f, 1.0f,  // Fade from 50% to 100%
            300  // Duration 300ms
        );
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie.getId());
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }
    
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }
}
