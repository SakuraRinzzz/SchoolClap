package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.myapplication.fragment.UploadFragment;
import com.example.myapplication.util.GlobalConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.Url;

public class UploadActivity extends AppCompatActivity {

    private EditText eTitle;     //标题
    private EditText eDescribe;  //描述
    private ImageView imageView;
    private Button btnUpload;

    //定位相关
    private ImageView imgLocation;  //定位信息
    private TextView locationMsg;
    private StringBuilder currentPosition;
    private LocationClient locationClient = null;
    private MyLocationListener locationListener = new MyLocationListener();
    private boolean isMyLocation = true;

    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_upload);
        initView();
    }

    //初始化
    private void initView()
    {
        eTitle = findViewById(R.id.edittext_title);
        eDescribe = findViewById(R.id.edittext_describe);
        imageView = findViewById(R.id.imageView);
        btnUpload = findViewById(R.id.btn_upload);
        imgLocation = findViewById(R.id.img_location);
        locationMsg = findViewById(R.id.tv_showData);

        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(locationListener);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
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

    //上传
    private void Upload() {

        //获取用户填写的数据
        String title = eTitle.getText().toString();
        String desc = eDescribe.getText().toString();
        String address = locationMsg.getText().toString();

        //封装为FeedBack对象


    }


    /**
     * 地图定位功能
     */
    //配置定位
    private void optionLocation() {
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        option.setIsNeedLocationDescribe(true);
        locationClient.setLocOption(option);
    }

    //启动定位
    private void startPosition() {
        optionLocation();
        locationClient.start();
    }

    //自定义定位监听器
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            currentPosition = new StringBuilder();
            currentPosition.append(bdLocation.getAddrStr());
            String addr = bdLocation.getAddrStr();    //获取详细地址信息
            String country = bdLocation.getCountry();    //获取国家
            String province = bdLocation.getProvince();    //获取省份
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息
            String adcode = bdLocation.getAdCode();    //获取adcode
            String town = bdLocation.getTown();    //获取乡镇信息
            locationMsg.setText(currentPosition.toString());
        }
    }

    /**
     *  相片相关功能
     */
    //显示底部栏
    private void showBottomSheetList() {

        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title("选择")
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
                                imageUrl = file.getAbsolutePath();
                                Log.e("绝对路径",file.getAbsolutePath());
                                try {
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.camerademo.fileprovider", file);
                                } else {
                                    imageUri = Uri.fromFile(file);
                                    Log.e("图片的URI",imageUri.toString());
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
                        Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case GlobalConstants.CHOOSE_PHOTO:
                if (resultCode == GlobalConstants.RESULT_OK) {
                    imageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(imageUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageUrl = cursor.getString(columnIndex);
                    Log.e("11111",imageUrl);

                    if (imageUri != null)
                    {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);
            ActivityCompat.requestPermissions(this,permissions,requestCode);
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
                Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GlobalConstants.CHOOSE_PHOTO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                goPhotoAlbum();
            }
            else
                Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == GlobalConstants.GET_LOCATION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startPosition();
            }
            else
            {
                Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}