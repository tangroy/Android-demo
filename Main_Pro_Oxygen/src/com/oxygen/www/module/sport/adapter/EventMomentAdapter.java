package com.oxygen.www.module.sport.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.enties.Comment;
import com.oxygen.www.enties.Moment;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.enties.Vote;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.utils.GDUtil;
import com.oxygen.www.utils.HttpUtil;
import com.oxygen.www.utils.ImageUtil;
import com.oxygen.www.utils.KeyBoardUtils;
import com.oxygen.www.utils.ToastUtil;
import com.oxygen.www.utils.UserInfoUtils;
import com.oxygen.www.widget.CircleImageView;
import com.oxygen.www.widget.NoScrollGridView;
import com.oxygen.www.widget.NoScrollListView;

public class EventMomentAdapter extends BaseAdapter {

	private Context c;
	private LayoutInflater mInflater;
	/**
	 * 动态集合
	 */
	private ArrayList<Moment> moments;
	private JSONObject jsonarray_userinfo;
	private final int NET_DEL = 1;
	private final int NET_VOTE = 2;
	private final int NET_UNVOTE = 3;
	private int del_position = 0;
	private int current_userid;
	private int event_created_id;
	int count=0;
	@SuppressLint("SimpleDateFormat") 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	boolean isall = false;
	/**
	 * 评论现形布局
	 */
	private LinearLayout ll_moment;
	/**
	 * 评论输入框
	 */
	private EditText et_moment;
	private ViewPager vp_group_photo;
	/**
	 * 活动动态
	 */
	private NoScrollListView lv_moments;
	/**
	 * 记录删除位置
	 */
	private int mPosition;
	
	public EventMomentAdapter(LinearLayout ll_moment, EditText et_moment, NoScrollListView lv_moments, Context c, ArrayList<Moment> moments,
			JSONObject jsonarray_userinfo, int current_userid, int event_created_id, int count,boolean isall,ViewPager vp_group_photo) {
		this.c = c;
		mInflater = LayoutInflater.from(c);
		this.moments = moments;
		this.jsonarray_userinfo = jsonarray_userinfo;
		this.current_userid = current_userid;
		this.event_created_id = event_created_id;
		this.count =count;
		this.isall = isall;
		this.ll_moment = ll_moment;
		this.et_moment = et_moment;
		this.lv_moments = lv_moments;
		this.vp_group_photo = vp_group_photo;
	}

