package com.blue.rchina.adapter;

/**
 * Created by cj on 2017/9/1.
 */

import android.content.Context;

import com.blue.rchina.R;
import com.blue.rchina.bean.MenuItem;

import java.util.List;


public class MenuAdapter extends CommonAdapter<MenuItem> {


    public MenuAdapter(Context context,List<MenuItem> datas) {
        super(context, datas, R.layout.item_menu);
    }

    @Override
    public void convert(ViewHolder holder, MenuItem item,int position) {
        holder.setText(R.id.tv_name, item.getItemName());
        holder.setImageResource(R.id.iv_icon, item.getIconRes());
        if(0!=item.getMoreRes())//有更多的图标
            holder.setImageResource(R.id.iv_more, item.getMoreRes());
    }
}

