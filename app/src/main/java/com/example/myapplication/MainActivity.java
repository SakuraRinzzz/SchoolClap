package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.entity.Result;
import com.example.myapplication.server.LoginServer;
import com.example.myapplication.server.RegisterServer;
import com.example.myapplication.util.SaveSharedPreferences;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAccount;
    private EditText etPassword;
    private TextView register_request;
    private Button login_button;

    private final String BASE_URL = "http://49.235.134.191:8080";
    final String ACCOUNT="ROOT";
    final String PASSWORD="123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_relativelayout);
        //初始化控件
        initView();
        //从本地读取保存的登录信息
        Map<String,String> userInfo = SaveSharedPreferences.getUserInfo(this);
        if (userInfo!=null)
        {
            etAccount.setText(userInfo.get("Account"));
            etPassword.setText(userInfo.get("Password"));
        }
    }

    public void initView()
    {
        etAccount = findViewById(R.id.account);
        etPassword = findViewById(R.id.password);

        register_request = findViewById(R.id.textview_register);
        register_request.setOnClickListener(this);

        login_button=findViewById(R.id.button_login);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent i;
        switch (view.getId())
        {
            case R.id.textview_register:
                i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
                Toast.makeText(MainActivity.this, "正在跳转到注册界面", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_login:
                //获取账号密码
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString();
                //判断账号密码是否非空
                if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送登录请求
                requestLogin(account,password);
                break;
        }
    }

    private void requestLogin(String account, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginServer server = retrofit.create(LoginServer.class);
        Call<Result> call = server.requestLogin(account,password);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().getCode() == 200)
                {
                    //提示登录成功
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    //跳转到主界面MainFragment
                    Intent i =new Intent(MainActivity.this,MainFragment.class);
                    startActivity(i);
                    //保存账号密码到本地
                    SaveSharedPreferences.saveUserInfo(getApplicationContext(),account,password);
                    Toast.makeText(MainActivity.this, "账号密码保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    //提示登录失败
                    Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}