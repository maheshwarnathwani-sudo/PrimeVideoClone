package com.example.primevideoclone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.primevideoclone.fragments.HomeFragment;

/**
 * Main Activity for the Prime Video Clone app.
 * Uses Java + XML layouts and follows MVVM architecture.
 * 
 * Features:
 * - Java + XML layouts
 * - MVVM architecture
 * - Fragment-based navigation
 * - Glide for image loading (local drawables + online URLs)
 * - ExoPlayer for video playback
 */
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Load HomeFragment as the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
    
    /**
     * Navigate to a fragment with animation.
     */
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in,    // enter
                    android.R.anim.fade_out,   // exit
                    android.R.anim.fade_in,    // popEnter
                    android.R.anim.fade_out    // popExit
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
