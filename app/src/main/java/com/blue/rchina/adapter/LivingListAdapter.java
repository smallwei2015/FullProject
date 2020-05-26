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
import com.blue.rchina.activity.LiveCommentActivity;
import com.blue.rchina.bean.living.LivingInfo;
import com.blue.rchina.manager.xUtilsManager;
import com.vode.living.ui.LiveActivity;
import com.vode.living.ui.VodActivity;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by cj on 2018/9/11.
 */

public class LivingListAdapter extends RecyclerView.Adapter<LivingListAdapter.Holder> {

    List<LivingInfo> datas;
    Context context;

    View.OnClickListener onItemClick;
    public ImageOptions.Builder builder;

    public void setOnItemClick(View.OnClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public LivingListAdapter(Context context, List<LivingInfo> datas) {
        this.datas = datas;
        this.context = context;

        //设置加载过程中的图片
        //设置加载失败后的图片
        //设置使用缓存
        //设置显示圆形图片
        //设置支持gif
        builder = new ImageOptions.Builder()
                //设置加载过程中的图片
                .setLoadingDrawableId(R.color.lightGray)
                //设置加载失败后的图片
                .setFailureDrawableId(R.color.lightGray)
                //设置使用缓存
                .setUseMemCache(true)
                //设置显示圆形图片
                .setCircular(false)
                //设置支持gif
                .setIgnoreGif(false)
                .setCrop(true);


    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getLiveState();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.living_item_now, parent, false);
        /*if (viewType==1){
            view= LayoutInflater.from(context).inflate(R.layout.living_item_now, parent, false);
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.living_item_review, parent, false);
        }*/

        return new Holder(view,viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final LivingInfo livingInfo = datas.get(position);

        x.image().bind(holder.icon,livingInfo.getAnchor().getHeadIcon(),xUtilsManager.getCircleImageOption());
        holder.name.setText(livingInfo.getAnchor().getNickName());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LiveCommentActivity.class);
                intent.putExtra("data",livingInfo);
                context.startActivity(intent);
            }
        });

        if (getItemViewType(position)==1){
            holder.tag.setEnabled(true);
            holder.tag_text.setText("直播中");
            holder.title.setText(livingInfo.getLive().getChanneName());

            x.image().bind(holder.pre,livingInfo.getLive().getPicsrc(), builder.setImageScaleType(ImageView.ScaleType.CENTER_CROP).build());

            holder.pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (livingInfo.getLive() != null) {
                        Intent intent = new Intent(context, LiveActivity.class);
                        intent.putExtra("videoPath",livingInfo.getLive().getHttpPullUrl());

                        /*直播暂时无法获取长宽*/
                        //intent.putExtra("isLandscape",true);
                        context.startActivity(intent);
                    }


                }
            });
        }else if (getItemViewType(position)==2){
            holder.tag.setEnabled(false);
            holder.tag_text.setText("重播中");
            holder.title.setText(livingInfo.getLiveVideo().getChanneName());
            x.image().bind(holder.pre,livingInfo.getLiveVideo().getSnapshotUrl(), builder.setImageScaleType(ImageView.ScaleType.CENTER_CROP).build());

            holder.pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LivingInfo.LiveVideoBean liveVideo = livingInfo.getLiveVideo();
                    if (liveVideo !=null) {
                        Intent intent = new Intent(context, VodActivity.class);
                        intent.putExtra("videoPath", liveVideo.getVideoUrl());

                        intent.putExtra("isLandscape",liveVideo.getWidth()>liveVideo.getHeight());
                        context.startActivity(intent);
                    }

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder{

        View tag;
        TextView tag_text,title,name;
        ImageView pre,icon,comment;

        public Holder(View itemView,int type) {
            super(itemView);
            tag=itemView.findViewById(R.id.tag);
            tag_text= (TextView) itemView.findViewById(R.id.tag_text);
            title= (TextView) itemView.findViewById(R.id.title);
            pre= (ImageView) itemView.findViewById(R.id.preview_img);
            icon= (ImageView) itemView.findViewById(R.id.icon);
            name= (TextView) itemView.findViewById(R.id.name);
            comment= (ImageView) itemView.findViewById(R.id.comment);
        }
    }
}
