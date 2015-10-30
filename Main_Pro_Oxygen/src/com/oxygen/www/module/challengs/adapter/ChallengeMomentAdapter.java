package com.oxygen.www.module.challengs.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.Moment;
import com.oxygen.www.enties.Vote;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.challengs.eventbus_enties.ListViewAddHeight;
import com.oxygen.www.module.sport.adapter.CommentAdapter;
import com.oxygen.www.module.user.activity.UserInfoActivity;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollListView;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 挑战战况直播适配器界面
 * @author 杨庆雷
 * 2015-7-21上午9:37:11
 */
public class ChallengeMomentAdapter extends BaseAdapter {
	protected static final int NET_UNVOTE = 111;
	protected static final int NET_VOTE = 222;
	private Context c;
	private LayoutInflater mInflater;
	private List<Moment> moments;
	private JSONObject jsonobject_userinfo;
	private JSONObject Current_userinfo;
	private Handler handler;
	private int currentId;
	private LinearLayout ll_moment;
	private EditText et_moment;
	private Button bt_commit;
	private List<Boolean> isvoteList;
	private List<List<String>> voteNamess;
	private List<String> voteNames;
	private String nativeDate;
	private List<Comment> comments;
	private List<List<Comment>> commentss;
	private int position;
	public ChallengeMomentAdapter(LinearLayout ll_moment,EditText et_moment,Button bt_commit,Context c,List<Moment> moments,
			JSONObject jsonobject_userinfo,JSONObject Current_userinfo,Handler handler){
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.moments = moments;
		this.jsonobject_userinfo = jsonobject_userinfo;
		this.Current_userinfo = Current_userinfo;
		this.handler = handler;
		this.ll_moment = ll_moment;
		this.et_moment = et_moment;
		this.bt_commit = bt_commit;
		isvoteList = new ArrayList<Boolean>();
		commentss = new ArrayList<List<Comment>>();
		voteNamess = new ArrayList<List<String>>();
		voteNames = new ArrayList<String>();
		if(moments != null){
			for (int i = 0; i < moments.size(); i++) {
				isvoteList.add(false);
			}
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");     
		nativeDate = sDateFormat.format(new java.util.Date());
	}
	@Override
	public int getCount() {
		if(moments == null){
			return 0;
		}
		return moments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		voteNames.clear();
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_challenge_moment, null); 
			holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
			holder.iv_message_head = (CircleImageView) convertView.findViewById(R.id.iv_message_head);
			holder.moment_words = (TextView) convertView.findViewById(R.id.moment_words);
			holder.comment_praise = (ImageView) convertView.findViewById(R.id.comment_praise);
			holder.comment_moment = (ImageView) convertView.findViewById(R.id.comment_moment);
			holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
			holder.comment_praise_names = (TextView) convertView.findViewById(R.id.comment_praise_names);
			holder.lv_moment_comment = (NoScrollListView) convertView.findViewById(R.id.lv_moment_comment);
			holder.ll_praise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
			holder.v_view = (View) convertView.findViewById(R.id.v_view);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			holder.v_view.setVisibility(View.VISIBLE);
			holder.iv_circle.setImageResource(R.drawable.icon_timeline_red);
		}
		Moment moment = moments.get(position);
		String time = moment.getCreated_at();
		try {
			currentId = Current_userinfo.getInt("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String url = jsonobject_userinfo.getJSONObject(
					moments.get(position).getCreated_by() + "").getString(
					"headimgurl");
			ImageUtil.showImage(url+Constants.qiniu_photo_head, holder.iv_message_head,
					R.drawable.icon_def);
			String name = jsonobject_userinfo.getJSONObject(
					moments.get(position).getCreated_by() + "").getString(
					"nickname");
			String words = moments.get(position).getWords();
			holder.moment_words.setText(name + " " + words);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String month = time.substring(5, 7);
		String day = time.substring(8,10);
		String hour = time.substring(11, 16);
		String date = time.substring(0,10);
		if(nativeDate.equals(date)){
			holder.message_time.setText(hour);
		}else{
			holder.message_time.setText(month+"月"+day+"日");
		}
		//当前用户是否已经点赞
		if(moments.get(position).getVotes() == null){
			holder.comment_praise.setImageResource(R.drawable.icon_challenge_praise_hollow_white);
			holder.ll_praise.setVisibility(View.GONE);
		}else{
			for (int i = 0; i < moments.get(position).getVotes().size(); i++) {
				try {
					if(moments.get(position).getVotes().get(i).getCreated_by() == currentId){
						holder.comment_praise.setImageResource(R.drawable.icon_challenge_praise_red);
						isvoteList.set(position, true);
					}else{
						holder.comment_praise.setImageResource(R.drawable.icon_challenge_praise_hollow_white);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//当前评论的点赞
		if(moments != null && moments.size() > 0 && moments.get(position).getVotes() != null){
			for (int i = 0; i < moments.get(position).getVotes().size(); i++) {
				try {
					voteNames.add(jsonobject_userinfo.getJSONObject(moments
							.get(position).getVotes().get(i)
							.getCreated_by()
							+ "").getString("nickname"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(voteNames.size() != 0){
			holder.ll_praise.setVisibility(View.VISIBLE);
			holder.comment_praise_names.setText(voteNames.toString().substring(1, voteNames.toString().length()-1));
		}
		voteNamess.add(voteNames);
		//点赞
		holder.comment_praise.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//取消点赞
				if(isvoteList.get(position)){
					holder.comment_praise.setImageResource(R.drawable.icon_challenge_praise_hollow_white);
					if(moments.get(position).getVotes() != null){
						for (int i = 0; i <moments.get(position).getVotes().size(); i++) {
							if (currentId == moments.get(position).getVotes()
									.get(i).getCreated_by()) {
								unvote(moments.get(position).getVotes().get(i));
								break;
							}
						}
					}
					try {
						voteNames.remove(Current_userinfo.getString("nickname"));
						voteNamess.set(position, voteNames);
						holder.comment_praise_names.setText(voteNames.toString().substring(1, voteNames.toString().length()-1));
						if(voteNames.size() == 0){
							holder.ll_praise.setVisibility(View.GONE);
							EventBus.getDefault().post(new ListViewAddHeight(0));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					isvoteList.set(position, false);
				}else{//点赞
					Vote vote = new Vote();
					vote.setCreated_by(currentId);
					vote.setTarget_type("Moment");
					vote.setTarget_id(moments.get(position).getId());
					vote(vote);
					holder.comment_praise.setImageResource(R.drawable.icon_challenge_praise_red);
					try {
						if(voteNames.size() == 0){
							EventBus.getDefault().post(new ListViewAddHeight(0));
						}
						voteNames.add(Current_userinfo.getString("nickname"));
						voteNamess.set(position, voteNames);
						holder.comment_praise_names.setText(voteNames.toString().substring(1, voteNames.toString().length()-1));
						holder.ll_praise.setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					isvoteList.set(position, true);
				}
			}
		});	
		
		/**
		 * 评论
		 */
		holder.comment_moment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyBoardUtils.openKeybord(et_moment, c);
				ChallengeMomentAdapter.this.position = position;
				ll_moment.setVisibility(View.VISIBLE);
				et_moment.requestFocus();
				KeyBoardUtils.openKeybord(et_moment, c);
				String id = moments.get(position).getId()+"";
				UserInfoUtils.setUserInfo(c.getApplicationContext(), Constants.SET_COMENT_ID, id);
			}
		});
		
		comments = moment.getComments();
		commentss.add(comments);
		if (comments != null && comments.size() > 0) {
			CommentAdapter commentadapter = new CommentAdapter(c, comments,jsonobject_userinfo);
			holder.lv_moment_comment.setAdapter(commentadapter);
			holder.lv_moment_comment.setVisibility(View.VISIBLE);
		}else{
			holder.lv_moment_comment.setVisibility(View.GONE);
		}
		
		//提交评论
		bt_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				KeyBoardUtils.closeKeybord(et_moment, c);
				ll_moment.setVisibility(View.INVISIBLE);
				//提交评论到服务器
				((ChallengesDetailActivity) c).postComentToNet();
				
				Comment comment = new Comment();
				comment.setContent(et_moment.getText().toString());
				comment.setCreated_by(currentId);
				//当前战况消息没有评论0
				if(commentss.get(ChallengeMomentAdapter.this.position) == null){
					List<Comment> commentList = new ArrayList<Comment>();
					commentss.remove(ChallengeMomentAdapter.this.position);
					commentss.add(ChallengeMomentAdapter.this.position, commentList);
					commentss.get(ChallengeMomentAdapter.this.position).add(comment);
				}else{
					commentss.get(ChallengeMomentAdapter.this.position).add(comment);
				}
			}
		});
		//头像跳转个人资料页
		holder.iv_message_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//不是小乐的情况下跳转
				if(moments.get(position).created_by != Constants.XIAOLE_ID){
					Intent userIntent = new Intent(c,UserInfoActivity.class);
					userIntent.putExtra("userid", moments.get(position).created_by+"");
					c.startActivity(userIntent);
				}
			}
		});
		return convertView;
	}

	public class ViewHolder{
		TextView message_time;
		CircleImageView iv_message_head;
		TextView moment_words;
		ImageView comment_praise;
		ImageView comment_moment;
		TextView comment_praise_names;
		NoScrollListView lv_moment_comment;
		LinearLayout ll_praise;
		View v_view;
		ImageView iv_circle;
	}
	
	/**
	 * 取消点赞
	 * 
	 * @param momoentid
	 */
	private void unvote(final Vote vote) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(
						UrlConstants.OPERATIONS_UNVOTE_POST
								+ vote.getTarget_id() + "/"
								+ vote.getTarget_type() + ".json", handler,
						NET_UNVOTE);
			}
		});
	}
	
	/**
	 * 点赞
	 * 
	 * @param momoentid
	 */
	private void vote(Vote vote) {
		// POST 参数
		final Map<String, String> params = new HashMap<String, String>();
		params.put("target_type", vote.getTarget_type());
		params.put("target_id", vote.getTarget_id() + "");
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Post(UrlConstants.OPERATIONS_VOTE_POST, handler,
						NET_VOTE, params);
			}
		});
	}
}
