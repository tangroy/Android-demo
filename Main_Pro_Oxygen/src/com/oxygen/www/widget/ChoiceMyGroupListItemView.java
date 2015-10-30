package com.oxygen.www.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.enties.Group;
import com.oxygen.www.utils.ImageUtil;

public class ChoiceMyGroupListItemView extends LinearLayout implements Checkable {

	// private TextView nameTxt;
	private CheckBox selectBtn;
	Context context;
	public TextView tv_name, tv_member_count, tv_select_member_count,tv_joined;
	public CircleImageView iv_head;

	public ChoiceMyGroupListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.item_group_tochallenges, this, true);
		selectBtn = (CheckBox) v.findViewById(R.id.radio);
		tv_name = (TextView) v.findViewById(R.id.tv_name);
		tv_member_count = (TextView) v.findViewById(R.id.tv_member_count);
		iv_head = (CircleImageView) v.findViewById(R.id.iv_head);
		tv_select_member_count = (TextView) v.findViewById(R.id.tv_select_member_count);
		tv_joined = (TextView) v.findViewById(R.id.tv_joined);
	}

	public void setData(Group group,ArrayList<ChallengesUser>  Groups_leaderboard,View v) {
		if(Groups_leaderboard!=null&&Groups_leaderboard.size()>0){
			for (int i = 0; i < Groups_leaderboard.size(); i++) {
				if(group.getId()==Groups_leaderboard.get(i).getGroup_id()){
					tv_joined.setVisibility(View.VISIBLE);
					selectBtn.setVisibility(View.GONE);
					break;
				}else{
					tv_joined.setVisibility(View.GONE);
					selectBtn.setVisibility(View.VISIBLE);
			}
			}
		}
		else{
			tv_joined.setVisibility(View.GONE);
		}
		tv_select_member_count.setVisibility(View.VISIBLE);
		tv_select_member_count.setText("活动数：" + group.getEvent_count());
		ImageUtil.showImage(group.getPic() + Constants.qiniu_photo_head,
				iv_head, R.drawable.icon_def);
		tv_name.setText(group.getName());
		tv_member_count.setText("成员：" + group.getMember_count() + "人");
	}

	@Override
	public boolean isChecked() {
		return selectBtn.isChecked();
	}

	@Override
	public void setChecked(boolean checked) {
		selectBtn.setChecked(checked);
		// //根据是否选中来选择不同的背景图片
		if (checked) {
			selectBtn.setBackgroundResource(R.drawable.invite_friend_select);
		} else {
			selectBtn.setBackgroundColor(context.getResources().getColor(
					R.color.black));
			selectBtn.setBackgroundResource(R.drawable.invite_friend);

		}
	}

	@Override
	public void toggle() {
		selectBtn.toggle();
	}

}
