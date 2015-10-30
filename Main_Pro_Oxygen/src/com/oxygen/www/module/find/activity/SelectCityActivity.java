package com.oxygen.www.module.find.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.module.find.adapter.SelectCityAdapter;
import com.oxygen.www.module.find.eventbus_enties.SelectCity;

import de.greenrobot.event.EventBus;

public class SelectCityActivity extends BaseActivity implements OnClickListener {
	/**
	 * 返回键
	 */
	private ImageView selectcity_back;
	private TextView tv_title;
	private TextView tv_city;
	private LinearLayout ll_selectcity;
	private EditText et_selectcity;
	private Button bt_selectcity;
	/**
	 * 城市列表
	 */
	private ListView lv_cities;
	/**
	 * 城市字符串
	 */
	private String strCities;
	private String[] cities;
	/**
	 * 当前定位的城市
	 */
	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcity);
		city = getIntent().getStringExtra("city");
		strCities = getIntent().getStringExtra("cities");
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		selectcity_back = (ImageView) findViewById(R.id.selectcity_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_city = (TextView) findViewById(R.id.tv_city);
		ll_selectcity = (LinearLayout) findViewById(R.id.ll_selectcities);
		et_selectcity = (EditText) findViewById(R.id.et_selectcity);
		bt_selectcity = (Button) findViewById(R.id.bt_selectcity);
		lv_cities = (ListView) findViewById(R.id.lv_cities);
	}

	private void initViewsEvent() {
		selectcity_back.setOnClickListener(this);
		bt_selectcity.setOnClickListener(this);
		lv_cities.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventBus.getDefault().post(new SelectCity(cities[position]));
				SelectCityActivity.this.finish();
			}
		});
	}

	private void initValues() {
		tv_city.setText(city);
		cities = strCities.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "").split(",");
		SelectCityAdapter adapter = new SelectCityAdapter(this,cities);
		lv_cities.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectcity_back:
			this.finish();
			break;
		case R.id.bt_selectcity:
			String city = et_selectcity.getText().toString().trim();
			if(!TextUtils.isEmpty(city)){
				List<String> search_cities = new ArrayList<String>();
				for (int i = 0; i < cities.length; i++) {
					if(cities[i].contains(city)){
						search_cities.add(cities[i]);
					}
				}
				cities = (String[]) search_cities.toArray(new String[search_cities.size()]);
			}
			SelectCityAdapter adapter = new SelectCityAdapter(this,cities);
			lv_cities.setAdapter(adapter);
			break;

		default:
			break;
		}
	}
}
