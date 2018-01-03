package com.blue.rchina.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Uninon;
import com.blue.rchina.utils.UIUtils;

import java.util.List;

import static com.blue.rchina.manager.UserManager.action_cityChange;

/**
 * Created by cj on 2017/12/26.
 */

public class UnionAdapter extends RecyclerView.Adapter<UnionAdapter.Holder> implements View.OnClickListener, View.OnLongClickListener {


    public List<DataWrap> datas;
    public Context context;
    /*0联盟1常用城市2常用联盟，3选择添加联盟*/
    public int flag;
    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public UnionAdapter(List<DataWrap> datas, int flag) {

        this.datas = datas;
        this.flag = flag;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context==null) {
            context = parent.getContext();
        }

        View inflate = LayoutInflater.from(context).inflate(R.layout.uninon_item, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        DataWrap dataWrap = datas.get(position);
        Uninon uninon = ((Uninon) dataWrap.getData());

        holder.title.setTag(position);
        holder.title.setOnClickListener(this);

        switch (flag){
            case 0:
                holder.title.setText(uninon.getUnionName());
                break;
            case 1:
            case 2:
                holder.title.setOnLongClickListener(this);
                if (flag == 1) {
                    holder.title.setText(uninon.getAreaName());
                } else {
                    holder.title.setText(uninon.getUnionName());
                }

                if (dataWrap.getState() == 0) {
                    holder.del.setVisibility(View.GONE);
                } else {
                    holder.del.setVisibility(View.VISIBLE);
                    holder.del.setTag(uninon);
                    holder.del.setOnClickListener(this);
                }

                break;
            case 3:
                holder.title.setText(uninon.getUnionName());
                break;
            default:

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_title:
                int pPos = (int) v.getTag();
                DataWrap pData = datas.get(pPos);
                if (pData.getState()!=0) {

                    /*设置所有隐藏*/
                    for (int i = 0; i < datas.size(); i++) {
                        datas.get(i).setState(0);
                    }
                    notifyDataSetChanged();
                }else {
                    /*切换城市*/
                    Uninon tag = (Uninon) pData.getData();

                    if (!TextUtils.isEmpty(tag.getUnionName())) {
                        tag.setIsOperate(1);
                        tag.setAreaIcon(tag.getUnionIcon());
                    }
                    if (tag.getIsOperate() == 1) {

                        if (flag!=3) {
                            Intent intent = new Intent(action_cityChange);
                            intent.putExtra("area", tag);
                            context.sendBroadcast(intent);
                            ((Activity) context).finish();
                        }else {
                            listener.onClick(v);
                        }
                    } else {
                        UIUtils.showToast("该城市还未开通，无法选择");
                    }
                }
                break;
            case R.id.tag_del:
                listener.onClick(v);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        RotateAnimation rotate = new RotateAnimation(-2f, 2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(100);//设置动画持续周期
        rotate.setRepeatCount(4);//设置重复次数
        rotate.setFillAfter(false);//动画执行完后是否停留在执行完的状态

        /*for (int i = 0; i < flow.getChildCount(); i++) {
            View childAt = flow.getChildAt(i);
            (childAt.findViewById(R.id.tag_del)).setVisibility(View.VISIBLE);
            childAt.startAnimation(rotate);

        }*/
        v.startAnimation(rotate);

        /*设置所有显示*/
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setState(1);
        }
        notifyDataSetChanged();

        return true;
    }

    class Holder extends RecyclerView.ViewHolder{

        public View del;
        public TextView title;
        public RelativeLayout parent;

        public Holder(View itemView) {
            super(itemView);
            parent = (RelativeLayout) itemView.findViewById(R.id.parent);
            del = itemView.findViewById(R.id.tag_del);
            title = ((TextView) itemView.findViewById(R.id.tab_title));
        }
    }
}
