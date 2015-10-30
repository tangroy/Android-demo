package com.oxygen.www.module.sport.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.NaviPara;
import com.oxygen.www.R;

/**
 * 活动详情页 查看活动地点 显示
 * 
 * @author 张坤
 */
public class EventAddressMapInfoActivity extends Activity implements
		OnMarkerClickListener, InfoWindowAdapter, OnClickListener {
	
	private Marker marker;
	private AMap aMap;
	private MapView mapView;
	private ImageView iv_back;
	private TextView tv_created_post;
	
	/**
	 * 活动地址
	 */
	private String address = "";
	/**
	 * 活动地点经纬度 
	 */
	private double latitude;
	private double longitude;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_event_address_map);
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		
		initValues();

		initViews();
		initEvents();
		

	}


	private void initValues() {
		// TODO Auto-generated method stub
		address = getIntent().getStringExtra("address");
		latitude = getIntent().getDoubleExtra("latitude", 0);
		longitude = getIntent().getDoubleExtra("longitude", 0);
	
		// 初始化位置
		initLocation();
		
	}


	/**
	 * 初始化位置信息
	 */
	private void initLocation() {
		// TODO Auto-generated method stub
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}


	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_created_post = (TextView) findViewById(R.id.tv_created_post);
		
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_created_post.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();

	}
	
	@Override  
	protected void onSaveInstanceState(Bundle outState) {  
	     super.onSaveInstanceState(outState);  
         mapView.onSaveInstanceState(outState);  
	}  
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	
	
	/**
	 * 设置页面监听
	 */
	private void setUpMap() {
		
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
		
		MarkerOptions markerOption = new MarkerOptions();
		markerOption.position(new LatLng(latitude, longitude));
		markerOption.title(address).snippet("["+latitude+", "+longitude+"]");
//		markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_createvent_address2));
		marker = aMap.addMarker(markerOption);
		marker.showInfoWindow();

		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		
	}


	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());

		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app);
		// 调起高德地图app
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startAMapNavi(marker);
			}
		});
		return view;
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	@SuppressWarnings("deprecation")
	public void startAMapNavi(Marker marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker.getPosition());
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

		// 调起高德地图导航
		try {
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (AMapException e) {
			// 如果没安装会进入异常，调起下载页面
			AMapUtils.getLatestAMapApp(getApplicationContext());

		}

	}

	/**
	 * 判断高德地图app是否已经安装
	 */
	public boolean getAppIn() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					"com.autonavi.minimap", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		// 本手机没有安装高德地图app
		if (packageInfo != null) {
			return true;
		}
		// 本手机成功安装有高德地图app
		else {
			return false;
		}
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}


	/**
	 * Button点击事件回调方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_created_post:
			startAMapNavi(this.marker);
			break;
		
		default:
			break;
		}
	}

}

