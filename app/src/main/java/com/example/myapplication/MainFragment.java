package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_fragment);

        //初始化
        BottomNavigationView bottomMenu = findViewById(R.id.bnv_menu);

        NavController navController = Navigation.findNavController(this,R.id.fragmentContainerView);
        //具有左上角返回箭头的实现方式
//        AppBarConfiguration configuration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //不具有左上角返回箭头的实现方式
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomMenu.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomMenu,navController);


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupWithNavController(bottomMenu,navController);


    }

}