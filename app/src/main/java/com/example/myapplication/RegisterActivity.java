package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.entity.Result;
import com.example.myapplication.fragment.NewsFragment;
import com.example.myapplication.server.RegisterServer;
import com.example.myapplication.util.NewsAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private final String BASE_URL = "http://49.235.134.191:8080";
    private Activity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_constraintlayout);
        activity = this;
        EditText eAccount = findViewById(R.id.register_account);
        EditText ePassword = findViewById(R.id.register_pwd);
        EditText ePassword2 = findViewById(R.id.register_pwd2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = eAccount.getText().toString();
                String password = ePassword.getText().toString();
                String _password = ePassword2.getText().toString();
                requestRegister(account,password,_password);
            }
        });

    }

    //请求注册
    private void requestRegister(String account, String password, String password1) {

        //判断账号密码是否为空
        if (password == null || password1 == null || account == null ||
                "".equals(password) || "".equals(password1) ||
                "".equals(account))
        {
            Toast.makeText(this, "账号密码不可为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断密码和重复密码是否一致
        if (!password.equals(password1))
        {
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterServer server = retrofit.create(RegisterServer.class);
        Call<Result> call = server.requestRegister(account,password);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //Log.e("33333333333",response.body().toString());
                if (response.body().getMessage().equals("SUCCESS"))
                {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_SHORT).show();
            }

        });
    }

}

