package com.example.myapplication.server;

import com.example.myapplication.entity.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求注册 网络请求接口
 */
public interface RegisterServer {

    @GET("/user/save")
    Call<Result> requestRegister(@Query("account") String account,@Query("password") String password);

}
