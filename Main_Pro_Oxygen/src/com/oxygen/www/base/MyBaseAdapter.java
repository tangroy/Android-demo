package com.oxygen.www.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int mLayoutId;
	
	public MyBaseAdapter(Context context,List<T> datas,int layoutId){
		this.mContext = context;
		this.mDatas = datas;
		this.mLayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseViewHolder holder = BaseViewHolder.get(mContext, convertView, parent, mLayoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public abstract void convert(BaseViewHolder holder,T t);
}
