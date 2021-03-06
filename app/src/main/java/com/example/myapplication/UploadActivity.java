package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.myapplication.entity.FeedBack;
import com.example.myapplication.entity.Result;
import com.example.myapplication.fragment.UploadFragment;
import com.example.myapplication.server.UploadServer;
import com.example.myapplication.util.GlobalConstants;
import com.example.myapplication.util.SaveSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class UploadActivity extends AppCompatActivity {

    private EditText eTitle;     //??????
    private EditText eDescribe;  //??????
    private ImageView imageView;
    private Button btnUpload;
    private RadioGroup typeGroup;
    private RadioGroup importanceGroup;

    //?????? feedback??????
    private String title;           //??????
    private String desc;            //??????
    private String address;         //??????
    private String category;        //????????????
    private int degree;             //????????????
    private String account;         //??????
    private String imageUrl;        //??????URL

    //????????????
    private ImageView imgLocation;  //????????????
    private TextView locationMsg;
    private StringBuilder currentPosition;
    private LocationClient locationClient = null;
    private MyLocationListener locationListener = new MyLocationListener();

    // ??????????????????????????????
    private String mTempPhotoPath;
    // ???????????????Uri??????
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_upload);
        initView();
        //??????????????????
        requestPermission(GlobalConstants.GET_LOCATION);
    }

    //?????????
    private void initView()
    {
        eTitle = findViewById(R.id.edittext_title);
        eDescribe = findViewById(R.id.edittext_describe);
        imageView = findViewById(R.id.imageView);
        btnUpload = findViewById(R.id.btn_upload);
        imgLocation = findViewById(R.id.img_location);
        locationMsg = findViewById(R.id.tv_showData);
        importanceGroup = findViewById(R.id.importanceGroup);
        typeGroup = findViewById(R.id.typeGroup);

        locationClient = new LocationClient(getApplicationContext());
        //??????????????????
        locationClient.registerLocationListener(locationListener);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
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
                requestPermission(GlobalConstants.GET_LOCATION);
            }
        });
        //???????????????radioButton?????????????????????
        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.rb_security:
                        category = "????????????";
                        break;
                    case R.id.rb_health:
                        category = "????????????";
                        break;
                    case R.id.rb_rule:
                        category = "????????????";
                        break;
                }
            }
        });
        //???????????????radioButton?????????????????????
        importanceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.rb_importent:
                        degree = 1;
                        break;
                    case R.id.rb_normal:
                        degree = 0;
                        break;
                }
            }
        });

    }

    //????????????
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Upload() {

        //???????????????????????????
        title = eTitle.getText().toString();
        desc = eDescribe.getText().toString();
        address = locationMsg.getText().toString();
        imageUrl = "http://49.235.134.191:8080/images/123.jpg";
        //????????????????????????????????????
        Map<String,String> userInfo = SaveSharedPreferences.getUserInfo(this);
        if (userInfo!=null)
            account = userInfo.get("Account");
        //?????????FeedBack??????
        FeedBack feedBack = new FeedBack();
        feedBack.setImageUrl(imageUrl);
        feedBack.setTitle(title);
        feedBack.setDesc(desc);
        feedBack.setAccount(account);
        feedBack.setAddress(address);
        feedBack.setCategory(category);
        feedBack.setDegree(degree);
        feedBack.setTime(new Date());   //2021-11-06T13:14:25.909+00:00
        feedBack.setProcess("?????????");
        //?????????
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        String json = gson.toJson(feedBack);
        Log.e("jsonnnn",json);
        //??????
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadServer uploadServer = retrofit.create(UploadServer.class);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
        Log.e("requestBodyyyyyyyy",requestBody.toString());
        Call<Result> resultCall = uploadServer.uploadInfo(requestBody);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
//                Log.e("rawwwwww",response.raw().toString());
                Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * ??????????????????
     */
    //????????????
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

    //????????????
    private void startPosition() {
        optionLocation();
        locationClient.start();
    }

    //????????????????????????
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            //?????????BDLocation?????????????????????????????????????????????get??????????????????????????????????????????
            //??????????????????????????????????????????????????????
            //??????????????????????????????????????????????????????BDLocation???????????????
            currentPosition = new StringBuilder();
            currentPosition.append(bdLocation.getAddrStr());
            String addr = bdLocation.getAddrStr();    //????????????????????????
            String country = bdLocation.getCountry();    //????????????
            String province = bdLocation.getProvince();    //????????????
            String city = bdLocation.getCity();    //????????????
            String district = bdLocation.getDistrict();    //????????????
            String street = bdLocation.getStreet();    //??????????????????
            String adcode = bdLocation.getAdCode();    //??????adcode
            String town = bdLocation.getTown();    //??????????????????
            locationMsg.setText(currentPosition.toString());
        }
    }

    /**
     *  ??????????????????
     */
    //???????????????
    private void showBottomSheetList() {

        new BottomSheet.Builder(this,R.style.BottomSheet_StyleDialog).title("??????")
                .sheet(R.menu.bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            //?????? ????????????
                            case R.id.choose_photo:
                                //??????????????????
                                requestPermission(GlobalConstants.CHOOSE_PHOTO);
                                break;

                            //?????? ??????
                            case R.id.take_photo:
                                //??????????????????????????????SD???????????????
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                                String timeStamp = sdf.format(new Date());
                                mTempPhotoPath =
                                        Environment.getExternalStorageDirectory() + File.separator + "photo_" + timeStamp + ".jpg";

                                File file = new File(mTempPhotoPath);
                                imageUrl = file.getAbsolutePath();
                                Log.e("????????????",file.getAbsolutePath());
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
                                    Log.e("?????????URI",imageUri.toString());
                                }
                                //??????????????????
                                requestPermission(GlobalConstants.TAKE_PHOTO);
                                break;

                            //?????? ??????
                            case R.id.cancel:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();

    }

    //????????????
    private void callCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,GlobalConstants.TAKE_PHOTO);
    }

    //????????????
    private void goPhotoAlbum() {
        //??????????????????
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        //??????????????????
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
     * ??????????????????
     */
    //????????????????????????
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
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GlobalConstants.CHOOSE_PHOTO)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                goPhotoAlbum();
            }
            else
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == GlobalConstants.GET_LOCATION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startPosition();
            }
            else
            {
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }
}