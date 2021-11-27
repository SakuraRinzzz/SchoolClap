package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.myapplication.fragment.CameraFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends AppCompatActivity{

    private BottomNavigationView bottomMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_fragment);

        //初始化
        bottomMenu = findViewById(R.id.bnv_menu);


        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomMenu,navController);
    }

}