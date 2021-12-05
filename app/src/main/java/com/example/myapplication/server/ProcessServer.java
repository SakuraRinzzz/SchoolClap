package com.example.myapplication.server;

import com.example.myapplication.entity.ProcessBean;
import com.example.myapplication.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProcessServer {
    @GET("/feedback/process")
    Call<Result> getProcess(@Query("feed_back_id") long feedBackId);
}
