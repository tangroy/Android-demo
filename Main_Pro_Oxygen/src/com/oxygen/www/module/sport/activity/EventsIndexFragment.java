package com.oxygen.www.module.sport.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.MenuActivity;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.db.DBManager;
import com.oxygen.www.enties.Banner;
import com.oxygen.www.enties.Feeds;
import com.oxygen.www.enties.GDAcvitity;
import com.oxygen.www.enties.Sport;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.challengs.activity.CreatChallengesActivity;
import com.oxygen.www.module.find.activity.PostsDetailActivity;
import com.oxygen.www.module.sport.adapter.MyActivityTimeAdapter;
import com.oxygen.www.module.sport.construct.ActivitiesConstruct;
import com.oxygen.www.module.sport.construct.SportConstruct;
import com.oxygen.www.module.team.activity.GroupDetailActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.SharedPreferencesCompat;
import com.oxygen.www.utils.ToastUtil;

public class EventsIndexFragment extends Fragment {

	public final int NET_GETACTIVITIES = 1;
	public final int LOCAL_GETACTIVITIES = 2;
	private final int NET_CREATEDACTIVITIES = 3;
	/**
	 * 获取弹出金币个数
	 */
	private static final int NET_COIN_CNT = 4;
	public PullToRefreshListView prlv_gdactivity_list;
	public ListView actualListView;

	private ImageView iv_create_event;
	private TextView message;
	private RelativeLayout rl_create_event;
	private RelativeLayout rl_create_lose;
	private ArrayList<Sport> sports = new ArrayList<Sport>();
	private ArrayList<Sport> sports_future;
	Map<String, JSONArray> user_map = new HashMap<String, JSONArray>();
	private MyActivityTimeAdapter adapter;
	private MenuActivity context;
	private ProgressBar progressBar;
	// 请求失败在请求一次
	private boolean fristrequest = true;
	private ImageView iv_message;
	private TextView tv_message_future;
	private View ll_nav_up_bg;
	private boolean isshowfuture = false;
	ArrayList<Sport> local_sports;
	/**
	 * 当前正在操作的sport
	 */
	int currentsport_location;
	DBManager dmr;
	/**
	 * 当前页
	 */
	private int page = 1;
	/**
	 * 每页获取数据数
	 */
	private int limit = 10;
	private View rootView;
	private TextView challenge_singal;
	private TextView challenge_two;
	private TextView challenge_more;
	private TextView create_event;
	private TextView create_party;
	private TextView create_other;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(R.layout.fragment_indexsport, null);
			initViews();// 控件初始化
			initViewsEvent();
			if (sports != null) {
				sports.clear();
			}
			dmr = new DBManager(context);
			page = 1;
			getSportsInNet();

