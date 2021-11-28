package com.example.myapplication.server;

import com.example.myapplication.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求登录 网络请求接口
 */

public interface LoginServer {

    @GET("/user/login")
    Call<Result> requestLogin(@Query("account") String account, @Query("password") String password);

}
