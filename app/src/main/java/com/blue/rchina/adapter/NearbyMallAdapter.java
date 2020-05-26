package com.blue.rchina.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.activity.MallDetailActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.GoodsKind;
import com.blue.rchina.utils.xUtilsImageUtils;

import java.util.List;

/**
 * Created by cj on 2018/5/25.
 */

public class NearbyMallAdapter extends RecyclerView.Adapter<NearbyMallAdapter.Holder> implements View.OnClickListener {


    private List<DataWrap> datas;
    private Context mContext;
    private View.OnClickListener listener;


    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public NearbyMallAdapter(List<DataWrap> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        switch (viewType) {
            case 0:
                inflate = LayoutInflater.from(mContext).inflate(R.layout.nearby_mall_kind, parent, false);
                break;
            case 1:
                inflate = LayoutInflater.from(mContext).inflate(R.layout.nearby_mall_goods, parent, false);
                break;
            case 2:
                inflate = LayoutInflater.from(mContext).inflate(R.layout.nearby_mall_title, parent, false);
                break;
            default:
                inflate = LayoutInflater.from(mContext).inflate(R.layout.nearby_mall_goods, parent, false);
                break;
        }
        return new Holder(inflate, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        DataWrap dataWrap = datas.get(position);
        switch (getItemViewType(position)) {
            case 0:
                GoodsKind.InfoBean data0 = (GoodsKind.InfoBean) dataWrap.getData();
                holder.title.setText(data0.getTitle());

                holder.title.setEnabled(dataWrap.getState() == 0);
                holder.title.setTag(position);
                if (listener != null) {
                    holder.title.setOnClickListener(listener);
                }
                break;
            case 1:

                Goods data1 = (Goods) dataWrap.getData();

                holder.name.setText(data1.getTitle());
                xUtilsImageUtils.display(holder.icon,data1.getPicsrc());
                holder.icon.setTag(data1);
                holder.icon.setOnClickListener(this);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon:
                Goods tag1 = (Goods) v.getTag();

                Intent intent=new Intent(mContext, MallDetailActivity.class);
                intent.putExtra("data",tag1);
                intent.putExtra("isFromMall",false);
                mContext.startActivity(intent);
                break;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public TextView name;

        public Holder(View itemView, int type) {
            super(itemView);

            switch (type) {
                case 0:
                    title = (TextView) itemView.findViewById(R.id.title);
                    break;
                case 1:
                    icon = ((ImageView) itemView.findViewById(R.id.icon));
                    name = ((TextView) itemView.findViewById(R.id.name));
                    break;
            }

        }
    }
}
