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

import java.util.List;

/**
 * ViewPager2 adapter for banner movies carousel.
 */
public class BannerMoviesPagerAdapter extends RecyclerView.Adapter<BannerMoviesPagerAdapter.BannerViewHolder> {
    
    private Context context;
    private List<Movie> movies;
    private OnMovieClickListener listener;
    
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    
    public BannerMoviesPagerAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_movie_item, parent, false);
        return new BannerViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Movie movie = movies.get(position);
        // Use posterResId if available, otherwise fallback to imageUrl
        if (movie.getPosterResId() != 0) {
            ImageLoader.loadImage(context, movie.getPosterResId(), holder.bannerImage);
        } else {
            ImageLoader.loadImage(context, movie.getImageUrl(), holder.bannerImage);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }
    
    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        
        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.banner_image);
        }
    }
}
