package com.oxygen.www.module.sport.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;

public class RunService extends Service implements AMapLocationListener {
	private LocationManagerProxy locationManager;
	private Intent runServiceIntent = new Intent(
			Constants.RECEVIER_RUN_UPDATELINK);
	private coReceiver coReceiver;
	private Double geoLat = 0.0;
	private Double geoLng = 0.0;
	private Double geoLat_old = 0.0;
	private Double geoLng_old = 0.0;
	/**
	 * 开始时间
	 */
	private String start_time;
	/**
	 * 用户统计点的个数，第四个点开始点击
	 */
	private int point = 0;
	/**
	 * 当前累计飘移点
	 */
	int count = 0;
	/**
	 * 移动路程
	 */
	private Double journey = 0.0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		tr_updatelink();
		registerReceiver();
		return START_STICKY;

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	private void registerReceiver() {
		// 动态注册广播接收器
		coReceiver = new coReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.RECEVIER_RUN_CO);
		registerReceiver(coReceiver, intentFilter);
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			// 发送广播
			OxygenApplication.location = location;
			sendBroadcast(runServiceIntent);
		}
	}

	/**
	 * 定位请求,GPS定位
	 */
	@SuppressWarnings("deprecation")
	public void tr_updatelink() {
		stopLocation();
		locationManager = LocationManagerProxy.getInstance(this);
		// locationManager.requestLocationData(
		// LocationManagerProxy.GPS_PROVIDER, 2*1000, 2, this);
		// for (final String provider : locationManager.getProviders(true)) {
		// if (LocationProviderProxy.AMapNetwork.equals(provider)) {
		locationManager.requestLocationData(LocationProviderProxy.AMapNetwork,
				3 * 1000, 3, RunService.this);
		// }
		// }
	}

	
	/**
	 * 销毁定位
	 */
	@SuppressWarnings("deprecation")
	private void stopLocation() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
			locationManager.destory();
			locationManager = null;
		}

	}

	/**
	 * 广播接收器
	 * 
	 * @author len
	 * 
	 */
	public class coReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constants.RECEVIER_RUN_CO)) {
				switch (intent.getIntExtra("action", 0)) {
				case Constants.RUN_SERVICE_CO_STOP:
					stopLocation();
					break;
				case Constants.RUN_SERVICE_CO_PLAY:
					tr_updatelink();
					break;
				case Constants.RUN_SERVICE_CO_KILL:
					stopLocation();
					stopSelf();
					if (coReceiver != null)
						unregisterReceiver(coReceiver);
					break;

				default:
					break;
				}

			}

		}

	}

	@Override
	public void onDestroy() {
		Intent localIntent = new Intent();
		localIntent.setClass(this, RunService.class); // 销毁时重新启动Service
		this.startService(localIntent);

		super.onDestroy();
	}
}
