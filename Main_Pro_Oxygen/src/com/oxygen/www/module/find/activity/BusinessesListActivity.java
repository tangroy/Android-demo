package com.oxygen.www.module.find.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Businesses;
import com.oxygen.www.enties.Categories;
import com.oxygen.www.module.find.adapter.BusinessTypeAdapter;
import com.oxygen.www.module.find.adapter.BusinessesAreaadapter;
import com.oxygen.www.module.find.adapter.BusinessesListAdapter;
import com.oxygen.www.module.find.construct.BusinessConstruct;
import com.oxygen.www.module.find.eventbus_enties.BusinessName;
import com.oxygen.www.module.find.eventbus_enties.SelectCity;
import com.oxygen.www.utils.AppManager;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * 选择场馆界面
 * @author yang
 * 2015-5-22上午11:16:12
 */
public class BusinessesListActivity extends BaseActivity implements
		OnClickListener, AMapLocationListener, OnItemClickListener {
	private ImageView iv_back;
//	private LinearLayout rl_search_business;
	private LinearLayout ll_search;
//	private EditText et_search_business;
	private EditText et_adress;
//	private TextView bt_search;
	private Button bt_setadress;
	public   PullToRefreshListView prlv_businesses_list;
	public   ListView actualListView;
	private ImageView tv_businesslistisnull;
	private ArrayList<Businesses> businesses = new ArrayList<Businesses>();
	public   final int NET_GETBUSINESSES = 1;
	public     final int NET_GETBUSINESSETYPE = 2;
	private ProgressBar progressBar;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private BusinessesListAdapter adapter;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 维度
	 */
	private double latitude;
	private TextView tv_created_adress;
	private LocationManagerProxy mLocationManagerProxy;
	private String city;
	private RadioGroup rg_selection;
	private RadioButton address_type;
	private RadioButton address_nearby;
	private RadioButton address_search;
	private ListView lv_select;
	private String url;
	/**
	 * 活动场馆总数据
	 */
	private String strObject;
	/**
	 * 场馆类型集合
	 */
	private List<Categories> categories;
	/**
	 * 场馆类型ID
	 */
	private int category_id;
	/**
	 * 距离范围集合
	 */
	private List<String> areas;
	private String selectFlag;
	/**
	 * 距离范围
	 */
	private String dist;
	/**
	 * 场馆名
	 */
	private String name;
//	private FrameLayout fl_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businesseslist);
		initLocation();
		initViews();
		initViewsEvent();
		EventBus.getDefault().register(this);
	}

	private void initViews() {
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		
//		fl_search = (FrameLayout) findViewById(R.id.fl_search);
		//搜索场馆
//		rl_search_business = (LinearLayout) findViewById(R.id.rl_search_business);
//		et_search_business = (EditText) findViewById(R.id.et_search_business);
//		bt_search = (TextView) findViewById(R.id.bt_search);
		
		//手动输入地址
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		et_adress = (EditText) findViewById(R.id.et_adress);
		bt_setadress = (Button) findViewById(R.id.bt_setadress);
		
		rg_selection = (RadioGroup) findViewById(R.id.rg_selection);
		address_type = (RadioButton) findViewById(R.id.address_type);
		address_nearby = (RadioButton) findViewById(R.id.address_nearby);
		address_search = (RadioButton) findViewById(R.id.address_search);
		tv_created_adress = (TextView) findViewById(R.id.tv_created_adress);
		lv_select = (ListView) findViewById(R.id.lv_select);
		tv_created_adress.setText(city);
		prlv_businesses_list = (PullToRefreshListView) findViewById(R.id.prlv_businesses_list);
		prlv_businesses_list.setMode(Mode.PULL_FROM_END);

		ILoadingLayout endLabels = prlv_businesses_list.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		actualListView = prlv_businesses_list.getRefreshableView();
		tv_businesslistisnull = (ImageView) findViewById(R.id.tv_businesslistisnull);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		hint = getResources().getString(R.string.hand_edit_address);
		et_adress.setHint(hint);
	}

	private void initViewsEvent() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		bt_setadress.setOnClickListener(this);
		tv_created_adress.setOnClickListener(this);
		lv_select.setOnItemClickListener(this);
		address_type.setOnClickListener(this);
		address_nearby.setOnClickListener(this);
		address_search.setOnClickListener(this);
		et_adress.setOnClickListener(this);
