package com.oxygen.www.module.team.adapter;

import java.util.List;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.utils.ImageUtil;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GroupPhotosAdapter extends BaseAdapter {
	
	private Context c;
	private List<Photo> photos;
	private LayoutInflater inflater;
	private ViewPager vp_group_photo;
	private int width;
	public GroupPhotosAdapter(Context c,List<Photo> photos,ViewPager vp_group_photo){
		this.c = c;
		this.photos = photos;
		this.vp_group_photo = vp_group_photo;
		inflater = LayoutInflater.from(c);
		WindowManager manager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		width = manager.getDefaultDisplay().getWidth();
	}
	@Override
	public int getCount() {
		if(photos == null){
			return 0;
		}
		return photos.size();
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
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_groupphoto, null);
			holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width/4, width/4);
		holder.iv_photo.setLayoutParams(layoutParams);
		holder.iv_photo.setScaleType(ScaleType.FIT_XY);
		ImageUtil.showImage(photos.get(position).getUrl()+Constants.qiniu_photo_business, holder.iv_photo, R.drawable.nopic);
		holder.iv_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vp_group_photo.setVisibility(View.VISIBLE);
				PhotoSPageAdapter photoSPageAdapter = new PhotoSPageAdapter(c,photos,vp_group_photo);
				vp_group_photo.setAdapter(photoSPageAdapter);
				vp_group_photo.setCurrentItem(position);
//				Intent bigPhotoIntent = new Intent(c,BigPhotoActivity.class);
//				bigPhotoIntent.putExtra("photoUrl", photos.get(position).getUrl());
//				c.startActivity(bigPhotoIntent);
			}
		});
		return convertView;
	}
	public  class ViewHolder {
		public ImageView iv_photo;
	}
	
}
