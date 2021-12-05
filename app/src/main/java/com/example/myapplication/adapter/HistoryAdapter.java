package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.entity.FeedBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<FeedBack> historyList = new ArrayList<>();
    private Context context;

    public HistoryAdapter(List<FeedBack> arrayList, Context context) {
        this.historyList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedBack history = historyList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        Glide.with(context).load(history.getImageUrl())
                .apply(requestOptions)
                .into(holder.image);


        holder.title.setText(history.getTitle());
        holder.description.setText(history.getDesc());
        holder.status.setText(history.getProcess());
        holder.type.setText(history.getCategory());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(history.getTime());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return historyList == null ? 0 : historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private TextView type;
        private TextView status;
        private TextView date;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.history_title);
            description = itemView.findViewById(R.id.history_description);
            type = itemView.findViewById(R.id.history_type);
            status = itemView.findViewById(R.id.history_status);
            date = itemView.findViewById(R.id.history_date);
            image = itemView.findViewById(R.id.history_img);
        }
    }
}
