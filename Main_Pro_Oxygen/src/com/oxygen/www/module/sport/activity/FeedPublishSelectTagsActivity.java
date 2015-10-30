package com.oxygen.www.module.sport.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.module.sport.adapter.FeedTagsAdapter;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.FlowLayout;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 动态-发布动态-选择标签
 * 
 * @author 张坤
 *
 */
public class FeedPublishSelectTagsActivity extends Activity implements OnClickListener {

	private List<String> tags;
	
	private ImageView iv_back;
	private ImageView iv_select;
	private TextView tv_publish;
	private TextView tv_tags;
	private TextView tv_tips;
	private TextView tv_suggest;
	private EditText et_search;
	private RelativeLayout rl_tag;
	private ListView lv_tags;
	private FeedTagsAdapter adapter;
	private List<String> list;
	
	private LayoutInflater mInflater;
	private FlowLayout fl;
	private FlowLayout fl_hot;
	/**
	 * 最大标签数量
	 */
	private static final int MAX_TAGS = 5;
	private int currentTags = 0;
	/**
	 * 搜索关键词
	 */
	private String key;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_publish_select_tags);
		
		initViews();
		initEvents();
		initValues();
		
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.FEED_HOT_TAGS +"?page=1&limit=10", handler, HOT_TAGS);
			}
		});
		
	}

	private void initViews() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		tv_suggest = (TextView) findViewById(R.id.tv_suggest);
		fl = (FlowLayout) findViewById(R.id.fl);
		fl_hot = (FlowLayout) findViewById(R.id.fl_hot);
		et_search = (EditText) findViewById(R.id.et_search);
		lv_tags = (ListView) findViewById(R.id.lv_tags);
		
		tags = new ArrayList<String>();
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
		
		fl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cnt = fl.getChildCount();
				if (cnt > 0) {
					for (int i = 0; i < cnt; i++) {
						fl.getChildAt(i).setBackgroundColor(Color.BLACK);
						((LinearLayout)fl.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
						
					}
				}
				
			}
		});
		
		
		lv_tags.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				tv_tips.setVisibility(View.GONE);
				
				if (currentTags < 5) {
					
					final LinearLayout ll_tag = (LinearLayout) mInflater.inflate(R.layout.feed_tags, fl, false);
					final ImageView iv_del = (ImageView) ll_tag.findViewById(R.id.iv_del);
					TextView tv_tag = (TextView) ll_tag.findViewById(R.id.tv_tag);
					
					tv_tag.setText(list.get(position));
					tv_tag.setTag(position);
					fl.addView(ll_tag);
					
					currentTags++;
					
					// 加入 tags 集合
					tags.add(list.get(position));
					
					ll_tag.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (iv_del.getVisibility() == View.VISIBLE) {
								// 删除按钮出现
								fl.removeView(v);
								currentTags--;
								
								// 从 tags 集合中删除
								tags.remove(((TextView)ll_tag.getChildAt(1)).getText().toString());
								
							} else {
								v.setBackgroundColor(Color.RED);
								iv_del.setVisibility(View.VISIBLE);
								
							}
							
						}
					});
					
					
				} else {
					
					ToastUtil.showShort(FeedPublishSelectTagsActivity.this, "最多只能设置 5 个标签!");
				}

			}
		});
		
		et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					// do something;
					
					tv_suggest.setVisibility(View.GONE);
					fl_hot.setVisibility(View.GONE);
					lv_tags.setVisibility(View.VISIBLE);
					

					key = et_search.getText().toString().trim();

					if (!key.isEmpty()) {
						try {
							String keyWords = URLEncoder.encode(key, "utf-8");
							search(keyWords);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					return true;
				}
				return false;
			}
		});
		
		et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tv_suggest.setVisibility(View.GONE);
				fl_hot.setVisibility(View.GONE);
				lv_tags.setVisibility(View.VISIBLE);
				

				key = et_search.getText().toString().trim();
				if (!key.isEmpty()) {
					try {
						String keyWords = URLEncoder.encode(key, "utf-8");
						search(keyWords);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					tv_suggest.setVisibility(View.VISIBLE);
					fl_hot.setVisibility(View.VISIBLE);
					lv_tags.setVisibility(View.GONE);
				}
			}
		});
		
	}

	protected void search(final String key) {
		// TODO Auto-generated method stub
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.FEED_SEARCH_TAGS + "?keyword="+key+"&page=1&limit=10", handler, SEARCH_TAGS);
			}
		});
		
	}

	private void initValues() {
		// TODO Auto-generated method stub
		list = new ArrayList<String>();
		mInflater = LayoutInflater.from(FeedPublishSelectTagsActivity.this);
		
		String tagsString = getIntent().getStringExtra("tags");
		if (tagsString != null) {
			
			tv_tips.setVisibility(View.GONE);
			
			String[] strs = GDUtil.deleteBrackets(tagsString).split(", ");
			
			for (int i = 0; i < strs.length; i++) {
				
				final LinearLayout ll_tag = (LinearLayout) mInflater.inflate(R.layout.feed_tags, fl, false);
				final ImageView iv_del = (ImageView) ll_tag.findViewById(R.id.iv_del);
				TextView tv_tag = (TextView) ll_tag.findViewById(R.id.tv_tag);
				
				tv_tag.setText(strs[i]);
				fl.addView(ll_tag);
				
				// 加入 tags 集合
				tags.add(strs[i]);

				currentTags++;
				ll_tag.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (iv_del.getVisibility() == View.VISIBLE) {
							// 删除按钮出现
							fl.removeView(v);
							currentTags--;
							
							// 从 tags 集合中删除
							tags.remove(((TextView)ll_tag.getChildAt(1)).getText().toString());
							
						} else {
							v.setBackgroundColor(Color.RED);
							iv_del.setVisibility(View.VISIBLE);
							
						}
						
					}
				});
			}
			
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		
		switch (v.getId()) {
		
		case R.id.iv_back:
			finish();
			break;
		
		case R.id.iv_select:
			// TODO...
			
			break;

		case R.id.tv_publish:
			// TODO...
			intent = new Intent();
			intent.putExtra("tags", tags.toString());
            setResult(RESULT_OK, intent);
            finish();
			
			break;

		case R.id.rl_tag:
			// TODO...
			
			break;
			
			

		default:
			break;
		}
		
	}
	
	private static final int SEARCH_TAGS = 1;
	private static final int HOT_TAGS = 2;
	
	private Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case HOT_TAGS:
				// 	TODO...
				if (msg.obj != null) {
					
					try {
						JSONObject json = new JSONObject((String)msg.obj);
//						Log.i("TAG", json.toString());

						if (json!=null && 200==json.getInt("status")) {
							
							JSONArray data = json.getJSONArray("data");
							if (data != null && data.length()>0) {
								
								List<String> list = new ArrayList<String>();
								String str;
								int len = data.length() > 3 ? 3 : data.length();
								for (int i = 0; i < len; i++) {
									str = data.getString(i);
									list.add(str);
									
									final LinearLayout ll_tag = (LinearLayout) mInflater.inflate(R.layout.feed_hot_tags, fl, false);
									final ImageView iv_del = (ImageView) ll_tag.findViewById(R.id.iv_del);
									iv_del.setTag(0); // 默认
									TextView tv_tag = (TextView) ll_tag.findViewById(R.id.tv_tag);

									tv_tag.setText(str);
									
									ll_tag.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											
											
											if (iv_del.getVisibility() == View.VISIBLE && (Integer)iv_del.getTag()==0) {
												
												if (currentTags < 5) {
													tv_tips.setVisibility(View.GONE);
													// 火苗可见
													// 上去
													fl_hot.removeView(v);
													iv_del.setImageResource(R.drawable.tag_del);
													iv_del.setTag(1); // 上面
													iv_del.setVisibility(View.GONE);
													fl.addView(v);
													currentTags++;
													
													// 加入 tags 集合
													tags.add(((TextView)ll_tag.getChildAt(1)).getText().toString());
													
												} else {
													
													ToastUtil.showShort(FeedPublishSelectTagsActivity.this, "最多只能设置 5 个标签!");
												}
												
												
											} else if (iv_del.getVisibility() == View.VISIBLE && (Integer)iv_del.getTag()==1) {
												// 删除按钮可见
												// 下来
												fl.removeView(v);
												iv_del.setImageResource(R.drawable.hot);
												iv_del.setVisibility(View.VISIBLE);
												v.setBackgroundColor(Color.BLACK);
												iv_del.setTag(0);
												fl_hot.addView(v);
												currentTags--;
												
												// 从 tags 集合中删除
												tags.remove(((TextView)ll_tag.getChildAt(1)).getText().toString());
												
											} else {
												// 可见删除按钮
												iv_del.setVisibility(View.VISIBLE);
												v.setBackgroundColor(Color.RED);
											}
											
										}
									});
									
									fl_hot.addView(ll_tag);
									
								}
								
//								Log.i("TAG", list.toString());
								
								
							}
							
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
				break;

			case SEARCH_TAGS:
				if (msg.obj != null) {
					
					try {
						JSONObject json = new JSONObject((String)msg.obj);
//						Log.i("TAG", json.toString());

						if (json!=null && 200==json.getInt("status")) {
							
							JSONArray data = json.getJSONArray("data");
							if (data != null && data.length()>0) {
								
								list.clear();
								String str;
								for (int i = 0; i < data.length(); i++) {
									str = data.getString(i);
									list.add(str);
//									Log.i("TAG", list.toString());
								}
								
								if (!list.contains(key)) {
									// 将用户搜索关键词加在第一个位置
									list.add(0, key);
								}
								
								if (adapter == null) {
									adapter = new FeedTagsAdapter(FeedPublishSelectTagsActivity.this, list);
									lv_tags.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
								
							} else {
								list.clear();
								list.add(key);
								
								if (adapter == null) {
									adapter = new FeedTagsAdapter(FeedPublishSelectTagsActivity.this, list);
									lv_tags.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
//								adapter.notifyDataSetChanged();
							}
							
						} 
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
				break;

			default:
				break;
			}
			
		};
	};


}
