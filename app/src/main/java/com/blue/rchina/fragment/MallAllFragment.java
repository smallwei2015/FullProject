package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.activity.MallActivity;
import com.blue.rchina.activity.TraveActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.utils.xUtilsImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_mall_all)
public class MallAllFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.rec_province)
    RecyclerView rec_p;
    @ViewInject(R.id.rec_city)
    RecyclerView rec_c;
    public RecyclerView.Adapter<PHolder> pAdapter;

    List<AreaInfo> pdatas;
    List<AreaInfo> cdatas;
    public RecyclerView.Adapter<CHolder> cAdapter;
    public GridLayoutManager cManager;
    private int selectedPos=0;
    public int flag=0;

    public MallAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=x.view().inject(this,inflater,container);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        Bundle arguments = getArguments();
        if (arguments != null) {
            flag = arguments.getInt("flag",0);
        }

        pdatas=new ArrayList<>();
        cdatas=new ArrayList<>();
        getCityList();
    }

    private void getCityList() {
        x.http().post(new RequestParams(UrlUtils.N_achieveAreaStructure), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    List<AreaInfo> areaInfos = JSON.parseArray(netData.getInfo(), AreaInfo.class);

                    if (areaInfos != null&&areaInfos.size()>0) {
                        pdatas.addAll(areaInfos);
                    }

                    pAdapter.notifyDataSetChanged();
                    cdatas.addAll(pdatas.get(selectedPos).getSons());
                    cAdapter.notifyDataSetChanged();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络连接失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        rec_p.setLayoutManager(new LinearLayoutManager(mActivity));
        cManager = new GridLayoutManager(mActivity,3);
        cManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        rec_c.setLayoutManager(cManager);



        pAdapter = new RecyclerView.Adapter<PHolder>() {
            @Override
            public PHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(mActivity).inflate(R.layout.mall_city_item,parent,false);

                return new PHolder(view);
            }

            @Override
            public void onBindViewHolder(PHolder holder, int position) {
                AreaInfo areaInfo = pdatas.get(position);

                if (selectedPos==position) {
                    holder.parent.setSelected(true);
                }else{
                    holder.parent.setSelected(false);
                }
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(MallAllFragment.this);
                holder.name.setText(areaInfo.getAreaName());
            }


            @Override
            public int getItemCount() {
                if (pdatas != null) {
                    return pdatas.size();
                }
                return 0;
            }
        };
        rec_p.setAdapter(pAdapter);

        cAdapter = new RecyclerView.Adapter<CHolder>() {
            @Override
            public CHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.mall_city, parent, false);
                return new CHolder(inflate);
            }

            @Override
            public void onBindViewHolder(CHolder holder, int position) {
                holder.parent.setTag(position);
                holder.parent.setOnClickListener(MallAllFragment.this);
                AreaInfo areaInfo = cdatas.get(position);
                holder.name.setText(areaInfo.getAreaName());
                if (TextUtils.isEmpty(areaInfo.getAreaIcon())){
                    holder.icon.setImageResource(R.mipmap.mall);
                }else {
                    xUtilsImageUtils.displayCity(holder.icon, areaInfo.getAreaIcon());
                }
            }

            @Override
            public int getItemCount() {
                if (cdatas != null) {
                    return cdatas.size();
                }
                return 0;
            }
        };
        rec_c.setAdapter(cAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.city){

            if (flag==1) {
                Intent intent = new Intent(mActivity, TraveActivity.class);
                AreaInfo areaInfo = cdatas.get(((int) v.getTag()));
                intent.putExtra("data", areaInfo);
                startActivity(intent);
            }else {
                Intent intent = new Intent(mActivity, MallActivity.class);
                AreaInfo areaInfo = cdatas.get(((int) v.getTag()));
                intent.putExtra("data", areaInfo);
                startActivity(intent);
            }
        }else {
            selectedPos = ((int) v.getTag());
            pAdapter.notifyDataSetChanged();

            cdatas.clear();
            cdatas.addAll(pdatas.get(selectedPos).getSons());
            cAdapter.notifyDataSetChanged();
        }
    }

    class PHolder extends RecyclerView.ViewHolder{
        View parent;
        public TextView name;

        public PHolder(View itemView) {
            super(itemView);
            parent=itemView;
            name = ((TextView) itemView.findViewById(R.id.mall_city_name));
        }
    }
    class CHolder extends RecyclerView.ViewHolder{

        public ImageView icon;
        public TextView name;
        public View parent;

        public CHolder(View itemView) {
            super(itemView);

            parent = itemView;
            icon = ((ImageView) itemView.findViewById(R.id.city_icon));
            name = ((TextView) itemView.findViewById(R.id.city_name));
        }
    }
}
