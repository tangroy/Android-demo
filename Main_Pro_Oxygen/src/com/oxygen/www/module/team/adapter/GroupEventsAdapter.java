package com.oxygen.www.module.team.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.EventFeed;
import com.oxygen.www.enties.UserInfo;
import com.oxygen.www.enties.Vote;
import com.oxygen.www.module.sport.adapter.FeedCommentAdapter;
import com.oxygen.www.module.sport.construct.UsersConstruct;
import com.oxygen.www.module.team.eventbus_enties.EventFeedData;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollListView2;

import de.greenrobot.event.EventBus;

public class GroupEventsAdapter extends BaseAdapter {
	/**
	 * 点赞
	 */
	private static final int NET_VOTE = 1;
	/**
	 * 取消赞
	 */
	private static final int NET_UNVOTE = 2;
	/**
	 * 评论
	 */
	private static final int POST_COMENT = 3;
	private Context c;
	private LayoutInflater mInflater;
	private ArrayList<EventFeed> events;
	private Map<String, UserInfo> usersMap;
	private LinearLayout ll_comment;
	private EditText et_comment;
	private Button bt_comment;
	DecimalFormat df = new DecimalFormat("#0.00");
	public String mFeedId;
	public int mPosition;
	private UserInfo mCurrent_User;
	/**
	 * 条目 内控件点击事件监听类
	 */
	private ItemViewOnClickListener mItemViewOnClickListener;

	public GroupEventsAdapter(Context c, ArrayList<EventFeed> events,
			Map<String, UserInfo> usersMap,JSONObject current_user,LinearLayout ll_comment, EditText et_comment, Button bt_comment) {
		this.c = c;
		this.events = events;
		this.ll_comment = ll_comment;
		this.et_comment = et_comment;
		this.bt_comment = bt_comment;
		mInflater = LayoutInflater.from(c);
		this.usersMap = usersMap;
		mCurrent_User = UsersConstruct.ToUserInfo(current_user);
	}

	@Override
	public int getCount() {
		return events.size();
	}

	@Override
	public Object getItem(int arg0) {
		return events.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_group_events, null);
			holder.lv_comments = (NoScrollListView2) convertView
					.findViewById(R.id.lv_comments);
			holder.iv_head = (CircleImageView) convertView
					.findViewById(R.id.iv_head);
			holder.rl_votes = (RelativeLayout) convertView
					.findViewById(R.id.rl_votes);
			holder.iv_team = (CircleImageView) convertView
					.findViewById(R.id.iv_team);
			holder.iv_vote = (ImageView) convertView.findViewById(R.id.iv_vote);
			holder.iv_comment = (ImageView) convertView
					.findViewById(R.id.iv_comment);
			holder.iv_vote_thumb = (ImageView) convertView
					.findViewById(R.id.iv_vote_thumb);
			holder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_team_name = (TextView) convertView
					.findViewById(R.id.tv_team_name);
			holder.tv_team_title = (TextView) convertView
					.findViewById(R.id.tv_team_title);
			holder.tv_vote_names = (TextView) convertView
					.findViewById(R.id.tv_vote_names);
			holder.tv_start_time = (TextView) convertView
					.findViewById(R.id.tv_start_time);
			holder.view_divider = (View) convertView
					.findViewById(R.id.view_divider);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		EventFeed event = events.get(position);
		UserInfo userInfo = usersMap.get(event.getUser_id());

		ImageUtil.showImage2(userInfo.getHeadimgurl(), holder.iv_head,
				R.drawable.icon_def);
		holder.tv_nick.setText(userInfo.getNickname());
		holder.tv_type.setText(event.getTitle());
		holder.tv_time.setText(event.getCreated_at());
		
		String type = event.getTarget_type();
		if("Group".equals(type)){
			holder.tv_team_name.setText(event.getName());
			holder.tv_team_title.setText(event.getIntro());
			holder.tv_start_time.setVisibility(View.GONE);
			ImageUtil.showImage2(event.getPic(), holder.iv_team,
					R.drawable.icon_def);
		}else if("Event".equals(type)){
			holder.tv_team_name.setText(event.getData_title());
			holder.tv_start_time.setText(event.getCreated_at());
			holder.tv_team_title.setText(event.getAddress());
			ImageUtil.showImage2(event.getPic(), holder.iv_team,
					c.getResources().getIdentifier("bg_eventdetail_" +event.getSport(), "drawable", c.getPackageName()));
		}else if("Challenge".equals(type)){
			holder.tv_team_name.setText(event.getData_title());
			holder.tv_start_time.setText(event.getDays_left());
			holder.tv_team_title.setText(event.getIntro());
			String is_group = event.getIs_group();
			String is_team = event.getIs_team();
			if ("no".equals(is_group) && "no".equals(is_team)) {
				holder.iv_team.setImageResource(R.drawable.icon_create_challenge_single);
			} else if ("yes".equals(is_team)) {
				holder.iv_team.setImageResource(R.drawable.icon_create_challenge_two);
			} else if ("yes".equals(is_group)) {
				holder.iv_team.setImageResource(R.drawable.icon_create_challenge_more);
			}
		}

