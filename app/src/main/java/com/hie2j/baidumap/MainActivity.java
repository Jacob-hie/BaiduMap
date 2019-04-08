package com.hie2j.baidumap;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private UiSettings mUiSettings;
    private static final boolean enabled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //空白地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        //路况颜色设置方法
        //参数含义String severeCongestion,String congestion,String slow,String smooth 分别代表严重拥堵，拥堵，缓行，畅通
//        mBaiduMap.setCustomTrafficColor(String severeCongestion,String congestion,String slow,String smooth)
//        mBaiduMap.setTrafficEnabled(true);
//        mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");
//  对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效。
//        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(13);
//        mBaiduMap.animateMapStatus(u);
        //开启热力图
//        mBaiduMap.setBaiduHeatMapEnabled(true);

        //设置logo
        mMapView.setLogoPosition(LogoPosition.logoPostionleftBottom);
//        mBaiduMap.setPadding(20, 20, 20, 20);
//        mBaiduMap.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        //其中参数paddingLeft、paddingTop、paddingRight、paddingBottom参数表示距离屏幕边框的左、上、右、下边距的距离，单位为屏幕坐标的像素密度。

        //开启地图的定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

        //打开室内图，默认为关闭状态
        mBaiduMap.setIndoorEnable(true);

        //实例化UiSettings类对象
        mUiSettings = mBaiduMap.getUiSettings();
        //通过设置enable为true或false 选择是否显示指南针
        mUiSettings.setCompassEnabled(enabled);
        //通过设置enable为true或false 选择是否显示比例尺
        mMapView.showScaleControl(enabled);
        //通过设置enable为true或false 选择是否显示缩放按钮
        mMapView.showZoomControls(enabled);
        //通过设置enable为true或false 选择是否启用地图俯视功能
        mUiSettings.setOverlookingGesturesEnabled(enabled);
        //通过设置enable为true或false 选择是否启用地图旋转功能
        mUiSettings.setRotateGesturesEnabled(enabled);
        //通过设置enable为true或false 选择是否禁用所有手势
        mUiSettings.setAllGesturesEnabled(enabled);

        //定义Maker坐标点
        final LatLng point = new LatLng(39.944251, 116.494996);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.loc);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions()
                .position(point) //必传参数
                .icon(bitmap) //必传参数
                .draggable(true)
        //设置平贴地图，在地图中双指下拉查看效果
                .flat(true)
                .alpha(0.5f);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(options);

        //地图单击事件监听接口
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MainActivity.this,
                        "单击 维度：" +latLng.latitude+
                                "  经度："+latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        //设置地图双击事件监听
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                Toast.makeText(MainActivity.this,
                        "双击 维度：" +latLng.latitude+
                                "  经度："+latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }
        });
        //设置地图长按事件监听
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MainActivity.this,
                        "长按 维度：" +latLng.latitude+
                                "  经度："+latLng.longitude,
                        Toast.LENGTH_SHORT).show();

                //绘制折线
                drawLine(latLng);
                //绘制弧线
//                drawArc(latLng);
            }
        });
    }

    private void drawLine(LatLng latLng) {
        //构建折线点坐标
        LatLng p1 = new LatLng(latLng.latitude, latLng.longitude);
        LatLng p2 = new LatLng(latLng.latitude-0.2, latLng.longitude);
        LatLng p3 = new LatLng(latLng.latitude-0.2, latLng.longitude+0.5);
        LatLng p4 = new LatLng(latLng.latitude, latLng.longitude+0.5);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p1);

        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
        //在地图上绘制折线
        //mPloyline 折线对象
        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
    }

    private void drawArc(LatLng latLng) {
        // 添加弧线坐标数据
        LatLng p1 = new LatLng(latLng.latitude, latLng.longitude);//起点
        LatLng p2 = new LatLng(latLng.latitude-0.03, latLng.longitude+0.04);//中间点
        LatLng p3 = new LatLng(latLng.latitude, latLng.longitude+0.04);//终点

        //构造ArcOptions对象
        OverlayOptions mArcOptions = new ArcOptions()
                .color(Color.RED)
                .width(10)
                .points(p1, p2, p3);
        //在地图上显示弧线
        Overlay mArc = mBaiduMap.addOverlay(mArcOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }
}


