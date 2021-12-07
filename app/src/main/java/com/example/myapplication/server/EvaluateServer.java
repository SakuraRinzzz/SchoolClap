package com.example.myapplication.server;

import com.example.myapplication.entity.Evaluate;
import com.example.myapplication.entity.Result;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EvaluateServer {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/feedback/evaluate")
    Call<Result> uploadEvaluation(@Body RequestBody requestBody);
}
