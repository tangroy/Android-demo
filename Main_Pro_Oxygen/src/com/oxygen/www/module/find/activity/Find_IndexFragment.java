package com.oxygen.www.module.find.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.oxygen.www.R;

public class Find_IndexFragment extends Fragment implements OnClickListener {
	private View parentView;
	private RelativeLayout rl_businesses, rl_posts;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = inflater.inflate(R.layout.fragment_find, container, false);
		setUpViews();
		return parentView;
	}

	private void setUpViews() {
		context = getActivity();
		rl_businesses = (RelativeLayout) parentView
				.findViewById(R.id.rl_businesses);
		rl_posts = (RelativeLayout) parentView.findViewById(R.id.rl_posts);
		rl_businesses.setOnClickListener(Find_IndexFragment.this);
		rl_posts.setOnClickListener(Find_IndexFragment.this);

	}

	@Override
	public void onClick(View arg0) {
		if (arg0.equals(rl_businesses)) {
			Intent i = new Intent(context, BusinessesListActivity.class);
			startActivity(i);
		} else if (arg0.equals(rl_posts)) {
			Intent i = new Intent(context, PostsListFrament.class);
			startActivity(i);
		}

	}

}
