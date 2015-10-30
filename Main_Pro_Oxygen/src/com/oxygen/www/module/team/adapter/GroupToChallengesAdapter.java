package com.oxygen.www.module.team.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.oxygen.www.enties.Group;
import com.oxygen.www.enties.ChallengesUser;
import com.oxygen.www.widget.ChoiceMyGroupListItemView;

public class GroupToChallengesAdapter extends BaseAdapter {
	ArrayList<Group> groups;
	Context c;
	private ArrayList<ChallengesUser>  Groups_leaderboard;


	public GroupToChallengesAdapter(ArrayList<Group> groups, Context c,ArrayList<ChallengesUser>  Groups_leaderboard) {
		this.c = c;
		this.groups = groups;
		this.Groups_leaderboard = Groups_leaderboard;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ChoiceMyGroupListItemView choiceListItemView = new ChoiceMyGroupListItemView(c, null);
		choiceListItemView.setData(groups.get(arg0),Groups_leaderboard,choiceListItemView);
		return choiceListItemView;

	}

}
