package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.enter.Channel;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_open_channel)
public class OpenChannelActivity extends BaseActivity {

    @ViewInject(R.id.rec)
    RecyclerView rec;

    private List<DataWrap> datas;
    public RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initData();
        initView();

    }

    @Override
    public void initData() {
        super.initData();
        datas=new ArrayList<>();
    }

    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"栏目选择","确定");
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.open_channel_item, parent, false);

                return new Holder(inflate);
            }

            @Override
            public void onBindViewHolder(Holder holder, final int position) {

                final DataWrap data = datas.get(position);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (position==0||position==4){
                            UIUtils.showToast("首页和商城栏目必选");
                        }else {
                            if (data.getState() == 0) {
                                data.setState(1);
                            } else {
                                data.setState(0);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
                Channel channel = (Channel) data.getData();
                holder.name.setText(channel.getName());

                if (data.getState()==0) {
                    holder.check.setChecked(false);
                }else {
                    holder.check.setChecked(true);
                }
            }

            @Override
            public int getItemCount() {
                if (datas != null) {
                    return datas.size();
                }
                return 0;
            }
        };
        rec.setAdapter(adapter);

        loadChannel();
    }

    private void loadChannel() {
        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_achieveChannelList);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    List<Channel> channels = JSON.parseArray(netData.getInfo(), Channel.class);

                    for (int i = 0; i < channels.size(); i++) {
                        DataWrap e = new DataWrap();

                        Channel data = channels.get(i);
                        if (data.getName().equals("首页")||data.getName().equals("商城")){
                            e.setState(1);
                        }else {
                            e.setState(0);
                        }
                        e.setData(data);
                        datas.add(e);
                    }

                    adapter.notifyDataSetChanged();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("服务器异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
            }
        });
    }

    @Override
    public void onRightTextClick(View v) {
        super.onRightTextClick(v);

        Intent data = new Intent();


        List<Channel> channels=new ArrayList<>();

         /*获取选择项*/
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getState()!=0){
                channels.add((Channel) datas.get(i).getData());
            }
        }


        if (channels.size()<4){
            UIUtils.showToast("至少选择四个栏目");
            return;
        }
        data.putExtra("channel", (Serializable) channels);
        setResult(200, data);

        finish();
    }

    class Holder extends RecyclerView.ViewHolder{

        public TextView name;
        public CheckBox check;

        public Holder(View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));
            check = ((CheckBox) itemView.findViewById(R.id.check));
        }
    }


}
