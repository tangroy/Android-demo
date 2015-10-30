package com.oxygen.www.base;

import android.content.Context;
import android.provider.Telephony.Sms.Conversations;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseViewHolder {

	private SparseArray<View> mViews;
	private int mposition;
	private View mConvertView;
	
	public BaseViewHolder(Context context,ViewGroup parent,int layoutId,int position){
		this.mposition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	public static BaseViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView == null){
			return new BaseViewHolder(context, parent, layoutId, position);
		}else{
			BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
			holder.mposition = position;
			return holder;
		}
	}
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view == null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
	public View getConvertView(){
		return mConvertView;
	}
}
