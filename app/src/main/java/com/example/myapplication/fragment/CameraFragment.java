package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.HistoryActivity;
import com.example.myapplication.R;
import com.example.myapplication.UploadActivity;

public class CameraFragment extends Fragment implements View.OnClickListener{

    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ImageView img_upload = view.findViewById(R.id.img_upload);
        ImageView img_history = view.findViewById(R.id.img_history);

        img_history.setOnClickListener(this);
        img_upload.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.img_upload:
                Intent intent = new Intent(getActivity(),UploadActivity.class);
                startActivity(intent);

                break;
            case R.id.img_history:
                Intent intent1=new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent1);
                break;
        }

    }
}