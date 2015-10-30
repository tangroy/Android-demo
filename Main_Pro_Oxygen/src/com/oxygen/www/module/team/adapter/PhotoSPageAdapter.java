package com.oxygen.www.module.team.adapter;

import java.util.ArrayList;
import java.util.List;

import com.oxygen.www.R;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.utils.ImageUtil;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotoSPageAdapter extends PagerAdapter {
	private Context c;
	private List<Photo> photos;
	private List<View> pictures;
	private ViewPager vp_group_photo;
	public PhotoSPageAdapter(Context c, List<Photo> photos,ViewPager vp_group_photo) {
		this.c = c;
		this.photos = photos;
		this.vp_group_photo = vp_group_photo;
		pictures = new ArrayList<View>();
		WindowManager manager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		for (int i = 0; i < photos.size(); i++) {
			ImageView iv = new ImageView(c);
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(width, width);
			iv.setLayoutParams(mParams);
//			iv.setScaleType(ImageView.ScaleType.FIT_XY); 
			ImageUtil.showImage(photos.get(i).getUrl(),iv, R.drawable.nopic);
			pictures.add(iv);
		}
	}

	@Override
	public int getCount() {
		if (photos == null) {
			return 0;
		}
		return photos.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(pictures.get(position));
		pictures.get(position).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vp_group_photo.setVisibility(View.GONE);
			}
		});
		return pictures.get(position);
	}
}
