package com.example.myapplication.server;

import com.example.myapplication.entity.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HistoryServer {
    @GET("/feedback/get")
    Call<ResponseBody> getHistory(@Query("account") String account);
}
