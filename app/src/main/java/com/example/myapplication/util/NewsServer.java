package com.example.myapplication.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface NewsServer {

    @GET("news/get")
    Call<ResponseBody> getNews();

}
