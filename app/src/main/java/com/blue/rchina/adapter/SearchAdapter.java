package com.blue.rchina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.SearchItem;

import org.xutils.x;

import java.util.List;

/**
 * Created by cj on 2018/8/21.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    Context mContext;
    List<SearchItem> datas;
    SearchListener listener;

    public interface SearchListener{
        void itemClick(SearchItem item);
        void bottomClick();
    }

    public SearchAdapter(Context mContext, List<SearchItem> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    public void setListener(SearchListener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Holder holder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        switch (viewType) {

            case 0:
                view = layoutInflater.inflate(R.layout.search_item_title, parent, false);
                holder = new Holder(view, viewType);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.search_item_text, parent, false);
                holder = new Holder(view, viewType);
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.search_item_tips, parent, false);
                holder = new Holder(view, viewType);
                break;
            case 3:
                view = layoutInflater.inflate(R.layout.search_item_tips, parent, false);
                holder = new Holder(view, viewType);
                break;
        }

        x.view().inject(holder, view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SearchItem searchItem = datas.get(position);

        int type = searchItem.getType();

        switch (type) {
            case 0:
                holder.title_tv.setTag(searchItem);
                holder.title_tv.setText(searchItem.getTitle());
                /*if (listener != null) {
                    holder.title_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SearchItem tag = (SearchItem) v.getTag();
                            listener.itemClick(tag);
                        }
                    });
                }*/

                break;
            case 1:
                holder.item_tv.setTag(searchItem);
                holder.item_tv.setText(searchItem.getTitle());
                if (listener != null) {
                    holder.item_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SearchItem tag = (SearchItem) v.getTag();
                            listener.itemClick(tag);
                        }
                    });
                }
                break;
            case 2:
                holder.tips.setText(searchItem.getTitle());

                if (listener != null) {
                    holder.tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.bottomClick();
                        }
                    });
                }

                break;
            case 3:
                holder.tips.setText(searchItem.getTitle());
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

        TextView item_tv;
        TextView title_tv;
        TextView tips;

        public Holder(View view, int viewType) {
            super(view);

            switch (viewType) {
                case 0:
                    title_tv = (TextView) view.findViewById(R.id.search_title_tv);
                    break;
                case 1:
                    item_tv = (TextView) view.findViewById(R.id.search_item_tv);
                    break;
                case 2:
                    tips = (TextView) view.findViewById(R.id.tips);
                    break;
                case 3:
                    tips = (TextView) view.findViewById(R.id.tips);
                    break;
            }
        }
    }
}
