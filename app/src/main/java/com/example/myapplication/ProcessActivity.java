package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.adapter.ProcessAdapter;
import com.example.myapplication.entity.FeedBack;
import com.example.myapplication.entity.ProcessBean;
import com.example.myapplication.entity.Result;
import com.example.myapplication.server.ProcessServer;
import com.example.myapplication.util.GlobalConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 查看进度
 */
public class ProcessActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ProcessBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        //获取feedBackId
        Bundle bundle = getIntent().getExtras();
        long feedBackId = bundle.getLong("feedBackId");
        //发送请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProcessServer server = retrofit.create(ProcessServer.class);
        Call<Result> call = server.getProcess(feedBackId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                list = getProcess(result);
                recyclerView = findViewById(R.id.process_list);
                LinearLayoutManager manager=new LinearLayoutManager(ProcessActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new ProcessAdapter(list,ProcessActivity.this));
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(ProcessActivity.this , "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //从result中提取processBean对象
    private List<ProcessBean> getProcess(Result result){
        Object data = result.getData();
        Gson gson=new Gson();
        Type type=new TypeToken<List<ProcessBean>>(){}.getType();
        String jsonProcess=gson.toJson(data);
        return gson.fromJson(jsonProcess, type);
    }
}