package com.example.primevideoclone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.primevideoclone.MainActivity;
import com.example.primevideoclone.R;
import com.example.primevideoclone.adapters.BannerMoviesPagerAdapter;
import com.example.primevideoclone.adapters.MainRecyclerAdapter;
import com.example.primevideoclone.model.Category;
import com.example.primevideoclone.model.Movie;
import com.example.primevideoclone.viewmodel.HomeViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Home fragment displaying:
 * - Category tabs (Home, Tv Shows, Movies, Kids)
 * - Banner carousel with featured movies
 * - Category sections with horizontal movie lists
 */
public class HomeFragment extends Fragment {
    
    private HomeViewModel viewModel;
    private TabLayout categoryTab;
    private ViewPager2 bannerViewPager;
    private TabLayout indicatorTab;
    private RecyclerView mainRecycler;
    private BannerMoviesPagerAdapter bannerAdapter;
    private MainRecyclerAdapter mainAdapter;
    private Timer sliderTimer;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, 
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(HomeViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        categoryTab = view.findViewById(R.id.tabLayout);
        bannerViewPager = view.findViewById(R.id.banner_viewPager);
        indicatorTab = view.findViewById(R.id.tab_indicator);
        mainRecycler = view.findViewById(R.id.main_recycler);
        
        // Setup category tabs
        setupCategoryTabs();
        
        // Observe ViewModel data
        observeViewModel();
    }
    
    private void setupCategoryTabs() {
        String[] categories = {"Home", "Tv Shows", "Movies", "Kids"};
        for (String category : categories) {
            categoryTab.addTab(categoryTab.newTab().setText(category));
        }
        
        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();
                viewModel.selectCategory(selectedCategory);
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void observeViewModel() {
        // Observe banner movies
        viewModel.getBannerMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                setupBannerPager(movies);
            }
        });
        
        // Observe categories
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                setupMainRecycler(categories);
            }
        });
    }
    
    private void setupBannerPager(List<Movie> movies) {
        bannerAdapter = new BannerMoviesPagerAdapter(requireContext(), movies, movie -> {
            // Navigate to details
            navigateToDetails(movie.getId());
        });
        bannerViewPager.setAdapter(bannerAdapter);
        
        // Setup indicator tabs
        new TabLayoutMediator(indicatorTab, bannerViewPager, (tab, position) -> {}).attach();
        
        // Auto-scroll banner
        startAutoScroll();
    }
    
    private void startAutoScroll() {
        if (sliderTimer != null) {
            sliderTimer.cancel();
        }
        sliderTimer = new Timer();
        sliderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (bannerViewPager != null && bannerAdapter != null) {
                            int currentItem = bannerViewPager.getCurrentItem();
                            int totalItems = bannerAdapter.getItemCount();
                            if (currentItem < totalItems - 1) {
                                bannerViewPager.setCurrentItem(currentItem + 1, true);
                            } else {
                                bannerViewPager.setCurrentItem(0, true);
                            }
                        }
                    });
                }
            }
        }, 4000, 4000);
    }
    
    private void setupMainRecycler(List<Category> categories) {
        mainRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mainAdapter = new MainRecyclerAdapter(requireContext(), categories, movieId -> {
            navigateToDetails(movieId);
        });
        mainRecycler.setAdapter(mainAdapter);
        
        // Add item animator for smooth animations
        mainRecycler.setItemAnimator(new androidx.recyclerview.widget.DefaultItemAnimator() {
            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                return super.animateAdd(holder);
            }
        });
    }
    
    private void navigateToDetails(int movieId) {
        DetailsFragment fragment = DetailsFragment.newInstance(movieId);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToFragment(fragment);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sliderTimer != null) {
            sliderTimer.cancel();
            sliderTimer = null;
        }
    }
}
