package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.Evaluate;
import com.example.myapplication.entity.Result;
import com.example.myapplication.server.EvaluateServer;
import com.example.myapplication.util.GlobalConstants;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EvaluateActivity extends AppCompatActivity {

    private Button submit;
    private Evaluate evaluate;
    private RatingBar bar_result;
    private RatingBar bar_speed;
    private EditText editText;
    private int starsResult;
    private int starsSpeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess);
        initail();
        Bundle bundle=getIntent().getExtras();
        long feedBackId=bundle.getLong("feedBackId");
        bar_result.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starsResult=(int)rating;
            }
        });
        bar_speed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starsSpeed=(int)rating;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starsResult<1||starsSpeed<1){
                    Toast.makeText(EvaluateActivity.this,"星级评价未完成",Toast.LENGTH_SHORT).show();
                }
                else{
                    String comments=editText.getText().toString();
                    evaluate.setFeedBackId(feedBackId);
                    evaluate.setRatingResult(starsResult);
                    evaluate.setRatingSpeed(starsSpeed);
                    evaluate.setCommend(comments);
                    Log.i("evaluate",evaluate.toString());
                    Gson gson=new Gson();
                    String json=gson.toJson(evaluate);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(GlobalConstants.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    EvaluateServer server=retrofit.create(EvaluateServer.class);
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
                    Call<Result> call = server.uploadEvaluation(requestBody);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Result result = response.body();
                            if (result.getCode()==200){
                                Toast.makeText(EvaluateActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else if (result.getCode()==400){
                                Toast.makeText(EvaluateActivity.this,"系统错误，请重新提交",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Toast.makeText(EvaluateActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public void initail(){
        submit=findViewById(R.id.submit);
        bar_result=findViewById(R.id.ratingBar);
        bar_speed=findViewById(R.id.ratingBar2);
        editText=findViewById(R.id.comments);
        evaluate=new Evaluate();
    }
}