	@Override
	public int getCount() {
		return isall?moments.size():count;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		 ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_moments, null);
			holder.iv_head = (CircleImageView) convertView.findViewById(R.id.iv_head);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.del = (TextView) convertView.findViewById(R.id.del);
			holder.tv_praiselist = (TextView) convertView
					.findViewById(R.id.tv_praiselist);
			holder.lv_moments = (NoScrollListView) convertView
					.findViewById(R.id.lv_comment);
			holder.gv_photos = (NoScrollGridView) convertView
					.findViewById(R.id.photos);
			holder.comment = (Button) convertView.findViewById(R.id.comment);
			holder.vote = (Button) convertView.findViewById(R.id.vote);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(moments.size()>0){
	 		try {
				del_position = arg0;
				String url = jsonarray_userinfo.getJSONObject(
						moments.get(arg0).getCreated_by() + "").getString(
						"headimgurl");
				ImageUtil.showImage(url+Constants.qiniu_photo_head, holder.iv_head,
						R.drawable.icon_def);
				String name = jsonarray_userinfo.getJSONObject(
						moments.get(arg0).getCreated_by() + "").getString(
						"nickname");
				holder.name.setText(name.equals("null") ? "--" : name);
				
				if (moments.get(arg0).getCreated_by() == current_userid 
						|| current_userid == event_created_id) {
					// 动态发布者, 活动创建者 均可删除
					holder.del.setVisibility(View.VISIBLE);
				}
				holder.del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg) {
						mPosition = arg0;
						showDialog(c);
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
	 		
			ArrayList<Photo> photos = moments.get(arg0).getPhotos();
			ArrayList<Comment> comments = moments.get(arg0).getComments();
	
			String word = moments.get(arg0).getWords();
			if (!word.equals("")) {
				holder.content.setVisibility(View.VISIBLE);
				holder.content.setText(word);
			} else {
				holder.content.setVisibility(View.GONE);
			}
			String time = moments.get(arg0).getCreated_at();
			try {
				holder.time.setText(GDUtil.getTimeDiff2(sdf.parse(time)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.vote.setText("赞");
	 		if (moments.get(arg0).getVotes() != null
					&& moments.get(arg0).getVotes().size() > 0) {
				String names = "";
				for (int i = 0; i < moments.get(arg0).getVotes().size(); i++) {
					try {
						if (moments.get(arg0).getVotes().get(i).getCreated_by() == current_userid) {
							holder.vote.setText("取消赞");
	 					}
						names = names
								+ (jsonarray_userinfo.getJSONObject(moments
										.get(arg0).getVotes().get(i)
										.getCreated_by()
										+ "").getString("nickname")) + "、";
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				holder.tv_praiselist.setVisibility(View.VISIBLE);
				

				if(names.length()>1){
					holder.tv_praiselist.setText(Html.fromHtml(names.substring(0,
							names.length() - 1)
							+ "<font color=\"#808080\">觉得很赞.</font>"));
				}
				
	
			} else {
				holder.tv_praiselist.setVisibility(View.GONE);
			}
	 		
	 		//点赞
			holder.vote.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View arg) {
					boolean isvote =true;
					for (int i = 0; i <moments.get(arg0).getVotes().size(); i++) {
						if (current_userid == moments.get(arg0).getVotes()
								.get(i).getCreated_by()) {
							unvote(moments.get(arg0).getVotes().get(i));
							moments.get(arg0).getVotes().remove(i);
	 						notifyDataSetChanged();
							isvote = false;
							break;
	 					}
					}
					if(isvote){
						Vote vote = new Vote();
						vote.setCreated_by(current_userid);
						vote.setTarget_type("Moment");
						vote.setTarget_id(moments.get(arg0).getId());
						moments.get(arg0).getVotes().add(vote);
						notifyDataSetChanged();
	 					vote(vote);
					}
					
	
				}
			});
			
			//评论
			holder.comment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					ll_moment.setVisibility(View.VISIBLE);
					et_moment.requestFocus();
					KeyBoardUtils.openKeybord(et_moment, c);
					et_moment.setText("");
					String id = moments.get(arg0).getId()+"";
					UserInfoUtils.setUserInfo(c.getApplicationContext(),Constants.SET_COMENT_ID, id);
				}
			});
			
	
			if (comments != null && comments.size() > 0) {
				holder.lv_moments.setVisibility(View.VISIBLE);
				CommentAdapter commentadapter = new CommentAdapter(c, comments,
						jsonarray_userinfo);
				ViewGroup.LayoutParams params = holder.lv_moments.getLayoutParams();
//				params.height =comments.size()* ((int) (25 * OxygenApplication.ppi+0.5));
				params.height = LayoutParams.WRAP_CONTENT;
				holder.lv_moments.setLayoutParams(params);
				holder.lv_moments.setAdapter(commentadapter);
//				Untilly.setListViewHeightBasedOnChildren(holder.lv_moments, 0);
				
			} else {
				holder.lv_moments.setVisibility(View.GONE);
			}
			if (photos != null) {
				EventDetailMomentPhotosAdapter adapter = new EventDetailMomentPhotosAdapter(
						c, photos,vp_group_photo);
				ViewGroup.LayoutParams params_b = holder.gv_photos.getLayoutParams();
				
				params_b.height = ((int) Math.floor((photos.size()+2) / 3) )
						* ((int) (80 * OxygenApplication.ppi+0.5));
				Log.i("photos", "position:"+arg0+" , size = "+photos.size()+", word: "+word+", params_b.height"+params_b.height);
				holder.gv_photos.setLayoutParams(params_b);
				holder.gv_photos.setAdapter(adapter);
			} 
//			else {
//				Log.i("photos", "position:"+arg0+" , size = 0");
//				holder.gv_photos.setVisibility(View.GONE);
//			}
		}
		
		return convertView;
	}

	/**
	 * 删除moment
	 * 
	 * @param momoentid
	 */
	private void del(final int momoentid) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() {
			public void run() {
				HttpUtil.Get(UrlConstants.MOMENT_DEL_GET + momoentid + ".json",
						handler, NET_DEL);
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
	 * 提示删除
	 * 
	 * @param content
	 */
	private void showDialog(Context content) {
		// if (Double.parseDouble(df.format(journey / 1000)) > 0.0) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(content);
		dialog.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("确定删除该条动态?")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// 取消弹出框
					}
				})
				.setNegativeButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						del(moments.get(del_position).getId());
						del(moments.get(mPosition).getId());

					}
				}).create().show();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_DEL:
				String strObject = (String) msg.obj;
				if (strObject != null && strObject.length() > 10) {
					try {
						JSONObject jsoninfos = new JSONObject(strObject);
						if (jsoninfos.getInt("status") == 200) {
							
//							moments.remove(del_position);
							moments.remove(mPosition);
							isall = true;
							
							ViewGroup.LayoutParams params = lv_moments.getLayoutParams();
							params.height = LayoutParams.WRAP_CONTENT;
							lv_moments.setLayoutParams(params);
							
							notifyDataSetChanged();
//							Untilly.setListViewHeightBasedOnChildren(EventsResultActivity.lv_moments, 0);
							if (moments.isEmpty()) {
								EventsResultActivity.iv_nomoment.setVisibility(View.VISIBLE);
								EventsResultActivity.moment_defttext.setVisibility(View.VISIBLE);
							} 
							
//							EventsResultActivity.tv_moments_more.setVisibility(View.GONE);
							if (moments.size() > 1) {
								EventsResultActivity.tv_moments_more.setText("查看全部评论(" + moments.size() + ")");
							} else {
								EventsResultActivity.tv_moments_more.setVisibility(View.GONE);
								
							}
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.show(c, "删除失败");
				}

				break;

			default:
				break;
			}
		}
	};

	public class ViewHolder {
		CircleImageView iv_head;
		TextView name;
		TextView content;
		TextView time;
		TextView del;
		TextView tv_praiselist;
		Button comment, vote;
		NoScrollListView lv_moments;
		NoScrollGridView gv_photos;
	}

}