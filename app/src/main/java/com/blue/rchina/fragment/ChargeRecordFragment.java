package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.activity.PayActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.ChargeInfo;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_charge_record)
public class ChargeRecordFragment extends BaseFragment {


    @ViewInject(R.id.rec)
    RecyclerView rec;
    @ViewInject(R.id.ptr)
    PtrClassicFrameLayout ptr;
    public int flag;
    private List<ChargeInfo> datas;
    public RecyclerView.Adapter<Holder> adapter;
    private View.OnClickListener listener;
    private int cPager=1;
    private boolean isloading=false;

    public ChargeRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inject = x.view().inject(this, inflater, container);

        initData();
        initView(inject);
        return inject;
    }


    @Override
    public void initData() {
        super.initData();

        listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                ChargeInfo chargeInfo = datas.get(tag);

                if (chargeInfo.getOrderFlag()==0){
                    Intent intent=new Intent(mActivity, PayActivity.class);

                    intent.putExtra("id", chargeInfo.getOrderNo());
                    intent.putExtra("money", chargeInfo.getMoneySum());
                    intent.putExtra("flag",1);
                    startActivity(intent);
                }
            }
        };
        flag = getArguments().getInt("flag",0);

        datas=new ArrayList<>();
        getChargeList(flag,1);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);

        ptr.setLastUpdateTimeKey(getClass().getName());
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getChargeList(flag,1);
            }
        });
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.charge_item, parent, false);

                return new Holder(inflate);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                ChargeInfo chargeInfo = datas.get(position);

                holder.balance.setText("金额："+chargeInfo.getMoneySum());
                holder.number.setText("订单号："+chargeInfo.getOrderNo());
                holder.date.setText(""+chargeInfo.getDatetime());

                holder.state.setTag(position);
                holder.state.setOnClickListener(listener);
                if (chargeInfo.getOrderFlag()==0) {
                    holder.state.setText("去完成");
                    holder.state.setSelected(true);
                    holder.state.setTextColor(getResources().getColor(R.color.white));
                }else {
                    holder.state.setText("已完成");
                    holder.state.setSelected(false);
                    holder.state.setTextColor(getResources().getColor(R.color.middle_gray));
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
        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rec.canScrollVertically(1) && datas.size() > 0) {
                    if (!isloading) {
                        getChargeList(flag, cPager+1);
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });
    }

    private void getChargeList(int flag, final int pager) {
        isHideLoading(false);
        isloading=true;

        RequestParams entity = new RequestParams(UrlUtils.N_getRechargeInfo);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("flag",flag+"");
        entity.addBodyParameter("page",pager+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    List<ChargeInfo> chargeInfos = JSON.parseArray(netData.getInfo(), ChargeInfo.class);
                    if (pager==1) {

                        cPager=1;
                        datas.clear();
                        datas.addAll(chargeInfos);
                    }else {
                        cPager++;
                        datas.addAll(chargeInfos);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptr.refreshComplete();
                isHideLoading(true);
                isloading=false;
                if (datas.size()>0) {
                    isNodata(false);
                }else {
                    isNodata(true);
                }
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder{

        public TextView number,date,balance,state;

        public Holder(View itemView) {
            super(itemView);

            number = ((TextView) itemView.findViewById(R.id.order_number));
            date = ((TextView) itemView.findViewById(R.id.order_date));
            balance = ((TextView) itemView.findViewById(R.id.order_balance));
            state = ((TextView) itemView.findViewById(R.id.order_state));


        }
    }
}
