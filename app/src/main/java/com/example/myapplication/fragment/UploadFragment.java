package com.example.myapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.myapplication.R;
import com.example.myapplication.util.GlobalConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UploadFragment extends Fragment {

    private EditText title;
    private EditText describe;
    private ImageView imageView;
    private Button btnUpload;

    //定位相关
    private ImageView imgLocation;
    private TextView locationMsg;
    private StringBuilder currentPosition = new StringBuilder();
    private LocationClient locationClient = null;
    private MyLocationListener locationListener = new MyLocationListener();

    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;
    private Context context;

    public UploadFragment() {
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
        SDKInitializer.initialize(this.getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        initView(view);
        requestPermission(GlobalConstants.GET_LOCATION);
        return view;
    }

    //初始化
    private void initView(View view)
    {
        context = getContext();
        title = view.findViewById(R.id.edittext_title);
        describe = view.findViewById(R.id.edittext_describe);
        imageView = view.findViewById(R.id.imageView);
        btnUpload = view.findViewById(R.id.btn_upload);
        imgLocation = view.findViewById(R.id.img_location);
        locationMsg = view.findViewById(R.id.tv_showData);

        locationClient = new LocationClient(context);
        //注册监听函数
        locationClient.registerLocationListener(locationListener);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetList();
            }
        });
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MapActivity.class);
//                startActivityForResult(intent, GlobalConstants.GET_LOCATION);
                requestPermission(GlobalConstants.GET_LOCATION);
            }
        });
    }


    /**
     * 地图定位功能
     */
    //配置定位
    private void optionLocation() {
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(1500);
        option.setIsNeedLocationDescribe(true);
        locationClient.setLocOption(option);
    }

    //启动定位
    private void startPosition() {
        optionLocation();
        locationClient.start();
    }

    //自定义定位监听器,实现定位回调
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            String addr = bdLocation.getAddrStr();    //获取详细地址信息
            String country = bdLocation.getCountry();    //获取国家
            String province = bdLocation.getProvince();    //获取省份
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息
            String adcode = bdLocation.getAdCode();    //获取adcode
            String town = bdLocation.getTown();    //获取乡镇信息


            currentPosition.replace(0,currentPosition.toString().length(),addr);
            locationMsg.setText(currentPosition.toString());

        }
    }

    /**
     *  相片相关功能
     */
    //显示底部栏
    private void showBottomSheetList() {

        new BottomSheet.Builder(context,R.style.BottomSheet_StyleDialog).title("选择")
                .sheet(R.menu.bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            //点击 选择照片
                            case R.id.choose_photo:
                                //动态申请权限
                                requestPermission(GlobalConstants.CHOOSE_PHOTO);
                                break;

                            //点击 拍照
                            case R.id.take_photo:
                                //指定照片的存储位置为SD卡本目录下
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                                String timeStamp = sdf.format(new Date());
                                mTempPhotoPath =
                                        Environment.getExternalStorageDirectory() + File.separator + "photo_" + timeStamp + ".jpg";

                                File file = new File(mTempPhotoPath);
                                try {
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(context, "com.example.camerademo.fileprovider", file);
                                } else {
                                    imageUri = Uri.fromFile(file);
                                }
                                //动态申请权限
                                requestPermission(GlobalConstants.TAKE_PHOTO);
                                break;

                            //点击 取消
                            case R.id.cancel:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();

    }

    //调用相机
    private void callCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,GlobalConstants.TAKE_PHOTO);
    }

    //获取相册
    private void goPhotoAlbum() {
        //显示系统相册
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        //设置图片类型
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,GlobalConstants.CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case GlobalConstants.TAKE_PHOTO:
                if (resultCode == GlobalConstants.RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case GlobalConstants.CHOOSE_PHOTO:
                if (resultCode == GlobalConstants.RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null)
                    {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;
            case GlobalConstants.GET_LOCATION:
                if (resultCode == GlobalConstants.RESULT_OK){
                    String message = data.getStringExtra("locationMsg");
                    locationMsg.setText(message);
                }
                break;
        }
    }

    /**
     * 权限相关功能
     */
    //动态获取所需权限
    private void requestPermission(int requestCode) {

        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,2);
        }
        else {
            if (requestCode == GlobalConstants.CHOOSE_PHOTO)
                goPhotoAlbum();
            if (requestCode == GlobalConstants.TAKE_PHOTO)
                callCamera();
            if (requestCode == GlobalConstants.GET_LOCATION)
                startPosition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GlobalConstants.TAKE_PHOTO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callCamera();
            }
            else
            {
                Toast.makeText(context, "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GlobalConstants.CHOOSE_PHOTO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                goPhotoAlbum();
            }
            else
                Toast.makeText(context, "权限申请失败", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == GlobalConstants.GET_LOCATION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startPosition();
            }
            else
            {
                Toast.makeText(context, "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}