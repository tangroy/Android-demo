package com.oxygen.www.module.sport.listener;

import android.widget.AbsListView;

public interface OnAdapterScrollListener extends AbsListView.OnScrollListener {
	
    void onTopWhenScrollIdle(AbsListView view);

    void onBottomWhenScrollIdle(AbsListView view);

}
