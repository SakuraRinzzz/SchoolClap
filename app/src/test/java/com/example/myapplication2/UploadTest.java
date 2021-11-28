package com.example.myapplication2;

import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.entity.FeedBack;
import com.example.myapplication.entity.Result;
import com.example.myapplication.server.UploadServer;
import com.example.myapplication.util.SaveSharedPreferences;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.Date;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadTest {

    @Test
    public void testUpload()
    {
        String json = "{\"imageUrl\":\"http://49.235.134.191:8080/images/123.jpg\",\"title\":\"标题\",\"desc\":\"标题\",\"account\":\"root\",\"address\":\"添加按钮\",\"category\":\"安全隐患\",\"degree\":\"0\",\"time\":\"2021-11-06T13:14:25.909+00:00\",\"process\":\"已提交\"}";

        //上传
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.235.134.191:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadServer uploadServer = retrofit.create(UploadServer.class);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        Call<Result> resultCall = uploadServer.uploadInfo(requestBody);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.e("rawwwwww",response.raw().toString());
                Log.e("bodyyyyyy",response.body().toString());
                System.out.println(response.raw().toString());
                System.out.println(response.body().toString());
//                if (res == 200)
//                {
//                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
//
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

}
