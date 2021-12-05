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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.fragment.CameraFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.Key;

public class MainFragment extends AppCompatActivity{

    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}