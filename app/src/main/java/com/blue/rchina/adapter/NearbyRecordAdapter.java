package com.blue.rchina.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.activity.NewsKindImageActivity;
import com.blue.rchina.bean.NearbyRecord;
import com.blue.rchina.bean.Size;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.xUtilsImageUtils;
import com.blue.rchina.views.NoScrollGridView;

import java.util.List;

/**
 * Created by cj on 2018/1/19.
 */

public class NearbyRecordAdapter extends RecyclerView.Adapter<NearbyRecordAdapter.Holder> {


    public Context context;
    public List<NearbyRecord> datas;

    public NearbyRecordAdapter(List<NearbyRecord> datas) {
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        View inflate = LayoutInflater.from(context).inflate(R.layout.nearby_record, parent, false);

        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {


        final NearbyRecord nearbyRecord = datas.get(position);

        holder.content.setText(nearbyRecord.getContent());
        holder.content.setTextSize(17);
        holder.name.setText(nearbyRecord.getNickName());
        holder.data.setText(nearbyRecord.getDatetime());
        holder.delete.setVisibility(View.GONE);

        xUtilsImageUtils.display(holder.icon, nearbyRecord.getHeadIcon());

        final List<String> manyPic = nearbyRecord.getManyPic();
        final List<Size> picsize = nearbyRecord.getPicsize();
        holder.grid.setNumColumns((picsize.size()>0&&picsize.size()<3)?picsize.size():3);
        holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, NewsKindImageActivity.class);
                intent.putExtra("data", nearbyRecord);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        holder.grid.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                if (manyPic != null) {
                    return manyPic.size();
                }
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return manyPic.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.interact_grid_item, parent, false);
                }

                int size = picsize.size();

                ImageView icon = (ImageView) convertView.findViewById(R.id.image);
                int screenW = UIUtils.getWindowWidth((Activity) context);

                ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();

                if (size==1){
                    layoutParams.width = (int) (screenW / 2);
                    Size size1 = picsize.get(0);
                    layoutParams.height = (int) ((screenW / 2)*(size1.getHeight()/size1.getWidth()));
                } else if (size == 2) {
                    layoutParams.width = (int) (screenW / 3);
                    Size size1 = picsize.get(0);
                    layoutParams.height = (int) ((screenW / 3) * (size1.getHeight() / size1.getWidth()));
                }else {

                    layoutParams.width = (int) (screenW / 4);
                    layoutParams.height = (int) (screenW / 4);
                }

                icon.setLayoutParams(layoutParams);
                xUtilsImageUtils.display(icon, manyPic.get(position));
                return convertView;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView content, name, data, delete;
        ImageView icon;
        NoScrollGridView grid;

        public Holder(View itemView) {
            super(itemView);

            content = ((TextView) itemView.findViewById(R.id.record_content));
            name = ((TextView) itemView.findViewById(R.id.record_nickname));
            data = (TextView) itemView.findViewById(R.id.record_date);
            delete = (TextView) itemView.findViewById(R.id.record_delete);

            icon = (ImageView) itemView.findViewById(R.id.record_icon);
            grid = (NoScrollGridView) itemView.findViewById(R.id.record_grid);
        }
    }
}
