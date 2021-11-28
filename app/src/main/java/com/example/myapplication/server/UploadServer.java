package com.example.myapplication.server;

import android.app.DownloadManager;

import com.example.myapplication.entity.FeedBack;
import com.example.myapplication.entity.Result;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface UploadServer {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/feedback/save")
    Call<Result> uploadInfo(@Body RequestBody body);
}
