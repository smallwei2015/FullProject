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
import com.blue.rchina.bean.enter.City;
import com.blue.rchina.bean.enter.County;
import com.blue.rchina.bean.enter.Province;
import com.blue.rchina.bean.enter.Town;

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

    public CitySelectAdapter(List<DataWrap> datas, int flag) {
        this.datas = datas;
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
            case 6:
                view = inflater.inflate(R.layout.city_head, parent, false);

                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                view = inflater.inflate(R.layout.city_item, parent, false);
                break;
        }
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        DataWrap dataWrap = datas.get(position);
        AreaInfo data;

        switch (dataWrap.getType()) {
            case 0:
                data = (AreaInfo) dataWrap.getData();
                if (flag == 1) {
                    holder.name.setText("切换到\"" + data.getAreaName() + "\"");
                }else {
                    holder.name.setText("选择\"" + data.getAreaName() + "\"");
                }
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 1:
                data = (AreaInfo) dataWrap.getData();
                holder.name.setText(data.getAreaName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 2:
                Province province = (Province) dataWrap.getData();
                holder.name.setText(province.getProvinceName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 3:
                City city = (City) dataWrap.getData();
                holder.name.setText(city.getCityName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 4:
                County county = (County) dataWrap.getData();
                holder.name.setText(county.getCountyName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 5:
                Town town = (Town) dataWrap.getData();
                holder.name.setText(town.getTownName());
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(listener);
                break;
            case 6:
                Object data1 = dataWrap.getData();
                if (data1 instanceof Province) {
                    holder.name.setText("开通\"" + ((Province) data1).getProvinceName() + "\"");
                }else if (data1 instanceof City){
                    holder.name.setText("开通\"" + ((City) data1).getCityName() + "\"");
                }else if (data1 instanceof County){
                    holder.name.setText("开通\"" + ((County) data1).getCountyName() + "\"");
                }else if (data1 instanceof Town){
                    holder.name.setText("开通\"" + ((Town) data1).getTownName() + "\"");
                }

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

            parent = itemView;
            name = ((TextView) itemView.findViewById(R.id.city_name));
        }
    }
}
