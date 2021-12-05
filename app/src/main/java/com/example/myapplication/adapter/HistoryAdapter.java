package com.example.myapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.FeedBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<FeedBack> historyList = new ArrayList<>();
    private Activity activity;

    public HistoryAdapter(List<FeedBack> arrayList, Activity activity) {
        this.historyList = arrayList;
        this.activity = activity;
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

//        Glide.with(activity).load(history.getImageUrl())
//                .into(holder.image);

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
            image = itemView.findViewById(R.id.img_history);
        }
    }
}
