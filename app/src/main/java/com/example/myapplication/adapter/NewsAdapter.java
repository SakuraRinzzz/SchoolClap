package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.entity.News;
import com.example.myapplication.R;
import com.example.myapplication.fragment.NewsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> arrayList = new ArrayList<>();
    private Fragment fragment;

    public NewsAdapter(List<News> arrayList, NewsFragment fragment) {
        this.arrayList = arrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item2, parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = arrayList.get(position);

        Glide.with(fragment).load(news.getImageUrl())
                .into(holder.news_img);

        holder.news_content.setText(news.getDesc());
        holder.news_title.setText(news.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(news.getPublishTime());
        holder.news_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView news_content;
        private ImageView news_img;
        private TextView news_title;
        private TextView news_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            news_content = itemView.findViewById(R.id.news_content);
            news_img = itemView.findViewById(R.id.news_img);
            news_title = itemView.findViewById(R.id.news_title);
            news_date = itemView.findViewById(R.id.news_date);
        }
    }
}
