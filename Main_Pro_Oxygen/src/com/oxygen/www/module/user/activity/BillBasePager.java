package com.oxygen.www.module.user.activity;

import com.oxygen.www.R;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class BillBasePager {
	public Context mContext;
	private View rootView;
	public FrameLayout flContent;

	public BillBasePager(Context context) {
		this.mContext = context;
		rootView = initView();
	}

	public View initView() {
		View view = View.inflate(mContext, R.layout.activity_bill_tab_base,
				null);
		flContent = (FrameLayout) view
				.findViewById(R.id.fl_bill_base_pager_content);
		return view;
	}

	public View getRootView() {
		return rootView;
	}

	public void initData() {

	}

}
