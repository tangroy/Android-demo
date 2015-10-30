package com.oxygen.www.module.user.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import com.oxygen.www.R;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.BaseActivity;
import com.oxygen.www.base.Constants;
import com.oxygen.www.module.find.activity.PostsListFrament;
import com.oxygen.www.utils.UserInfoUtils;

/**
 * 获取我的收藏列表界面
 * 
 * @author kunyuan
 * 
 */
public class MyCollectionsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favicons);

		UserInfoUtils.setUserInfo(getApplicationContext(),Constants.MY_COLLECT, UrlConstants.GET_BOOKMARK);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.favicans, new PostsListFrament(), "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();

		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyCollectionsActivity.this.finish();
			}
		});

	}
}
