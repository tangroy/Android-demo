package com.oxygen.www.module.sport.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oxygen.www.R;
import com.oxygen.www.base.Constants;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.module.team.adapter.PhotoSPageAdapter;
import com.oxygen.www.utils.ImageUtil;

public class PhotosAdapter extends BaseAdapter {

	Context c;
	ArrayList<Photo> photos;
	ViewPager vp_group_photo;

	public PhotosAdapter(Context c, ArrayList<Photo> photos,ViewPager vp_group_photo) {
		this.c = c;
		this.photos = photos;
		this.vp_group_photo = vp_group_photo;
	}

	@Override
	public int getCount() {
		if (photos.size() > 5) {
			return 5;
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
		if (convertView == null) {
			convertView = View.inflate(c, R.layout.activity_showphotes, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.image_show);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageUtil.showImage(photos.get(position).getUrl()+Constants.qiniu_photo_business, holder.image,
				R.drawable.nopic);
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vp_group_photo.setVisibility(View.VISIBLE);
				PhotoSPageAdapter photoSPageAdapter = new PhotoSPageAdapter(c,photos,vp_group_photo);
				vp_group_photo.setAdapter(photoSPageAdapter);
				vp_group_photo.setCurrentItem(position);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}
