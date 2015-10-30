package com.oxygen.www.module.sport.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oxygen.www.R;
import com.oxygen.www.enties.Photo;
import com.oxygen.www.module.team.adapter.PhotoSPageAdapter;
import com.oxygen.www.utils.ImageUtil;



public class EventDetailMomentPhotosAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context c;
	private ArrayList<Photo> photos;
	private ViewPager vp_group_photo;
	public EventDetailMomentPhotosAdapter(Context c,ArrayList<Photo> photos,ViewPager vp_group_photo){
		inflater = LayoutInflater.from(c);
		this.c = c;
		this.photos = photos;
		this.vp_group_photo = vp_group_photo;
	}
	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return photos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_moment_photos,
					arg2, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String url = photos.get(arg0).getUrl();
		ImageUtil.showImage(url, holder.image,R.drawable.nopic);
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vp_group_photo.setVisibility(View.VISIBLE);
				PhotoSPageAdapter photoSPageAdapter = new PhotoSPageAdapter(c,photos,vp_group_photo);
				vp_group_photo.setAdapter(photoSPageAdapter);
				vp_group_photo.setCurrentItem(arg0);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}
