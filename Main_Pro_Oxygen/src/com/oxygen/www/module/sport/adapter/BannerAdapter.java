package com.oxygen.www.module.sport.adapter;

import java.util.List;

import com.oxygen.www.R;
import com.oxygen.www.enties.Banner;
import com.oxygen.www.module.challengs.activity.ChallengesDetailActivity;
import com.oxygen.www.module.find.activity.PostsDetailActivity;
import com.oxygen.www.module.sport.activity.EventsResultActivity;
import com.oxygen.www.module.sport.activity.WebViewActivity;
import com.oxygen.www.utils.ImageUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 *  动态 - banner 适配器
 *  
 * @author 张坤
 *
 */
public class BannerAdapter extends PagerAdapter {
	
	private Context mContext;
	private List<String> mList;
	private List<Banner> mBanners;
	private int size;
	
	public BannerAdapter(Context context, List<String> list, List<Banner> banners) {
		mContext = context;
		mList = list;
		mBanners = banners;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
//		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView iv = new ImageView(mContext);
		size = mList.size();
		ImageUtil.showImage2(mList.get(position % size), iv, R.drawable.iv_loading);
		iv.setScaleType(ScaleType.CENTER_CROP);

		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Log.i("banners", "图片 "+position+" 被点击了");
				
				Banner banner = mBanners.get(position % size);
				String type = banner.getType();
				Intent intent;

				if ("webview".equals(type)) {
					// 外链
					intent = new Intent(mContext, WebViewActivity.class);
					intent.putExtra("target_url", banner.getTarget_url());
					intent.putExtra("hasTitle", true);
					intent.putExtra("title", "乐运动");
					
					mContext.startActivity(intent);
					
				} else if ("objectview".equals(type)) {
					// 活动, 挑战, 精选
					String targetType = banner.getTarget_type();
					int target_id_int = banner.getTarget_id();  
					
					if ("Event".equals(targetType)) {
						// 活动
						intent = new Intent(mContext, EventsResultActivity.class);
						intent.putExtra("eventid", target_id_int);
						mContext.startActivity(intent);
						
					} else if ("Challenge".equals(targetType)) {
						// 挑战
						intent = new Intent(mContext, ChallengesDetailActivity.class);
						intent.putExtra("challengesid", target_id_int);
						mContext.startActivity(intent);
						
					} else if ("Post".equals(targetType)) {
						// 精选
						// TODO...
						intent = new Intent(mContext, PostsDetailActivity.class);
						intent.putExtra("posts_id", target_id_int);
						intent.putExtra("picurl", mList.get(position % size));
						mContext.startActivity(intent);
						
					} 
					
				}
				
			}
		});
		
		container.addView(iv);
		return iv;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		 ((ViewPager) container).removeView((ImageView) object);
	}
	

}
