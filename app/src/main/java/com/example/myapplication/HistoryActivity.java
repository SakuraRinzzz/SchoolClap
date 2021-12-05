package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.entity.FeedBack;
import com.example.myapplication.entity.Result;
import com.example.myapplication.server.HistoryServer;
import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.util.GlobalConstants;
import com.example.myapplication.util.SaveSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    private String account;
    private RecyclerView recyclerView;
    private List<FeedBack> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        Retrofit retrofit=new Retrofit.Builder().baseUrl(GlobalConstants.BASE_URL).build();
        HistoryServer server=retrofit.create(HistoryServer.class);
        Map<String, String> userInfo = SaveSharedPreferences.getUserInfo(this);

        account=userInfo.get("Account");

        Call<ResponseBody> call=server.getHistory(account);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson=new Gson();
                try {
                    recyclerView=findViewById(R.id.history_list);
                    Result result=gson.fromJson(response.body().string(),Result.class);
                    if (result.getCode()==200){
                        Toast.makeText(HistoryActivity.this , "连接成功", Toast.LENGTH_SHORT).show();
                        list=getHistoryInfo(result);
                        Log.i("history",list.toString());
                        LinearLayoutManager manager=new LinearLayoutManager(HistoryActivity.this);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(new HistoryAdapter(list,HistoryActivity.this));
                    }
                    else{
                        Toast.makeText(HistoryActivity.this , "连接失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HistoryActivity.this , "连接发生错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<FeedBack> getHistoryInfo(Result result){
        Object data = result.getData();
        Gson gson=new Gson();
        Type type=new TypeToken<List<FeedBack>>(){}.getType();
        String jsonHistory=gson.toJson(data);
        return gson.fromJson(jsonHistory, type);
    }
}