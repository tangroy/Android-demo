package com.oxygen.www.module.sport.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.utils.HttpUtil;

/**
 * 
 * 签到 页面
 * 
 * @author 张坤
 *
 */
public class CheckInActivity extends Activity implements OnClickListener, AMapLocationListener {

	public static final int EVENTS_CHECKIN_POST = 1;
	
	private ImageView iv_back;
	private TextView tv_check_in;
	private TextView tv_position;
	private TextView tv_error;
	private MapView mapView;
	private AMap aMap;
	private RelativeLayout rl_no_position;
	private RelativeLayout rl_loading;
	private RelativeLayout rl_error;

	/**
	 * 活动id
	 */
	private int eventid;
	/**
	 * 是否已经签到过
	 */
	private boolean isCheckIn;
	/**
	 * 活动第几个签到的人
	 */
	private int checkin;

	private LocationManagerProxy mLocationManagerProxy;
	
	/**
	 * 签到地点经纬度
	 */
	private Double latitude = 0.0;
	private Double longitude = 0.0;
	private String address;

	/**
	 * 活动地点经纬度
	 */
	private double eventLatitude;
	private double eventLongitude;
	private String eventAddress = "";
	
	/**
	 * 签到地点距离活动地点距离
	 */
	private double distance;
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case EVENTS_CHECKIN_POST:
				rl_loading.setVisibility(View.GONE);
				try {
					if (msg.obj != null && 200 == ((JSONObject)msg.obj).getInt("status")) {
					    rl_error.setVisibility(View.GONE);	
						checkin = ((JSONObject)msg.obj).getJSONObject("data").getInt("checkin");
						Log.i("checkin", "签到成功, 本活动第 "+checkin+" 个签到.");
						tv_check_in.setText("你是本活动第 "+checkin+" 个签到的人");
						// 签到成功后返回签到状态
						{
							Intent intent = new Intent();
							isCheckIn = true;
							intent.putExtra("isCheckIn", isCheckIn);
					        setResult(RESULT_OK, intent);
						}
						
//					} else if (msg.obj != null && "活动创建者已取消你的签到".equals(((JSONObject)msg.obj).getString("msg"))) {
					} else if (msg.obj != null && "event_user status is decline".equals(((JSONObject)msg.obj).getString("trace"))) {
						
						tv_error.setText("抱歉, 活动创建者已取消你的签到!");
						rl_error.setVisibility(View.VISIBLE);
						rl_error.setEnabled(false);
						
					} else {
						rl_error.setVisibility(View.VISIBLE);
//						ToastUtil.show(CheckInActivity.this, "网络异常, 请稍后重试...");
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_check_in);
		initViews();
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		initEvents();
		initValues();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		stopLocation();
	}
	
	@Override
	protected void onResume() {
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
		super.onDestroy();
		mapView.onDestroy();
	}
	
	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_check_in = (TextView) findViewById(R.id.tv_check_in);
		tv_position = (TextView) findViewById(R.id.tv_position);
		tv_error = (TextView) findViewById(R.id.tv_error);
		rl_no_position = (RelativeLayout) findViewById(R.id.rl_no_position);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		rl_error = (RelativeLayout) findViewById(R.id.rl_error);
		mapView = (MapView) findViewById(R.id.mapView);
		
	}

	private void initEvents() {
		iv_back.setOnClickListener(this);
		rl_error.setOnClickListener(this);
		
	}

	private void initValues() {
		Intent intent = getIntent();
		eventid = intent.getIntExtra("eventid", 0);
		eventLatitude = intent.getDoubleExtra("latitude", 0.0);
		eventLongitude = intent.getDoubleExtra("longitude", 0.0);
		eventAddress = intent.getStringExtra("address");
		
//		Log.i("Location", "活动地点经纬度 ["+eventLatitude+", "+eventLongitude+"]"+eventAddress);
		// 初始化位置
		initLocation();
		
	}

	/**
	 * 初始化map
	 */
	private void initmap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置位置图层
	 */
	private void setUpMap() {

		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
		
		MarkerOptions markerOption = new MarkerOptions();
		markerOption.position(new LatLng(latitude, longitude));
		markerOption.title(address).snippet("经纬度:["+latitude+", "+longitude+"]");
		markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_checkin_me));
		aMap.addMarker(markerOption);
		
		MarkerOptions markerOption2 = new MarkerOptions();
		markerOption2.position(new LatLng(eventLatitude, eventLongitude));
		markerOption2.title(eventAddress).snippet("经纬度:["+eventLatitude+", "+eventLongitude+"]");
		markerOption2.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_checkin_target));
		aMap.addMarker(markerOption2);
		
	}

	/**
	 * 初始化位置
	 */
	private void initLocation() {
		 mLocationManagerProxy = LocationManagerProxy.getInstance(this);
	    //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
	    //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
	    //在定位结束后，在合适的生命周期调用destroy()方法     
	    //其中如果间隔时间为-1，则定位只定一次
	    mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, this);
	}
	
	/**
	 * 停止定位
	 */
	private void stopLocation() {
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
//			mLocationManagerProxy.destory();
		}
		mLocationManagerProxy = null;
	}
	

	/**
	 * 请求网络签到
	 */
	private void checkIn() {
		rl_loading.setVisibility(View.VISIBLE);
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		
		params.put("latitude", latitude+"");
		params.put("longitude", longitude+"");
		params.put("address", address);
		Log.i("checkin", "["+latitude+", "+longitude+"]"+address);
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				HttpUtil.Post(UrlConstants.EVENTS_CHECKIN_POST + eventid + ".json",
						handler, EVENTS_CHECKIN_POST, params);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;

		case R.id.rl_error:
			checkIn();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// 当位置改变的时候调用 (定位成功)
		if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
			// 签到地址
			latitude = amapLocation.getLatitude();
            longitude = amapLocation.getLongitude(); 
            address = amapLocation.getAddress();
            Log.i("Location", "签到经纬度: ["+latitude + "," + longitude+"]"+ amapLocation.getAddress());
			
            // 计算 距离
            distance = getDistance(latitude, longitude, eventLatitude, eventLongitude);
            if (distance > 1000.0) {
            	tv_position.setText("距离活动地点 "+ (distance/1000) +" 公里");
			} else {
				tv_position.setText("距离活动地点 "+ (distance) +" 米");
			}
    		
            // 初始化map
            initmap();
            
        } else {
        	// 定位失败
        	Log.i("Location", "定位失败, 请检查网络及 GPS 设置并重试");
        	
        	tv_position.setVisibility(View.GONE);
        	rl_no_position.setVisibility(View.VISIBLE);
        	
		}
		
		// 请求网络签到
		checkIn();
		
	}
	

	/**
	 * 地球半径
	 */
	private static final double EARTH_RADIUS = 6378.137;
	/**
	 * 
	 * @param d
	 * @return
	 */
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为 m
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10;
	   return s;
	}
	
}
