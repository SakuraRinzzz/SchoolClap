package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.entity.News;
import com.example.myapplication.entity.Result;
import com.example.myapplication.entity.UploadHistoryFeedback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final String BASE_URL = "http://49.235.134.191:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



    }


}