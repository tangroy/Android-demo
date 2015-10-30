package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;

/**
 * 活动地点 设置页面
 * 
 * @author 张坤
 *
 */
public class CreatePLanSettingAddressActivity extends BaseActivity implements OnClickListener, AMapLocationListener, OnPoiSearchListener {

	/**
	 * 定位信息
	 */
	private AMapLocation mAMapLocation;
	
	private ImageView iv_back;
	/**
	 * 搜索地址输入框
	 */
	private EditText et_adress;
	/**
	 * 搜索按钮
	 */
	private TextView tv_search;
	private TextView tv_currentAddress;
	private LocationManagerProxy mLocationManagerProxy;
	/**
	 * 搜索关键词
	 */
	private String searchKeyWords = "";
	/**
	 * 常用地址集合
	 */
	private List<String> commonList;
	/**
	 * 搜索结果集合
	 */
	private List<String> searchList;
	/**
	 * 搜索结果列表
	 */
	private ListView lv_searchResult;
	/**
	 * 常用地址列表
	 */
	private ListView lv_commonAddress;
	private RelativeLayout rl_searList;
	private RelativeLayout rl_currentPosition;
	private SharedPreferences sp;
	/**
	 * 常用地址存储字符串
	 */
	private String commonstr;
	/**
	 * 常用地址存储数组
	 */
	private String[] commons;
	
	/**
	 * 活动地址
	 */
	private String address = "";
	private double latitude;
	private double longitude;
	/**
	 * 搜索返回集合
	 */
	private List<PoiItem> list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("com.oxygen.www_", MODE_MULTI_PROCESS);
		setContentView(R.layout.activity_createplan_settingaddress);

		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_adress = (EditText) findViewById(R.id.et_adress);
		tv_search = (TextView) findViewById(R.id.tv_search);
		tv_currentAddress = (TextView) findViewById(R.id.tv_currentAddress);
		
		rl_currentPosition = (RelativeLayout) findViewById(R.id.rl_currentPosition);
		