//		
//		prlv_businesses_list.setOnScrollListener(new OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				hint = getResources().getString(R.string.hand_edit_address);
//				et_adress.setHint(hint);
//			}
//		});

		// 场馆条目的点击事件
		actualListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EventBus.getDefault().post(
						new BusinessName(businesses.get(arg2 - 1).getName()));
				BusinessesListActivity.this.finish();
			}
		});

		prlv_businesses_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						page = 1;
						getBusinessesInNet(url);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多
						getBusinessesInNet(url);
					}
				});

		actualListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// 长按事件
						return true;
					}
				});

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.et_adress:
//			hint = getResources().getString(R.string.hand_edit_address);
//			et_adress.setHint(hint);
			break;
		//手动输入地址	或者  手动输入场馆名
		case R.id.bt_setadress:
			if(getResources().getString(R.string.hand_edit_address).equals(hint)){
				String addressName = et_adress.getText().toString().trim();
				if (TextUtils.isEmpty(addressName)) {
					ToastUtil.show(this, "请输入地址");
					return;
				}
				EventBus.getDefault().post(new BusinessName(addressName));
				this.finish();
			}else if(getResources().getString(R.string.hand_edit_business).equals(hint)){
				String addressName = et_adress.getText().toString().trim();
				if (TextUtils.isEmpty(addressName)) {
					ToastUtil.show(this, "请输入场馆名");
					return;
				}
				try {
					url = UrlConstants.BUSINESSES_LIST_GET_URL 
							+ "page=" + page
							+ "&limit=" + limit 
 							+ "&meta=1&findcity=1"
							+ "&category_id=" + category_id 
							+ "&dist=" + dist
							+ "&name=" + URLEncoder.encode(addressName, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				businesses.clear();
				getBusinessesInNet(url);
			}
			break;
		//打开城市列表界面
		case R.id.tv_created_adress:
			try {
				JSONObject jsonCities = new JSONObject(strObject);
				String strCities = jsonCities.getString("citylist");
				intent = new Intent(this, SelectCityActivity.class);
				intent.putExtra("cities", strCities);
				intent.putExtra("city", city);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		//全部场馆类型
		case R.id.address_type:
			if (address_type.isSelected()) {
				lv_select.setVisibility(View.INVISIBLE);
			} else {
				categories = getCategories(strObject);
				if (categories != null && categories.size() > 0) {
					BusinessTypeAdapter typeAdapter = new BusinessTypeAdapter(
							BusinessesListActivity.this, categories);
					lv_select.setAdapter(typeAdapter);
					lv_select.setVisibility(View.VISIBLE);
					selectFlag = "categories";
				}
			}
			break;
		// 选择场馆距离范围
		case R.id.address_nearby:
			if (address_nearby.isSelected()) {
				lv_select.setVisibility(View.INVISIBLE);
			} else {
				areas = new ArrayList<String>();
				areas.add("附近场馆");
				areas.add("500米");
				areas.add("1000米");
				areas.add("2000米");
				BusinessesAreaadapter areaadapter = new BusinessesAreaadapter(
						BusinessesListActivity.this, areas);
				lv_select.setAdapter(areaadapter);
				lv_select.setVisibility(View.VISIBLE);
				selectFlag = "areas";
			}
			break;
		// 按场馆名搜索场馆
		case R.id.address_search:
			hint = getResources().getString(R.string.hand_edit_business);
			et_adress.setHint(hint);
			et_adress.requestFocus();  
			InputMethodManager imm = (InputMethodManager) et_adress.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			break;
		}
	}

	/**
	 * 场馆类型或者距离范围条目的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		page = 1;
		lv_select.setVisibility(View.INVISIBLE);
		if ("categories".equals(selectFlag)) {
			if (position > 0) {
				category_id = categories.get(position - 1).getId();
			}
			// 之前选择过距离范围搜索的时候加上距离范围
			if (!TextUtils.isEmpty(dist)) {
				url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page
						+ "&limit=" + limit + "&meta=1&findcity=1"
						+ "&category_id=" + category_id + "&dist=" + dist;
			} else {
				url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page
						+ "&limit=" + limit + "&meta=1&findcity=1"
						+ "&category_id=" + category_id;
			}
			if (position == 0) {
				address_type.setText("全部场馆");
			} else {
				address_type.setText(categories.get(position - 1).getName());
			}
		} else if ("areas".equals(selectFlag)) {
			if (position != 0) {
				dist = areas.get(position - 1).replace("米", "");
			}
			// 之前选择过场馆类型搜索的时候加上场馆类型
			if (!TextUtils.isEmpty(category_id + "")) {
				url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page
						+ "&limit=" + limit + "&meta=1&findcity=1"
						+ "&dist=" + dist + "&category_id=" + category_id;
			} else {
				url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page
						+ "&limit=" + limit  + "&meta=1&findcity=1"
						+ "&dist=" + dist;
			}
			if (position == 0) {
				address_nearby.setText("附近场馆");
			} else {
				address_nearby.setText(areas.get(position));
			}
		}
		businesses.clear();
		getBusinessesInNet(url);
	}

	/**
	 * 获取服务器不sinesses列表
	 */
	private void getBusinessesInNet(final String url) {
		progressBar.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(url, handler, NET_GETBUSINESSES);
			}
		});
	}

	/**
	 * handler 更新UI
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_GETBUSINESSES:
				progressBar.setVisibility(View.GONE);
				strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(strObject);
						if (jsonObject != null) {
							ArrayList<Businesses> businesses_net = BusinessConstruct
									.ToBusinesseslist(jsonObject);
							if (businesses_net != null
									&& businesses_net.size() > 0) {
								notifilist(businesses_net);
							} else {
								page = page + 1;
								listtoast();
							}
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case NET_GETBUSINESSETYPE:
				String type = (String) msg.obj;
				break;
			default:
				break;
			}
		}

	};
	private String hint;

	/**
	 * 刷新列表数据
	 */
	private void notifilist(ArrayList<Businesses> businesses_list) {
		businesses.addAll(businesses_list);
		adapter = new BusinessesListAdapter(BusinessesListActivity.this,
				businesses);
		prlv_businesses_list.setAdapter(adapter);
		actualListView.setSelection((page - 1) * 10);
		page = page + 1;
		listtoast();
	}

	/**
	 * 提示场馆为空
	 */
	private void listtoast() {
		if (businesses != null && businesses.size() > 0) {
			prlv_businesses_list.setVisibility(View.VISIBLE);
			tv_businesslistisnull.setVisibility(View.GONE);
		} else {
			prlv_businesses_list.setVisibility(View.GONE);
			tv_businesslistisnull.setVisibility(View.VISIBLE);
		}
	}

	private List<Categories> getCategories(String strObject) {
		if (strObject != null && strObject.length() > 10) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(strObject);
				if (jsonObject != null) {
					List<Categories> categoriess = BusinessConstruct
							.ToCategorieslist(jsonObject);
					if (categoriess != null && categoriess.size() > 0) {
						return categoriess;
					} else {
						return null;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Categories> Categoriess = new ArrayList<Categories>();

		return Categoriess;
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

	/**
	 * 初始化定位
	 */
	private void initLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
		mLocationManagerProxy.setGpsEnable(false);
	}

	// 获取经纬度
	@Override
	public void onLocationChanged(AMapLocation location) {
 		if (location != null && location.getAMapException().getErrorCode() == 0) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			city = location.getCity().replace("市", "");
			url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page + "&limit="
					+ limit + "&latitude=" + latitude + "&longitude=" + longitude
					+ "&meta=1&findcity=1";
			
		}else{
			url = UrlConstants.BUSINESSES_LIST_GET_URL + "page=" + page
					+ "&limit=" + limit  + "&meta=1&findcity=1"
					+ "&dist=" + 500;
		}
 		getBusinessesInNet(url);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destory();
		}
		mLocationManagerProxy = null;
	}

	public void onEventMainThread(SelectCity msg) {
		String cityName = msg.getName();
		tv_created_adress.setText(cityName);
		try {
			url = UrlConstants.BUSINESSES_LIST_GET_URL
					+ "page=" + page
					+ "&limit=" + limit 
					+ "&meta=1&findcity=1"
					+ "&city=" + URLDecoder.decode(cityName,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		businesses.clear();
		getBusinessesInNet(url);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
		EventBus.getDefault().unregister(BusinessesListActivity.this);
	}
}
