package com.blue.rchina.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.activity.FragmentHolderActivity;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.utils.xUtilsImageUtils;

import java.util.List;

import static com.blue.rchina.activity.FragmentHolderActivity.FLAG_LIFE;
import static com.blue.rchina.activity.FragmentHolderActivity.FLAG_LIFE_OUTLINK;

/**
 * Created by cj on 2017/3/9.
 */

public class LifeGridAdapter extends BaseAdapter {

    private List<Channel> items;
    private Context context;
    public Intent intent;

    public LifeGridAdapter(List<Channel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.life_subitem, parent, false);
        }
        final Channel lifeSubitem = items.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int channelType = lifeSubitem.getChannelType();

                switch (channelType){
                    case 0:

                        break;
                    case 1:
                        Intent intent= new Intent(context, FragmentHolderActivity.class);
                        intent.putExtra("data",lifeSubitem);
                        intent.putExtra("flag",FLAG_LIFE_OUTLINK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case 2:

                        break;
                    case 3:
                        intent=new Intent(context, FragmentHolderActivity.class);
                        intent.putExtra("data",lifeSubitem);
                        intent.putExtra("flag",FLAG_LIFE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                }
            }
        });
        ImageView icon = (ImageView) convertView.findViewById(R.id.life_icon);
        TextView title = (TextView) convertView.findViewById(R.id.life_title);

        title.setText(lifeSubitem.getTitle());
        xUtilsImageUtils.display(icon,lifeSubitem.getShowicon());


        return convertView;
    }
}