		rl_searList = (RelativeLayout) findViewById(R.id.rl_searList);
		lv_searchResult = (ListView) findViewById(R.id.lv_searchResult);
		lv_commonAddress = (ListView) findViewById(R.id.lv_commonAddress);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		tv_currentAddress.setOnClickListener(this);
		lv_commonAddress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent();
	            address = commonList.get(position);
	            latitude = Double.parseDouble(commons[position].split("@")[1]);
	            longitude = Double.parseDouble(commons[position].split("@")[2]);
	            intent.putExtra("address", address);
	            intent.putExtra("latitude", latitude);
	            intent.putExtra("longitude", longitude);
	            setResult(RESULT_OK, intent);
	            finish();
				
			}
		});
	}

	private void initValues() {
		commonList = new ArrayList<String>();
		commonstr = sp.getString("commonstr", "");
		if (!commonstr.isEmpty()) {
			commons = commonstr.split("#");
			for (int i = 0; i < commons.length; i++) {
				commonList.add(commons[i].substring(0, commons[i].indexOf("@")));
			}
			
			CreatePlanSettingAddressAdapter adapter = new CreatePlanSettingAddressAdapter(commonList, false);
			lv_commonAddress.setAdapter(adapter);
			
		}
		
		initLocation();
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {
		
		case R.id.iv_back:
            finish();
			break;
			
		case R.id.tv_search:
			searchKeyWords = et_adress.getText().toString().trim();
			if (TextUtils.isEmpty(searchKeyWords)) {
				ToastUtil.show(CreatePLanSettingAddressActivity.this, "请输入搜索关键字!");
			}else {
				search(searchKeyWords);
				
			}
			break;
			
		case R.id.tv_currentAddress:
			if (address.equals("定位失败, 请检查网络设置并重试")) {
				// 定位失败
				address = "";
			} 
            intent.putExtra("address", address);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
//            Log.i("Location", "定位返回:"+address+":["+latitude+","+longitude+"]");
            setResult(RESULT_OK, intent);
            finish();
			break;

		default:
			break;
		}
	}
	
	private void search(String keyWords) {
		
		// 隐藏软键盘
		KeyBoardUtils.closeKeybord(et_adress, CreatePLanSettingAddressActivity.this);

		if (tv_currentAddress.getText().toString().equals("定位失败, 请检查网络设置并重试")) {
			ToastUtil.show(this, "定位失败, 请检查网络设置并重试");
		} else {
			
			// 城市码
			String cityCode = "0755";
			
			// 定位成功
			if (mAMapLocation != null) {
				cityCode = mAMapLocation.getCityCode();
				
				if (TextUtils.isEmpty(cityCode)) {
					cityCode = "0755";
				}
			}
			
			// 第一个参数表示搜索字符串，第二个参数表示POI搜索类型，二选其一
			// 第三个参数表示POI搜索区域的编码，必设
			PoiSearch.Query query = new PoiSearch.Query(keyWords,"", cityCode);
			query.setPageSize(10);  // 设置每页最多返回多少条poiitem
			query.setPageNum(0);
			PoiSearch poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	 /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //位置变化的通知时间，单位为毫秒。如果为-1，定位只定位一次。其中如果间隔时间为-1，则定位只定一次
        //位置变化通知距离，单位为米
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 10, this);
 
    }
    
    /**
     * 停止定位
     */
    private void stopLocation() {
        if (mLocationManagerProxy != null) {
        	mLocationManagerProxy.removeUpdates(this);
//        	mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
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
		// 当位置改变的时候调用
		
		if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
//			Log.i("Location", amapLocation.toString());
			
			this.mAMapLocation = amapLocation;
			
			latitude = amapLocation.getLatitude();
            longitude = amapLocation.getLongitude(); 
			address = amapLocation.getAddress();
        } else {
        	address = "定位失败, 请检查网络设置并重试";
		}
		tv_currentAddress.setText(address);
//		Log.i("Location", "定位数值:"+address+":["+latitude+","+longitude+"]");

	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// 搜索返回
		if(rCode == 0 ){

			list = result.getPois();
			
			searchList = new ArrayList<String>();
			String searchItem;
			for(int i = 0; i<list.size(); i++){
				PoiItem item = list.get(i);
				
//				Log.i("Location", "POitem"+i+":TypeDes:"+item.getTypeDes());

				searchItem = item.getTitle();
				searchItem += "#"+item.getCityName()+"-"+item.getAdName()+"-"+item.getSnippet();
				
//				Log.i("Location", "POitem"+i+":Title:"+item.getTitle());
//				Log.i("Location", "POitem"+i+":"+item.getCityName()+"-"+item.getAdName()+"-"+item.getSnippet());
//				Log.i("Location", "PoiItem "+i+": "+item.toString()+item.getLatLonPoint().getLatitude()+","+item.getLatLonPoint().getLatitude());

				searchList.add(searchItem);
			}
			
			rl_currentPosition.setVisibility(View.GONE);
			rl_searList.setVisibility(View.VISIBLE);
			lv_searchResult.setAdapter(new CreatePlanSettingAddressAdapter(searchList, true));
			lv_searchResult.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
		            Intent intent = new Intent();
		            address = searchList.get(position).substring(0, searchList.get(position).indexOf("#"));
		            latitude = list.get(position).getLatLonPoint().getLatitude();
		            longitude = list.get(position).getLatLonPoint().getLongitude();
		            
		            if (!Arrays.asList(commonstr.split("#")).contains(new String(address))) {
		            	commonstr += address+"@"+latitude+"@"+longitude+"#";
		            	sp.edit().putString("commonstr", commonstr).commit();
		            	
		            }
		            
		            intent.putExtra("latitude", latitude);
		            intent.putExtra("longitude", longitude);
					intent.putExtra("address", address);
//					Log.i("Location", "搜索返回:"+address+":["+latitude+","+longitude+"]");
		            setResult(RESULT_OK, intent);
		            finish();
				}
			});
		} else {
			Log.i("Location", "定位失败, 返回码"+rCode);
			ToastUtil.show(CreatePLanSettingAddressActivity.this, "定位失败, 请检查网络设置并重试");
		}
		
	}
	
	/**
	 * 常用地址/搜索地址列表 适配器
	 * 
	 * @author 张坤
	 *
	 */
	private class CreatePlanSettingAddressAdapter extends BaseAdapter{

		private List<String> list;
		private boolean isSearchList;
		
		public CreatePlanSettingAddressAdapter(List<String> list, boolean isSearchResult) {
			this.list = list;
			this.isSearchList = isSearchResult;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(CreatePLanSettingAddressActivity.this, R.layout.item_setting_address_activity, null);
				holder.tv_position = (TextView) convertView.findViewById(R.id.tv_address_search);
				holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_address_detail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (isSearchList) {
				// 搜索结果
				holder.tv_detail.setVisibility(View.VISIBLE);
				String[] strs = list.get(position).split("#");
				holder.tv_position.setText(strs[0]);
				holder.tv_detail.setText(strs[1]);
				
			} else {
				// 常用地址
				holder.tv_position.setText(list.get(position));
				holder.tv_detail.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	
	private static class ViewHolder{
		TextView tv_position;
		TextView tv_detail;
	}
	
}
