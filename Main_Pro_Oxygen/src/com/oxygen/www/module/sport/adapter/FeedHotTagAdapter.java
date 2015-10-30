package com.oxygen.www.module.sport.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.FeedMoment;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.enties.Vote;
import com.oxygen.www.module.sport.activity.FeedHotTagActivity;
import com.oxygen.www.module.sport.eventbus_enties.HotTag;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.FlowLayout;
import com.oxygen.www.widget.NoScrollListView2;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *  动态 - 搜索标签
 *  
 * @author 张坤
 *
 */
public class FeedHotTagAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<FeedMoment> mList;
	private LayoutInflater mInflater; 
	private Map<String, UserInfo> mUsersInfo;
	/**
	 * 当前用户信息
	 */
	private UserInfo mCurrent_User;
	/**
	 * 条目 内控件点击事件监听类
	 */
	private ItemViewOnClickListener mItemViewOnClickListener;
	/**
	 * 评论输入框
	 */
	private LinearLayout ll_comment;
	private EditText et_comment;
	private Button bt_comment;
	
	public int mPosition;
	public String mFeedId;
	
	/**
	 * 时间格式化器
	 */
	@SuppressLint("SimpleDateFormat") 
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public FeedHotTagAdapter(List<FeedMoment> list,
			Map<String, UserInfo> usersInfo,  UserInfo current_user, Context context, LinearLayout ll_comment, EditText et_comment, Button bt_comment) {

		mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(mContext);
		mUsersInfo = usersInfo;
		mCurrent_User = current_user;
		this.ll_comment = ll_comment;
		this.et_comment = et_comment;
		this.bt_comment = bt_comment;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 初始化
				ViewHolder holder = null;
				if (convertView != null && (convertView instanceof RelativeLayout)) {
					// 只复用可复用的
					holder = (ViewHolder) convertView.getTag();
				} else {
					holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.item_feed_hot_tags_list, null);
					// 初始化 view
					initView(holder, convertView);
					
					convertView.setTag(holder);
				}
				
				FeedMoment feed = mList.get(position);

				// UserInfo
				String userId = feed.getUser_id();
				
				UserInfo userInfo = mUsersInfo.get(userId);
				
				if (userInfo != null) {
					holder.iv_head.setImageResource(R.drawable.icon_def);
					ImageUtil.showImage2(userInfo.getHeadimgurl(), holder.iv_head, R.drawable.icon_def);
					holder.tv_nick.setText(userInfo.getNickname());
				} else {
					holder.iv_head.setImageResource(R.drawable.icon_def);
					holder.tv_nick.setText("匿名用户");
				}
				// 头部
				holder.tv_type.setText(feed.getTitle());
				try {
					String time = GDUtil.getTimeDiff2(sdf.parse(feed.getCreated_at()));
					holder.tv_time.setText(time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String type = feed.getTarget_type();
				Map<String, Object> feed_data = feed.getFeed_data();
				
				if ("Moment".equals(type)) {
					// 用户发表的动态
					holder.rl_moment.setVisibility(View.VISIBLE);
					holder.iv_tag.setVisibility(View.GONE);
					
//					String url = ((List<Photo>)feed_data.get("photos")).get(0).getUrl();
					/*List<Photo> photo = (List<Photo>) feed_data.get("photos");

					String url = null;
					if (photo != null && photo.size()>0) {
						url = photo.get(0).getUrl();
					}
					
					ImageUtil.showImage2(url, holder.iv_moment, R.drawable.icon_def);*/

					
					List<?> photo = (ArrayList<?>) feed_data.get("photos");
					String url = null;
					if (photo != null && photo.size()>0) {
						LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) photo.get(0);
						url = map.get("url");
						
					}
					
					
					ImageUtil.showImage2(url+Constants.qiniu_photo_feed, holder.iv_moment, R.drawable.iv_loading);
					
					holder.tv_moment.setText((String)feed_data.get("words"));
					
//					holder.fl
					List<String> tagList = (List<String>) feed_data.get("tags");
					if (tagList != null && tagList.size()>0) {
//						Log.i("tagList", tagList.toString());
//						Log.i("tagList", "id:"+feed.getTarget_id());
						
						holder.fl.removeAllViews();
						
						LayoutInflater mInflater = LayoutInflater.from(mContext);
						for (int i = 0; i < tagList.size(); i++) {
							TextView tv = (TextView) mInflater.inflate(R.layout.tv, holder.fl, false);
							tv.setText(tagList.get(i).trim());
							holder.fl.addView(tv);
							
							final String tag = tagList.get(i).trim();
							
							tv.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO... 
									
									Intent intent = new Intent(mContext, FeedHotTagActivity.class);
									intent.putExtra("tag", tag);
									mContext.startActivity(intent);
									
								}
							});
						}
						
					}
					
				}
				

				// 赞
				List<Vote> votes = feed.getVotes();
				List<String> voteNicks = new ArrayList<String>();
				if (votes != null && votes.size() > 0) {
					
					for (Vote feedVote : votes) {
		 				String nick = mUsersInfo.get(""+feedVote.getCreated_by()).getNickname();
		 				voteNicks.add(nick);
					}
					
					if ("yes".equals(feed.getCurr_user_voted())) {
						holder.iv_vote.setImageResource(R.drawable.feed_thumb_selected);
					} else {
						holder.iv_vote.setImageResource(R.drawable.feed_thumb_normal);
					}
					holder.rl_votes.setVisibility(View.VISIBLE);
					holder.tv_vote_names.setText(GDUtil.deleteBrackets(voteNicks.toString()));
				} else {
					// 无人点赞
					holder.iv_vote.setImageResource(R.drawable.feed_thumb_normal);
					holder.rl_votes.setVisibility(View.GONE);
					
				}

				
				// 评论
				List<Comment> comments = feed.getComments();
				if (comments != null && comments.size() > 0) {
					final FeedCommentAdapter commentAdapter = new FeedCommentAdapter(comments, mContext, mCurrent_User, mUsersInfo);
					holder.lv_comments.setAdapter(commentAdapter);
					
					holder.view_divider.setVisibility(View.VISIBLE);
					holder.lv_comments.setVisibility(View.VISIBLE);
					
				} else {
					holder.view_divider.setVisibility(View.GONE);
					holder.lv_comments.setVisibility(View.GONE);
				}
				
				// 初始化 view 点击事件
				initEvents(holder, position, userId, feed.getId(), voteNicks, votes);
				
				return convertView;
		
		
	}
	
	
	/**
	 * 初始化 控件
	 * 
	 * @param holder
	 * @param convertView
	 */
	private void initView(ViewHolder holder, View convertView) {
		
			holder.iv_head = (CircleImageView) convertView.findViewById(R.id.iv_head);
			holder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
			
			// 发表动态-moment
			holder.rl_moment = (RelativeLayout) convertView.findViewById(R.id.rl_moment);
			holder.iv_moment = (ImageView) convertView.findViewById(R.id.iv_moment);
			holder.tv_moment = (TextView) convertView.findViewById(R.id.tv_moment);
			holder.fl = (FlowLayout) convertView.findViewById(R.id.fl);

			// 评论和赞
			holder.iv_vote = (ImageView) convertView.findViewById(R.id.iv_vote);
			holder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

			holder.ll_votes_comment = (LinearLayout) convertView.findViewById(R.id.ll_votes_comment);
			holder.rl_votes = (RelativeLayout) convertView.findViewById(R.id.rl_votes);
			holder.tv_vote_names = (TextView) convertView.findViewById(R.id.tv_vote_names);
			holder.view_divider = convertView.findViewById(R.id.view_divider);
			holder.lv_comments = (NoScrollListView2) convertView.findViewById(R.id.lv_comments);
			
	}
	
	/**
	 * 初始化 点击事件
	 * 
	 * @param holder
	 */
	private void initEvents(final ViewHolder holder, final int position, String userId, final String feedId, List<String> voteNicks, List<Vote> votes) {
		
		// 监听类
		mItemViewOnClickListener = new ItemViewOnClickListener(holder, position, userId, feedId, voteNicks, votes);

		// 头像
		holder.iv_head.setOnClickListener(mItemViewOnClickListener);
		// 昵称
		holder.tv_nick.setOnClickListener(mItemViewOnClickListener);
		// 赞
		holder.iv_vote.setOnClickListener(mItemViewOnClickListener);
		
//		holder.iv_comment.setTag(position);
		// 评论
		holder.iv_comment.setOnClickListener(mItemViewOnClickListener);
		// 评论框
		bt_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content = et_comment.getText().toString().trim(); 
				if (content.isEmpty()) {
					ToastUtil.showShort(mContext, "评论内容不能为空!");
				} else {
					
//					comment(feedId, content);
					comment(mFeedId, content);
					
					KeyBoardUtils.closeKeybord(et_comment, mContext);
					ll_comment.setVisibility(View.GONE);
					et_comment.setText("");
					Comment feedComment = new Comment();
					feedComment.setCreated_by(mCurrent_User.getId());
					feedComment.setContent(content);

					// 更改 mList 数据
//					List<FeedComment> comments2 = mList.get(position).getComments();
					List<Comment> comments2 = mList.get(mPosition).getComments();
					if (comments2 != null && comments2.size()>0) {
						comments2.add(feedComment);
					} else {
						comments2 = new ArrayList<Comment>();
						comments2.add(feedComment);
					}
//					mList.get(position).setComments(comments2);
					mList.get(mPosition).setComments(comments2);
					if (!mUsersInfo.containsKey(mCurrent_User.getId()+"")) {
						mUsersInfo.put(mCurrent_User.getId()+"", mCurrent_User);
					}
					// 刷新 FeedAdpter 
					changeData();
//					notifyAdapter();
					
				}
				
			}
		});
		
	}
	
	/**
	 * 改变 mList
	 */
	private void changeData() {
		EventBus.getDefault().post(new HotTag(110, mList, mUsersInfo));
	}
	
	/**
	 *  条目 内控件点击事件处理
	 *  
	 * @author 张坤
	 *
	 */
	private class ItemViewOnClickListener implements OnClickListener {
		
		private ViewHolder holder;
		private int position;
		private String userId;
		private String feedId;
		private List<String> voteNicks;
		private List<Vote> votes;
		private String currentUserNick = mCurrent_User.getNickname();
		
		public ItemViewOnClickListener (ViewHolder holder, int position, String userId, String feedId, List<String> voteNicks, List<Vote> votes) {
			this.holder = holder;
			this.position = position;
			this.userId = userId;
			this.feedId = feedId;
			this.voteNicks = voteNicks;
			this.votes = votes;
		}

		@Override
		public void onClick(View v) {
			// TODO... 
			
			Intent intent;
			switch (v.getId()) {
			
			case R.id.iv_head:
				// 用户头像
				intent = new Intent(mContext, UserInfoActivity.class);
				intent.putExtra("userid", userId);
				mContext.startActivity(intent);
				
				break;
				
			case R.id.tv_nick:
				// 用户昵称
				intent = new Intent(mContext, UserInfoActivity.class);
				intent.putExtra("userid", userId);
				mContext.startActivity(intent);
				break;
				
			case R.id.iv_vote:
				// 点赞
				if (View.VISIBLE == holder.rl_votes.getVisibility()) {
					// 已经有人点赞
					
					if (voteNicks.contains(currentUserNick)) {
						// 我已点赞
						unVote(feedId);
						voteNicks.remove(currentUserNick);
						
						Vote vote;
						for (int i = 0; i < votes.size(); i++) {
							vote = votes.get(i);
							if (currentUserNick.equals(mUsersInfo.get(vote.getCreated_by()+"").getNickname())) {
								votes.remove(vote);
							}
							
						}
						
						// 更改 mList 数据
//						mList.get(position).setVotes(votes);

						
						holder.iv_vote.setImageResource(R.drawable.feed_thumb_normal);
						if (voteNicks.size() > 0) {
							holder.tv_vote_names.setText(GDUtil.deleteBrackets(voteNicks.toString()));
						} else {
							holder.rl_votes.setVisibility(View.GONE);
						}
						
					} else {
						// 我没点赞
						vote2net(feedId);
						
						Vote vote = new Vote();
						vote.setCreated_by(mCurrent_User.getId());
						votes.add(vote);
						
						// 更改 mList 数据
//						List<FeedVote> votes2 = mList.get(position).getVotes();
//						if (votes2 != null && votes2.size()>0) {
//							votes2.add(vote);
//						} else {
//							votes2 = new ArrayList<FeedVote>();
//							votes2.add(vote);
//						}
//						mList.get(position).setVotes(votes2);
						if (!mUsersInfo.containsKey(mCurrent_User.getId()+"")) {
							mUsersInfo.put(mCurrent_User.getId()+"", mCurrent_User);
						}
						
						voteNicks.add(currentUserNick);
						holder.iv_vote.setImageResource(R.drawable.feed_thumb_selected);
						holder.tv_vote_names.setText(GDUtil.deleteBrackets(voteNicks.toString()));
					}
					
				} else {
					// 无人点赞
					vote2net(feedId);
					voteNicks.add(currentUserNick);
					
					Vote vote = new Vote();
					vote.setCreated_by(mCurrent_User.getId());
					votes.add(vote);
					
					// 更改 mList 数据
//					List<FeedVote> votes2 = mList.get(position).getVotes();
//					if (votes2 != null && votes2.size()>0) {
//						votes2.add(vote);
//					} else {
//						votes2 = new ArrayList<FeedVote>();
//						votes2.add(vote);
//					}
//					mList.get(position).setVotes(votes2);
					
					if (!mUsersInfo.containsKey(mCurrent_User.getId()+"")) {
						mUsersInfo.put(mCurrent_User.getId()+"", mCurrent_User);
					}
					
					holder.iv_vote.setImageResource(R.drawable.feed_thumb_selected);
					holder.tv_vote_names.setText(currentUserNick);
					holder.rl_votes.setVisibility(View.VISIBLE);
				}
				break;

			case R.id.iv_comment:
				// 评论
//				ToastUtil.showShort(mContext, "position"+ position +"评论");
				mFeedId = feedId;
				mPosition = position;
				ll_comment.setVisibility(View.VISIBLE);
				et_comment.requestFocus();
				KeyBoardUtils.openKeybord(et_comment, mContext);
				break;

				
				
			default:
				break;
			}
			
		}
		
	}
	
	/**
	 * 点赞
	 * 
	 */
	private void vote2net(String id) {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("target_type", "Feed");
		params.put("target_id", id);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.OPERATIONS_VOTE_POST, handler,
						NET_VOTE, params);
			}
		});
	}
	
	/**
	 * 取消点赞
	 * 
	 */
	private void unVote(final String feedId) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.OPERATIONS_UNVOTE_POST
								+ feedId + "/Feed" + ".json", handler,
						NET_UNVOTE);
			}
		});
	}
	
	/**
	 * 添加评论
	 * 
	 * @param feedId
	 * @param content
	 */
	private void comment(String feedId, String content) {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("target_type", "Feed");
		params.put("target_id", feedId);
		params.put("content", content);
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.MOMENT_CREATE_GET, handler,
							POST_COMENT, params);
			}
		});
		
	}
	
	
	
	/**
	 * Feed 
	 * @author 张坤
	 *
	 */
	private static class ViewHolder {
		CircleImageView iv_head;
		TextView tv_nick;
		TextView tv_type;
		TextView tv_time;
		ImageView iv_tag;
		
		// 发表动态
		RelativeLayout rl_moment;
		ImageView iv_moment;
		TextView tv_moment;
		FlowLayout fl;
		
		
		// 评论和赞
		ImageView iv_vote;
		ImageView iv_comment;
		
		LinearLayout ll_votes_comment;
		RelativeLayout rl_votes;
		TextView tv_vote_names;
		View view_divider;
		NoScrollListView2 lv_comments;
		
	}

	
	
	
	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 点赞
	 */
	private static final int NET_VOTE = 1;
	/**
	 * 评论
	 */
	private static final int POST_COMENT = 3;
	/**
	 * 评论
	 */
	private static final int POST_COMENT_delete = 4;
	/**
	 * 取消赞
	 */
	private static final int NET_UNVOTE = 2;
		
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
		
			switch (msg.what) {
			
			case NET_VOTE:
				// 点赞
				if (msg.obj != null) {
					// 类型转换异常
					// Log.i("votes", (String)msg.obj);
					// TODO...

					JSONObject json;
					try {
						json =  (JSONObject) msg.obj;
						if (200 == json.getInt("status")) {
							// 不做处理
//							 Log.i("votes", "vote succeed");

						} else {
							// 服务器异常
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					// 网络异常
				}
				break;

			case NET_UNVOTE:
				// 取消赞
				if (msg.obj != null) {
					JSONObject json;
					try {
//						json =  (JSONObject) msg.obj;
						json =  new JSONObject((String)msg.obj);
						if (200 == json.getInt("status")) {
							// 不做处理
//							Log.i("votes", "unvote succeed");

						} else {
							// 服务器异常
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					// 网络异常
				}
				
				break;
				
			case POST_COMENT:
				// 评论
				
				break;
				
			default:
				break;
			}
		};
		
	};

}
