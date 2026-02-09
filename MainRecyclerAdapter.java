package com.example.primevideoclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primevideoclone.R;
import com.example.primevideoclone.model.Category;
import com.example.primevideoclone.model.Movie;

import java.util.List;

/**
 * Main RecyclerView adapter for displaying categories.
 * Each category contains a horizontal RecyclerView of movies.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    
    private Context context;
    private List<Category> categories;
    private OnMovieClickListener listener;
    
    public interface OnMovieClickListener {
        void onMovieClick(int movieId);
    }
    
    public MainRecyclerAdapter(Context context, List<Category> categories, OnMovieClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false);
        return new MainViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getTitle());
        
        // Setup horizontal RecyclerView for movies in this category
        ItemRecycleAdapter itemAdapter = new ItemRecycleAdapter(context, category.getMovies(), movieId -> {
            if (listener != null) {
                listener.onMovieClick(movieId);
            }
        });
        
        holder.categoryRecycler.setLayoutManager(
                new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.categoryRecycler.setAdapter(itemAdapter);
    }
    
    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
    
    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView categoryRecycler;
        
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.item_category);
            categoryRecycler = itemView.findViewById(R.id.category_recycler);
        }
    }
}
