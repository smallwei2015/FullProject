package com.blue.rchina.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.activity.TraveDetailActivity;
import com.blue.rchina.bean.Trave;

import java.util.List;

import static com.kuaiyou.g.a.getActivity;

/**
 * Created by cj on 2017/12/4.
 */

public class TraveAdapter extends RecyclerView.Adapter<TraveAdapter.Holder>{

    private List<Trave> datas;
    public Context context;

    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public TraveAdapter(List<Trave> datas) {
        this.datas=datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        View inflate = LayoutInflater.from(context).inflate(R.layout.trave_item, parent, false);

        return new Holder(inflate,viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.price.setText("现价只要998");

        /*if (listener != null) {
            holder.parent.setOnClickListener(listener);
        }*/
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TraveDetailActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),v,"trave").toBundle());
                }else {
                    context.startActivity(intent);
                }
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

    class Holder extends RecyclerView.ViewHolder{


        public TextView title;
        public TextView price;
        public View parent;
        public ImageView img;

        public Holder(View itemView, int type) {
            super(itemView);

            parent = itemView;
            title = ((TextView) itemView.findViewById(R.id.title));
            price = ((TextView) itemView.findViewById(R.id.price));
            img = ((ImageView) itemView.findViewById(R.id.img));
        }
    }
}
