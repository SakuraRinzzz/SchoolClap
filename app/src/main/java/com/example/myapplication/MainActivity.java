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

import com.example.myapplication.util.SaveSharedPreferences;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAccount;
    private EditText etPassword;
    private TextView register_request;
    private Button login_button;

    final String ACCOUNT="ROOT";
    final String PASSWORD="123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_relativelayout);

        initView();

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
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (account.equals(ACCOUNT)&& password.equals(PASSWORD))
                {
                    i =new Intent(MainActivity.this,MainFragment.class);
                    startActivity(i);
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    SaveSharedPreferences.saveUserInfo(this,account,password);
                    Toast.makeText(MainActivity.this, "账号密码保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
}