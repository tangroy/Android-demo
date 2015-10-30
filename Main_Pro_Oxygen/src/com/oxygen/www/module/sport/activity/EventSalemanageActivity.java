package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Event;
import com.oxygen.www.enties.RankUser;
import com.oxygen.www.module.sport.adapter.SaleManageAdapter;
import com.oxygen.www.module.sport.construct.EventConstruct;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.NoScrollListView;

/**
 * 战绩秀排名页
 * 
 * @author 杨庆雷 2015-6-12上午11:00:14
 */
public class EventSalemanageActivity extends BaseActivity implements
		OnClickListener {
	private ImageView iv_back;
	// private RelativeLayout rl_sale_type;
	public TextView tv_type, type_title, tv_isRank, tv_sale_invalid;
	private NoScrollListView lv_sale_valid, lv_sale_invalid;
	private Event event;
	private RelativeLayout rl_loading;
	/**
	 * 榜单排名方式
	 */
	public int run_ranking[] = { 0, 2, 3 };
	public int distance_ranking[] = { 1 };
	public int duration_ranking[] = { 5 };
	public int match_ranking[] = { 4, 5 };
	public int[] rankcatype;
	public String[] rank_chn;
	private JSONObject json_user;
	private int defrank = 0;
	private String defrank_str = "duration";

	private final int NET_GETRANKLIST = 1;
	private final int NET_ENABLE_USER_RANK = 2;
	private final int NET_GETLEADERBOARD = 3;
	private boolean isupdaterank = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salemanage);
		initViews();
		initViewsEvent();
		initValues();
	}

	private void initViews() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		// rl_sale_type = (RelativeLayout) findViewById(R.id.rl_sale_type);
		tv_type = (TextView) findViewById(R.id.tv_type);
		type_title = (TextView) findViewById(R.id.typetitle);
		tv_isRank = (TextView) findViewById(R.id.tv_isRank);
		lv_sale_valid = (NoScrollListView) findViewById(R.id.lv_sale_valid);
		lv_sale_invalid = (NoScrollListView) findViewById(R.id.lv_sale_invalid);
		tv_sale_invalid = (TextView) findViewById(R.id.tv_sale_invalid);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
	}

	private void initViewsEvent() {
		iv_back.setOnClickListener(this);
		tv_type.setOnClickListener(this);
	}

	private void initValues() {
		event = (Event) getIntent().getSerializableExtra("event");
		int category = GDUtil.SportCategory(event.getSport_eng());
		if (event.getCurrent_event_user() != null
				&& "organizer".equals(event.getCurrent_event_user().getRole())) {
			// 发起者

			getranklist();

		} else {
			// 参与者或者路人甲
			tv_isRank.setVisibility(View.GONE);
			LayoutParams params = (LayoutParams) type_title.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			params.rightMargin = (int) (30 * OxygenApplication.ppi);
			type_title.setLayoutParams(params);

			tv_sale_invalid.setVisibility(View.GONE);
			lv_sale_invalid.setVisibility(View.GONE);
			tv_type.setVisibility(View.GONE);
			leaderboard();
		}

		if (!("".equals(event.getRanking()) || "null"
				.equals(event.getRanking()))) {
			defrank_str = event.getRanking();
		}
		if (category == 8) {
			rankcatype = run_ranking;
		} else if (category == 4
				|| category == Constants.COUNT_CATEGORY_SWIMMING) {
			rankcatype = distance_ranking;
		} else if (category == 5 || category == 7) {
			rankcatype = match_ranking;
		} else {
			rankcatype = duration_ranking;
		}
		rank_chn = new String[rankcatype.length];
		for (int i = 0; i < rankcatype.length; i++) {
			rank_chn[i] = Constants.ranking_chn[rankcatype[i]];
		}
		for (int i = 0; i < rankcatype.length; i++) {
			if (defrank_str.equals(Constants.ranking[rankcatype[i]])) {
				defrank = i;
			}
		}

		type_title.setText(GDUtil.engforchn(this, defrank_str));

		lv_sale_valid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent GDAIntent = new Intent(EventSalemanageActivity.this,
						GDActivityResultActivity.class);

				GDAIntent.putExtra("activityid", is_ranks.get(position)
						.getActivity_id());
				GDAIntent.putExtra("event", event);
				GDAIntent.putExtra("sport_eng", event.getSport_eng());
				GDAIntent.putExtra("sportcategory",
						GDUtil.SportCategory(event.getSport_eng()));
				int duration = 0;
				if (event.getBpp() != null) {
					duration = event.getBpp().getDuration();
				}
				GDAIntent.putExtra("bestDuration", duration);

				startActivity(GDAIntent);

			}
		});
		lv_sale_invalid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent GDAIntent = new Intent(EventSalemanageActivity.this,
						GDActivityResultActivity.class);
				GDAIntent.putExtra("activityid", is_not_ranks.get(position)
						.getActivity_id());
				GDAIntent.putExtra("event", event);
				GDAIntent.putExtra("sport_eng", event.getSport_eng());
				GDAIntent.putExtra("sportcategory",
						GDUtil.SportCategory(event.getSport_eng()));

				int duration = 0;
				if (event.getBpp() != null) {
					duration = event.getBpp().getDuration();
				}
				GDAIntent.putExtra("bestDuration", duration);
				startActivity(GDAIntent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.iv_back:
			reback(event.get_id());
			break;
		case R.id.tv_type:
			ChooseType();
		default:
			break;
		}
	}

	/**
	 * 获取管理员排名列表
	 * 
	 * @param id
	 */
	private void getranklist() {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.EVENT_RANKLIST + event.get_id()
						+ ".json", handler, NET_GETRANKLIST);
			}
		});
	}

	/**
	 * 获取全部排名列表
	 * 
	 * @param id
	 */
	private void leaderboard() {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENT_LEADERBOARD_GET + event.get_id()
								+ ".json", handler, NET_GETLEADERBOARD);
			}
		});
	}

	/**
	 * 设定活动的排名方式
	 * 
	 * @param id
	 */
	public void set_ranking(final String type) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENT_RANK_SET_RANKING + event.getId()
								+ "/" + type + ".json", handler,
						NET_ENABLE_USER_RANK);
			}
		});
	}

	/**
	 * 允许某个user参加排名
	 * 
	 * @param id
	 */
	public void enable_user_rank(final int userid) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENT_RANK_ENABLE_USER_RANK
								+ event.getId() + "/" + userid + ".json",
						handler, NET_ENABLE_USER_RANK);
			}
		});
	}

	/**
	 * 不允许某个user参加排名
	 * 
	 * @param id
	 */
	public void disable_user_rank(final int userid) {
		rl_loading.setVisibility(View.VISIBLE);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.EVENT_RANK_DISABLE_USER_RANK
								+ event.getId() + "/" + userid + ".json",
						handler, NET_ENABLE_USER_RANK);
			}
		});
	}

	/**
	 * 选择排序方式
	 */
	private void ChooseType() {
		new AlertDialog.Builder(EventSalemanageActivity.this)
				.setTitle("请选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(rank_chn, defrank,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								defrank_str = Constants.ranking[rankcatype[which]];
								type_title
										.setText(Constants.ranking_chn[rankcatype[which]]);
								set_ranking(defrank_str);
								defrank = which;
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_GETRANKLIST:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject);
						if (jsonenlist.getInt("status") == 200) {
							UpdateRankUI(jsonenlist);
						} else {
							ToastUtil.show(EventSalemanageActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			case NET_ENABLE_USER_RANK:
				isupdaterank = true;
				String Object = (String) msg.obj;
				if (Object != null && Object.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(Object);
						if (jsonenlist.getInt("status") == 200) {
							getranklist();
						} else {
							ToastUtil.show(EventSalemanageActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;
			case NET_GETLEADERBOARD:
				String strObject_le = (String) msg.obj;
				if (strObject_le != null && strObject_le.length() > 10) {
					try {
						JSONObject jsonenlist = new JSONObject(strObject_le);

						Log.i("rankList", jsonenlist.toString());

						if (jsonenlist.getInt("status") == 200) {
							json_user = jsonenlist.getJSONObject("users_info");
							JSONArray is_rankjsonarray = jsonenlist
									.getJSONObject("data").getJSONArray(
											"ranklist");

							is_ranks = EventConstruct
									.ToRankUserlist(is_rankjsonarray);

							ArrayList<RankUser> ranks = EventConstruct
									.ToRankUserlist(is_rankjsonarray);
							if (is_rankjsonarray != null
									& is_rankjsonarray.length() > 0) {
								ViewGroup.LayoutParams params = lv_sale_valid
										.getLayoutParams();
								params.height = ranks.size()
										* ((int) (55 * OxygenApplication.ppi));
								lv_sale_valid.setLayoutParams(params);
								SaleManageAdapter adapter = new SaleManageAdapter(
										EventSalemanageActivity.this, ranks,
										json_user, defrank_str, false);
								lv_sale_valid.setAdapter(adapter);
							} else {
								lv_sale_valid.setAdapter(null);
							}
						} else {
							ToastUtil.show(EventSalemanageActivity.this,
									jsonenlist.getString("msg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;
			default:
				break;
			}

		}
	};
	private ArrayList<RankUser> is_ranks;
	private ArrayList<RankUser> is_not_ranks;

	/**
	 * 更新排名
	 * 
	 * @param jsonenlist
	 */
	private void UpdateRankUI(JSONObject jsonenlist) {
		try {
			json_user = null;

			json_user = jsonenlist.getJSONObject("users_info");
			JSONObject jsonObject = jsonenlist.getJSONObject("data");
			if (!jsonObject.isNull("is_rank")) {
				JSONArray is_rankjsonarray = jsonObject.getJSONArray("is_rank");
				is_ranks = EventConstruct.ToRankUserlist(is_rankjsonarray);
				if (is_rankjsonarray != null & is_rankjsonarray.length() > 0) {
					ViewGroup.LayoutParams params = lv_sale_valid
							.getLayoutParams();
					params.height = is_ranks.size()
							* ((int) (55 * OxygenApplication.ppi));
					lv_sale_valid.setLayoutParams(params);
					SaleManageAdapter adapter_valid = new SaleManageAdapter(
							EventSalemanageActivity.this, is_ranks, json_user,
							defrank_str, true);
					lv_sale_valid.setAdapter(adapter_valid);

				} else {
					lv_sale_valid.setAdapter(null);
				}
			}
			if (!jsonObject.isNull("is_not_rank")) {
				JSONArray is_not_rankjsonarray = jsonObject
						.getJSONArray("is_not_rank");
				is_not_ranks = EventConstruct
						.ToRankUserlist(is_not_rankjsonarray);
				if (is_not_rankjsonarray != null
						& is_not_rankjsonarray.length() > 0) {
					tv_sale_invalid.setVisibility(View.VISIBLE);
					ViewGroup.LayoutParams params = lv_sale_invalid
							.getLayoutParams();
					params.height = is_not_ranks.size()
							* ((int) (55 * OxygenApplication.ppi));
					lv_sale_invalid.setLayoutParams(params);
					SaleManageAdapter adapter_valid = new SaleManageAdapter(
							EventSalemanageActivity.this, is_not_ranks,
							json_user, defrank_str, true);
					lv_sale_invalid.setAdapter(adapter_valid);
				} else {
					lv_sale_invalid.setAdapter(null);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void reback(int eventid) {
		if (isupdaterank) {
			Intent intent = new Intent(EventSalemanageActivity.this,
					EventsResultActivity.class);
			intent.putExtra("eventid", eventid);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent eventa) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			reback(event.get_id());
			return true;
		}
		return super.onKeyDown(keyCode, eventa);
	}
}