			GDUtil.setGlobal(context, "timeline_is_rerfresh", false);

		}
		return rootView;
	}

	private void initViews() {
		context = (MenuActivity) getActivity();
		ll_nav_up_bg = getActivity().findViewById(R.id.ll_nav_up_bg);
		initprlv();
		iv_message = (ImageView) rootView.findViewById(R.id.iv_message);
		tv_message_future = (TextView) rootView
				.findViewById(R.id.tv_message_future);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		iv_create_event = (ImageView) rootView
				.findViewById(R.id.iv_create_event);
		message = (TextView) rootView.findViewById(R.id.message);
		rl_create_event = (RelativeLayout) rootView
				.findViewById(R.id.rl_create_event);
		rl_create_lose = (RelativeLayout) rootView
				.findViewById(R.id.rl_create_lose);

		challenge_singal = (TextView) rootView
				.findViewById(R.id.challenge_singal);
		challenge_two = (TextView) rootView.findViewById(R.id.challenge_two);
		challenge_more = (TextView) rootView.findViewById(R.id.challenge_more);
		create_event = (TextView) rootView.findViewById(R.id.create_event);
		create_party = (TextView) rootView.findViewById(R.id.create_party);
		create_other = (TextView) rootView.findViewById(R.id.create_other);
	}

	/**
	 * 初始化PullToRefreshListView
	 */
	private void initprlv() {
		prlv_gdactivity_list = (PullToRefreshListView) rootView
				.findViewById(R.id.prlv_gdactivity_list);

		prlv_gdactivity_list.setMode(Mode.BOTH);

		ILoadingLayout startLabels = prlv_gdactivity_list
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在载入...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = prlv_gdactivity_list.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

	}

	private void initViewsEvent() {
		challenge_singal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent challengeIntent = new Intent(context,
						CreatChallengesActivity.class);
				challengeIntent.putExtra("type",
						Constants.CHALLENGES_TYPE_PERSON);
				startActivity(challengeIntent);
			}
		});
		challenge_two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent challengeIntent = new Intent(context,
						CreatChallengesActivity.class);
				challengeIntent.putExtra("type",
						Constants.CHALLENGES_TYPE_TWOTEAM);
				startActivity(challengeIntent);
			}
		});
		challenge_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent challengeIntent = new Intent(context,
						CreatChallengesActivity.class);
				challengeIntent.putExtra("type",
						Constants.CHALLENGES_TYPE_MORETEAM);
				startActivity(challengeIntent);
			}
		});
		create_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent SportIntent = new Intent(context,
						ChooseSportActivity.class);
				SportIntent.putExtra("type", Constants.SPORTTYPE_CREATED);
				startActivity(SportIntent);
			}
		});
		create_party.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent partyIntent = new Intent(context,
						CreatedPlanActivity.class);
				partyIntent.putExtra("type", Constants.SPORTTYPE_CREATED);
				partyIntent.putExtra("sport", "party");
				startActivity(partyIntent);
			}
		});
		create_other.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent otherIntent = new Intent(context,
						CreatedPlanActivity.class);
				otherIntent.putExtra("type", Constants.SPORTTYPE_CREATED);
				otherIntent.putExtra("sport", "other");
				startActivity(otherIntent);
			}
		});

		iv_create_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rl_create_event.setVisibility(View.VISIBLE);
				ll_nav_up_bg.setVisibility(View.VISIBLE);
			}
		});

		iv_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				message.setVisibility(View.GONE);
				Intent i = new Intent(context, MessageActivity.class);
				startActivity(i);
			}
		});

		rl_create_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rl_create_event.setVisibility(View.GONE);
				ll_nav_up_bg.setVisibility(View.GONE);
			}
		});

		actualListView = prlv_gdactivity_list.getRefreshableView();
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				currentsport_location = arg2 - 1;
				Sport currentsport = sports.get(currentsport_location);

				// 数据来源
				String source = currentsport.getSource();
				if ("xiaomi".equals(source)) {
					// 小米手环
					// do nothing

				} else {
					// 跳转
					if (currentsport.getTarget_type().equals("Challenge")) {
						Intent intent = new Intent(context,
								ChallengesDetailActivity.class);
						intent.putExtra("challengesid",
								currentsport.getTarget_id());
						context.startActivity(intent);

					}
					// 立即开始（Target_id()==-1）的缓存数据
					else if (currentsport.getTarget_type().equals("Event")
							&& currentsport.getTarget_id() == -1) {
						// 上传数据
						addgdactivityNet(currentsport);
					} else if (currentsport.getTarget_type().equals("Event")) {
						Intent intent = new Intent(context,
								EventsResultActivity.class);
						intent.putExtra("eventid", currentsport.getTarget_id());
						context.startActivity(intent);
					}
				}

			}
		});
		prlv_gdactivity_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新

						// new FinishRefresh().execute();
						page = 1;
						adapter = null;
						if (sports != null) {
							sports.clear();

						}
						isshowfuture = false;
						GDUtil.setGlobal(context, "update_loca_data", true);
						getSportsInNet();
						new FinishRefresh().execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						getSportsInNet();
						new FinishRefresh().execute();

					}
				});

		tv_message_future.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isshowfuture = true;
				tv_message_future.setVisibility(View.GONE);
				sports.addAll(0, sports_future);
				if (adapter != null) {
					adapter.notifyDataSetChanged();
					actualListView.setSelection(0);
				} else {
					adapter = new MyActivityTimeAdapter(context, sports);
					prlv_gdactivity_list.setAdapter(adapter);
					actualListView.setSelection(0);
					prlv_gdactivity_list.setVisibility(View.VISIBLE);
				}
			}
		});

		rl_create_event.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		selectlocaldata();

	}

	/**
	 * 查找本地数据
	 */
	private void selectlocaldata() {
		try {
			if (GDUtil.getGlobal(context, "update_loca_data")) {
				GDUtil.setGlobal(context, "update_loca_data", false);
				ArrayList<GDAcvitity> gds = dmr.querysports();
				local_sports = new ArrayList<Sport>();

				if (gds != null && gds.size() > 0) {
					for (int i = 0; i < gds.size(); i++) {
						GDAcvitity avt = gds.get(i);
						if (avt.getEvent_id() == -1) {
							Sport s = new Sport();
							s.setTarget_type("Event");
							s.setStart_time(avt.getStart_time());
							s.setTarget_id(avt.getEvent_id());
							s.setSport_data(GDUtil.activitytojson(avt));
							s.setSynchronize(0);
							s.setSport(avt.getsport_eng());
							s.setTitle(avt.getTilte());
							s.setLocal(avt.getLocal());

							local_sports.add(s);
						} else {
							for (int j = 0; j < sports.size(); j++) {
								if (avt.getEvent_id() == sports.get(j)
										.getTarget_id()) {
									sports.get(j).setSynchronize(0);
								}
							}
						}
					}
					if (local_sports.size() > 0) {
						for (int i = 0; i < local_sports.size(); i++) {
							boolean isnewdata = true;
							for (int j = 0; j < sports.size(); j++) {
								if (local_sports.get(i).getLocal() == sports
										.get(j).getLocal()) {
									isnewdata = false;
								}
							}
							if (isnewdata) {
								sports.add(0, local_sports.get(i));
							}
						}
					}
					if (adapter != null) {
						adapter.notifyDataSetChanged();
						actualListView.setSelection(0);
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dmr != null)
			dmr.closeDB();
	}

	/**
	 * 提示运动记录为空
	 */
	private void listtoast() {
		if (sports != null && sports.size() > 0) {
			prlv_gdactivity_list.setVisibility(View.VISIBLE);
		} else {
			prlv_gdactivity_list.setVisibility(View.GONE);
		}
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			prlv_gdactivity_list.onRefreshComplete();
		}
	}

	/**
	 * 获取服务器activities列表
	 */
	private void getSportsInNet() {
		if (page == 1) {
			progressBar.setVisibility(View.VISIBLE);

		}
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.EVENTS_LIST_URL_GET + "?page=" + page
						+ "&limit=" + limit, handler, NET_GETACTIVITIES);

			}
		});

	}

	/**
	 * 刷新列表数据
	 */
	private void notifilist(ArrayList<Sport> list_event) {
		sports.addAll(list_event);
		GDUtil.setGlobal(context, "update_loca_data", true);
		selectlocaldata();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		} else {
			adapter = new MyActivityTimeAdapter(context, sports);
			prlv_gdactivity_list.setAdapter(adapter);
		}
		if (isshowfuture && sports_future != null && sports_future.size() > 0) {
			actualListView
					.setSelection(((page - 1) * 10 + sports_future.size()));
		} else {
			actualListView.setSelection(((page - 1) * 10));
		}

		page = page + 1;
		listtoast();
	}

	/**
	 * 上传本地数据
	 */
	private void addgdactivityNet(Sport s) {
		progressBar.setVisibility(View.VISIBLE);
		GDAcvitity performance = null;
		try {
			performance = ActivitiesConstruct.Toactivity(new JSONObject(s
					.getSport_data()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("sport", s.getSport());
		params.put("distance", performance.getDistance() + "");
		params.put("duration", performance.getDuration() + "");
		params.put("altitude", performance.getAltitude() + "");
		params.put("latitude", performance.getlatitude() + "");
		params.put("longitude", performance.getlongitude() + "");
		params.put("address", performance.getaddresss());
		params.put("start_time", performance.getStart_time());
		params.put("end_time", performance.getEnd_time());
		params.put("route", performance.getRoute() == null ? null : performance
				.getRoute().toString());
		params.put("created_at", performance.getCreated_at());
		params.put("title", performance.getTilte());
		params.put("intro", "一起运动更快乐，约么？");
		params.put("status", performance.getStatus());
		params.put("manual", "no");
		params.put("pace_min", performance.getPace_min());
		params.put("pace_max", performance.getPace_max());
		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Post(UrlConstants.ACTIVITIES_CREATE_URL_POST, handler,
						NET_CREATEDACTIVITIES, params);
			}
		});

	}

	/**
	 * 获取弹金币吐司内容
	 */
	private void getToastContent(final int user_action_id) {

		OxygenApplication.cachedThreadPool.execute(new Runnable() {

			public void run() {
				HttpUtil.Get(UrlConstants.OPERATIONS_ACTION_REWARD
						+ user_action_id + ".json", handler, NET_COIN_CNT);
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
			case NET_GETACTIVITIES:
				progressBar.setVisibility(View.GONE);
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(strObject);
						// userinfo
						if (jsonObject != null) {
							JSONObject jsonobject_userinfo_net = null;
							if (!jsonObject.isNull("users_info")) {
								jsonobject_userinfo_net = jsonObject
										.getJSONObject("users_info");
							}
							// 消息
							if (!jsonObject.isNull("notification")) {
								JSONObject notification = jsonObject
										.getJSONObject("notification");
								int messages = notification
										.getInt("unchecked_count");
								if (messages != 0) {
									message.setVisibility(View.VISIBLE);
									message.setText(messages + "");
								}
							}

							if (page == 1) {
								if (!jsonObject.isNull("future")) {
									JSONArray future_jsonarray = jsonObject
											.getJSONArray("future");
									sports_future = SportConstruct.Tosportlist(
											future_jsonarray,
											jsonobject_userinfo_net);
								}

								if (sports_future != null
										&& sports_future.size() > 0) {
									tv_message_future
											.setVisibility(View.VISIBLE);
									tv_message_future.setText("未来"
											+ sports_future.size()
											+ "个运动计划       ");
								}

								// pop_view
								if (!jsonObject.isNull("pop_view")) {
									int pop_view_id = 0;
									SharedPreferences sp = context
											.getSharedPreferences(
													Constants.USER_INFO,
													Context.MODE_MULTI_PROCESS);
									pop_view_id = sp.getInt("pop_view_id", 0);
									Banner banner = new Gson().fromJson(
											jsonObject.getJSONArray("pop_view")
													.getString(0).toString(),
											Banner.class);
									final String type = banner.getType();
									final int target_id_int = banner
											.getTarget_id();
									final String target_url = banner
											.getTarget_url();
									final String pic = banner.getPic();
									context.iv_ad
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View arg0) {
													Intent intent;
													if ("webview".equals(type)) {
														// 外链
														intent = new Intent(
																context,
																WebViewActivity.class);
														intent.putExtra(
																"target_url",
																target_url);
														intent.putExtra(
																"hasTitle",
																true);
														intent.putExtra(
																"title", "乐运动");
														startActivity(intent);

													} else if ("objectview"
															.equals(type)) {
														if ("Event"
																.equals(type)) {
															// 活动
															intent = new Intent(
																	context,
																	EventsResultActivity.class);
															intent.putExtra(
																	"eventid",
																	target_id_int);
															startActivity(intent);

														} else if ("Challenge"
																.equals(type)) {
															// 挑战
															intent = new Intent(
																	context,
																	ChallengesDetailActivity.class);
															intent.putExtra(
																	"challengesid",
																	target_id_int);
															startActivity(intent);

														} else if ("Post"
																.equals(type)) {
															// 精选
															intent = new Intent(
																	context,
																	PostsDetailActivity.class);
															intent.putExtra(
																	"posts_id",
																	target_id_int);
															intent.putExtra(
																	"picurl",
																	pic);
															startActivity(intent);

														}

													}
												}
											});
									if (pop_view_id != banner.getId()) {
										SharedPreferences.Editor editor = sp
												.edit();
										editor.putInt("pop_view_id",
												banner.getId());
										editor.putString("pop_view_pic",
												banner.getPic());
										SharedPreferencesCompat.apply(editor);
										context.rl_adbg
												.setVisibility(View.VISIBLE);
										ImageUtil.showImage2(pic,
												context.iv_ad,
												R.drawable.iv_loading);
									}

								}
								// 消息
								if (!jsonObject.isNull("feed_unread")) {
									int feed_unread = jsonObject
											.getInt("feed_unread");
									if (feed_unread != 0) {
										context.iv_message_red
												.setVisibility(View.VISIBLE);
									}
								}
							}

							ArrayList<Sport> sports_net = SportConstruct
									.Tosportlist(jsonObject,
											jsonobject_userinfo_net);

							if (sports_net != null && sports_net.size() > 0) {
								notifilist(sports_net);
							} else {
								page = page + 1;
								listtoast();
							}

							if (!jsonObject.isNull("user_action_id")) {

								int userActionId = jsonObject
										.getInt("user_action_id");
								getToastContent(userActionId);
							}

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						ToastUtil.show(context, "请求异常，请重试");
						e.printStackTrace();
					}
				} else {
					if (fristrequest) {
						getSportsInNet();
						fristrequest = false;
					} else {

					}
				}
				break;
			case NET_CREATEDACTIVITIES:
				progressBar.setVisibility(View.GONE);
				if (msg.obj == null) {
					ToastUtil.show(
							context,
							context.getResources().getString(
									R.string.errcode_wx));
				} else {
					JSONObject jsonobeObject = (JSONObject) msg.obj;
					try {
						// 上传成功删除本地纪录，更新上传本地数据状态（eventid，s）
						if (jsonobeObject.getInt("status") == 200) {
							GDAcvitity gdactivity = ActivitiesConstruct
									.Toactivity(new JSONObject(jsonobeObject
											.getString("data")));
							dmr.delsport(sports.get(currentsport_location)
									.getLocal());
							sports.get(currentsport_location).setSynchronize(1);
							sports.get(currentsport_location).setTarget_id(
									gdactivity.getEvent_id());
							adapter.notifyDataSetChanged();
						} else {
							ToastUtil.show(context, "数据上传失败，请重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;

			case NET_COIN_CNT:
				// 弹出金币提示
				if (msg.obj == null) {
					ToastUtil.show(context, "网络连接不可用，请稍后重试");
				} else {

					// Log.i("Coin", msg.obj.toString());

					try {
						JSONObject jsonobeObject = new JSONObject(
								(String) msg.obj);
						if (jsonobeObject.getInt("status") == 200) {
							JSONObject data = jsonobeObject
									.getJSONObject("data");
							String content = data.getString("alert");
							int coins = data.getInt("coins");
							ToastUtil.showCoin((Activity) context, content
									+ " +" + coins + " 金币!");

						} else {
							ToastUtil.show(context, "网络连接不可用，请稍后重试");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				break;

			default:
				break;
			}
		}

	};
}
