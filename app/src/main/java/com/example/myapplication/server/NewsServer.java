package com.example.myapplication.server;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * 获取新闻列表
 */
public interface NewsServer {

    @GET("news/get")
    Call<ResponseBody> getNews();

}
