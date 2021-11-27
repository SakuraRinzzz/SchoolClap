package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    public LocationClient locationClient = null;
    private MyLocationListener locationListener = new MyLocationListener();
    private StringBuilder currentPosition;
    private MapView map;
    private BaiduMap  baiduMap;
    private boolean isMyLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.baiduMapView);
        //声明LocationClient类
        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(locationListener);

        baiduMap = map.getMap();
        baiduMap.setMyLocationEnabled(true);

        requestPermission();
//        Intent intent = new Intent();
//        intent.putExtra("locationData",currentPosition.toString());
//        setResult(RESULT_OK,intent);
//        this.finish();
    }

    //动态获取权限
    private void requestPermission() {

        List<String> permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permission.isEmpty()) {
            String[] permissions = permission.toArray(new String[permission.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            startPosition();
        }

    }

    //启动定位
    private void startPosition() {
        optionLocation();
        locationClient.start();
    }

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

    public void navigateTo(BDLocation bdLocation)
    {
        //首次更新地图
        if (isMyLocation)
        {
            //获得经纬度 装进LatLng
            LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            Log.d("pipa",bdLocation.getLatitude()+"::"+ bdLocation.getLongitude());
            //设置地图更新位置
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            //地图更新
            baiduMap.animateMapStatus(update);
            //设置缩放比例
            update = MapStatusUpdateFactory.zoomTo(16f);
            //地图更新
            baiduMap.animateMapStatus(update);
            isMyLocation = false;
        }

        //构建我的位置
        MyLocationData.Builder loBuilder=new MyLocationData.Builder().latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude());
        MyLocationData locationData=loBuilder.build();
        baiduMap.setMyLocationData(locationData);
        Toast.makeText(this, "定位成功", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意权限", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    startPosition();
                } else {
                    Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        map.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }

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

            currentPosition.append(addr);
            Toast.makeText(getApplicationContext(),
                    currentPosition.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}