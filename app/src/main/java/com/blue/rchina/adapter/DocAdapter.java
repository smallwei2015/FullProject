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
import com.blue.rchina.activity.DocDetailActivity;
import com.blue.rchina.bean.DocBean;
import com.blue.rchina.utils.FileUtils;

import java.util.List;

/**
 * Created by Administrator on 2020/5/26.
 *
 * @copyright 北京融城互联
 */
public class DocAdapter extends RecyclerView.Adapter<DocAdapter.Holder> {
    private Context mContext;
    private List<DocBean> datas;

    public DocAdapter(Context mContext, List<DocBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.doc_item, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final DocBean docBean = datas.get(position);

        holder.title.setText(docBean.getTitle());

        holder.des.setText(docBean.getCreateTime()+"\t\t文件大小-"+ FileUtils.getFormatSize(docBean.getFileSize()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DocDetailActivity.class);
                intent.putExtra("data",docBean);
                mContext.startActivity(intent);
            }
        });

        if (docBean.getPrice()>0){
            holder.price.setText("商品价格:"+docBean.getPrice());
        }else {
            holder.price.setText("免费文档");
        }
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder {

        public TextView title,des,price;
        public ImageView icon;

        public Holder(View itemView) {
            super(itemView);
            icon = ((ImageView) itemView.findViewById(R.id.media_bg));
            title = ((TextView) itemView.findViewById(R.id.media_title));
            des = ((TextView) itemView.findViewById(R.id.media_des));
            price= (TextView) itemView.findViewById(R.id.price);
        }
    }
}
