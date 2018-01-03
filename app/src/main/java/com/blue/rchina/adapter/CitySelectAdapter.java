package com.blue.rchina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.DataWrap;

import java.util.List;

/**
 * Created by cj on 2017/12/22.
 */

public class CitySelectAdapter extends RecyclerView.Adapter<CitySelectAdapter.Holder> {

    public List<DataWrap> datas;
    public Context context;
    private View.OnClickListener listener;
    public int flag;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public CitySelectAdapter(List<DataWrap> datas,int flag) {
        this.datas=datas;
        this.flag = flag;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.city_head, parent, false);

                break;
            case 1:
                view = inflater.inflate(R.layout.city_item, parent, false);
                break;
        }
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        DataWrap dataWrap = datas.get(position);
        final AreaInfo data = (AreaInfo) dataWrap.getData();

        switch (dataWrap.getType()) {
            case 0:
                if (flag==1) {
                    holder.name.setText("切换到\"" + data.getAreaName() + "\"");
                }else {
                    holder.name.setText("选择\"" + data.getAreaName() + "\"");
                }
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 1:
                holder.name.setText(data.getAreaName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();

    }

    class Holder extends RecyclerView.ViewHolder {

        public TextView name;
        public View parent;

        public Holder(View itemView, int type) {
            super(itemView);
            switch (type) {
                case 0:
                    parent = itemView;
                    name = (TextView) itemView.findViewById(R.id.city_name);
                    break;
                case 1:
                    parent = itemView;
                    name = ((TextView) itemView.findViewById(R.id.city_name));
                    break;
            }
        }
    }
}
