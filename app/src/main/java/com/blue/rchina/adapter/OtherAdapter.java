package com.blue.rchina.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.Channel;

import java.util.List;


public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<Channel> channelList;
	private TextView item_text;

	boolean isVisible = true;

	public int remove_position = -1;

	public OtherAdapter(Context context, List<Channel> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public Channel getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		Channel channel = getItem(position);
		item_text.setText(channel.getTitle());
		if (!isVisible && (position == -1 + channelList.size())){
			item_text.setText("");
		}
		if(remove_position == position){
			item_text.setText("");
		}
		return view;
	}
	

	public List<Channel> getChannnelLst() {
		return channelList;
	}
	

	public void addItem(Channel channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}


	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
	}


	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	public void setListDate(List<Channel> list) {
		channelList = list;
	}


	public boolean isVisible() {
		return isVisible;
	}
	

	public void setVisible(boolean visible) {
		isVisible = visible;
	}
}