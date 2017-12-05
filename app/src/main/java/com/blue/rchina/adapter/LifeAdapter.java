package com.blue.rchina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.views.NoScrollGridView;

import java.util.List;

/**
 * Created by cj on 2017/10/31.
 */

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.Holder> {


    private Context context;
    private List<Channel> items;

    public LifeAdapter(List<Channel> items) {
        this.items=items;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.life_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Channel channel = items.get(position);
        if (channel!=null) {
            if (!TextUtils.isEmpty(channel.getTitle())) {
                holder.title.setText(channel.getTitle());
            }else {
                holder.title.setVisibility(View.GONE);
            }

            LifeGridAdapter adapter = new LifeGridAdapter(channel.getSons(), context);
            holder.grid.setAdapter(adapter);

            if (position==items.size()-1){
                holder.divider.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView title;
        NoScrollGridView grid;
        View divider;

        public Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            grid = (NoScrollGridView) itemView.findViewById(R.id.grid);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}


