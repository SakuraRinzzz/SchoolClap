package com.example.myapplication.adapter;

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
import com.example.myapplication.entity.ProcessBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ViewHolder>{

    private List<ProcessBean> processList = new ArrayList<>();
    private Context context;
    private int processImg;
    private String processDesc;

    public ProcessAdapter(List<ProcessBean> arrayList, Context context) {
        this.processList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.process_item, parent , false);
        ProcessAdapter.ViewHolder holder = new ProcessAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProcessBean bean = processList.get(position);

        processDesc = bean.getDesc();
        switch (processDesc) {
            case "已提交":
                processImg = R.drawable.submit;
                break;
            case "等待处理":
                processImg = R.drawable.waiting;
                break;
            case "处理中":
                processImg = R.drawable.handling;
                break;
            default:
                processImg = R.drawable.finish;
                break;
        }


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        Glide.with(context).load(processImg)
                .apply(requestOptions)
                .into(holder.image);

        holder.desc.setText(processDesc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(bean.getTime());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return processList == null ? 0 : processList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView desc;
        private TextView date;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.process_desc);
            date = itemView.findViewById(R.id.process_date);
            image = itemView.findViewById(R.id.process_img);
        }
    }

}
