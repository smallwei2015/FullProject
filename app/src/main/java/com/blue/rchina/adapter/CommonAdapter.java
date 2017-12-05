package com.blue.rchina.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	public CommonAdapter(Context context, int layoutId)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas==null?0:mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public List<T> getList(){
		return mDatas;
	}

	public void setList(List<T> list){
		this.mDatas=list;
	}

	 /**
	  * 获取holder中保存的组件
	  * 
	  * @param position 位置
	  * 
	  */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}

	/**
	 * 设置数据.通过holder.xxx的方式设置数据
	 * @param holder 
	 * @param item Bean对象
	 */
	public abstract void convert(ViewHolder holder, T item,int position);

}