		// 赞
		List<Vote> votes = event.getVotes();
		List<String> voteNicks = new ArrayList<String>();
		if (votes != null && votes.size() > 0) {
			for (Vote vote : votes) {
				String nick = usersMap.get((vote.getCreated_by() + ""))
						.getNickname();
				voteNicks.add(nick);
			}
			if ("yes".equals(event.getCurr_user_voted())) {
				holder.iv_vote.setImageResource(R.drawable.feed_thumb_selected);
			} else {
				holder.iv_vote.setImageResource(R.drawable.feed_thumb_normal);
			}
			holder.rl_votes.setVisibility(View.VISIBLE);
			holder.tv_vote_names.setText(GDUtil.deleteBrackets(voteNicks
					.toString()));
		} else {
			// 无人点赞
			holder.iv_vote.setImageResource(R.drawable.feed_thumb_normal);
			holder.rl_votes.setVisibility(View.GONE);
		}
		// 评论
		List<Comment> comments = event.getComments();
		if (comments != null && comments.size() > 0) {
			final FeedCommentAdapter commentAdapter = new FeedCommentAdapter(
					comments, c, userInfo, usersMap);
			holder.lv_comments.setAdapter(commentAdapter);
			holder.view_divider.setVisibility(View.VISIBLE);
			holder.lv_comments.setVisibility(View.VISIBLE);
		} else {
			holder.view_divider.setVisibility(View.GONE);
			holder.lv_comments.setVisibility(View.GONE);
		}
		// 初始化 view 点击事件
		 initEvents(holder, position, event.getUser_id(), event.getId(),voteNicks,votes);
		// voteNicks, votes);
		return convertView;
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
					ToastUtil.showShort(c, "评论内容不能为空!");
				} else {
					comment(mFeedId, content);
					KeyBoardUtils.closeKeybord(et_comment, c);
					ll_comment.setVisibility(View.GONE);
					et_comment.setText("");
					Comment feedComment = new Comment();
					feedComment.setCreated_by(mCurrent_User.getId());
					feedComment.setContent(content);

					// 更改 mList 数据
					List<Comment> comments2 = events.get(mPosition).getComments();
					if (comments2 != null && comments2.size()>0) {
						comments2.add(feedComment);
					} else {
						comments2 = new ArrayList<Comment>();
						comments2.add(feedComment);
					}
					events.get(mPosition).setComments(comments2);
					if (!usersMap.containsKey(mCurrent_User.getId()+"")) {
						usersMap.put(mCurrent_User.getId()+"", mCurrent_User);
					}
					changeData();
				}
			}
		});
	}
	/**
	 *  条目 内控件点击事件处理
	 * @author 张坤
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
			Intent intent;
			switch (v.getId()) {
			case R.id.iv_head:
				// 用户头像
				intent = new Intent(c, UserInfoActivity.class);
				intent.putExtra("userid", userId);
				c.startActivity(intent);
				break;
			case R.id.tv_nick:
				// 用户昵称
				intent = new Intent(c, UserInfoActivity.class);
				intent.putExtra("userid", userId);
				c.startActivity(intent);
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
							if (currentUserNick.equals(usersMap.get(vote.getCreated_by()+"").getNickname())) {
								votes.remove(vote);
							}
						}
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
						if (!usersMap.containsKey(mCurrent_User.getId()+"")) {
							usersMap.put(mCurrent_User.getId()+"", mCurrent_User);
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
					if (!usersMap.containsKey(mCurrent_User.getId()+"")) {
						usersMap.put(mCurrent_User.getId()+"", mCurrent_User);
					}
					
					holder.iv_vote.setImageResource(R.drawable.feed_thumb_selected);
					holder.tv_vote_names.setText(currentUserNick);
					holder.rl_votes.setVisibility(View.VISIBLE);
				}
				break;

			case R.id.iv_comment:
				// 评论
				mFeedId = feedId;
				mPosition = position;
				ll_comment.setVisibility(View.VISIBLE);
				et_comment.requestFocus();
				KeyBoardUtils.openKeybord(et_comment, c);
				break;

				
				
			default:
				break;
			}
			
		}
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
	 * 改变 mList
	 */
	private void changeData() {
		EventBus.getDefault().post(new EventFeedData(events, usersMap));
	}

	public class ViewHolder {
		public NoScrollListView2 lv_comments;
		public RelativeLayout rl_votes;
		public CircleImageView iv_head;
		public TextView tv_nick;
		public TextView tv_type;
		public TextView tv_time;
		public CircleImageView iv_team;
		public TextView tv_team_name;
		public TextView tv_team_title;
		public TextView tv_start_time;

		public ImageView iv_vote;
		public ImageView iv_comment;
		public ImageView iv_vote_thumb;
		public TextView tv_vote_names;
		public View view_divider;
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NET_VOTE:
				// 点赞
				if (msg.obj != null) {
					JSONObject json;
					try {
						json =  (JSONObject) msg.obj;
						if (200 == json.getInt("status")) {
						} else {
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
				}
				break;
			case NET_UNVOTE:
				// 取消赞
				if (msg.obj != null) {
					JSONObject json;
					try {
						json =  new JSONObject((String)msg.obj);
						if (200 == json.getInt("status")) {

						} else {
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
				}
				break;
			case POST_COMENT:
				break;
			default:
				break;
			}
		};
	};
}
