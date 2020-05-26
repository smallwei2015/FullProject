package com.blue.rchina.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.NoticeInfo;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.BasePopUpWindow;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class InfoActivity extends BaseActivity {


    @ViewInject(R.id.info_rec)
    RecyclerView rec;

    @ViewInject(R.id.ptrFrame)
    PtrClassicFrameLayout frame;


    @ViewInject(R.id.activity_info)
    View parent;


    private List<NoticeInfo> datas;
    private RecyclerView.Adapter<Holder> adapter;
    private View.OnClickListener listen;
    private int cPage=2;
    public NoticeInfo noticeInfo;
    public PopupWindow popupWindow;

    @Override
    public void initData() {
        super.initData();

        //fresh();

        /*if (noticeInfo != null) {
            datas.add(noticeInfo);
        }
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"我的消息",-1);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        header.setLastUpdateTimeKey(this.getClass().getName());
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                fresh();
            }
        });

        datas=new ArrayList<>();
        listen=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tag = (int) v.getTag();

                NoticeInfo noticeInfo = datas.get(tag);

                switch (v.getId()) {

                    case R.id.info_parent:
                        int type = noticeInfo.getType();

                        if (type==0){
                            /*Intent intent=new Intent(mActivity,InfoDetailActivity.class);
                            intent.putExtra("data",noticeInfo);
                            startActivity(intent);*/
                        }else if (type==1){
                            makeSureAddWine(noticeInfo);
                        }else if (type==2){
                            Intent intent=new Intent(mActivity,CouponActivity.class);
                            intent.putExtra("data",noticeInfo);
                            startActivity(intent);
                        }else if (type==3){
                            Intent intent=new Intent(mActivity,OrderDetailActivity.class);
                            intent.putExtra("data",noticeInfo);
                            startActivity(intent);
                        }else {

                        }

                        break;
                    case R.id.delete:


                        break;
                    case R.id.collect:


                        break;
                    case R.id.share:


                        break;
                }
            }
        };
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View inflate = LayoutInflater.from(mActivity).inflate(R.layout.info_item, parent, false);
                Holder holder = new Holder(inflate);

                x.view().inject(holder,inflate);
                return holder;
            }

            @Override
            public void onBindViewHolder(Holder holder, final int position) {

                NoticeInfo s = datas.get(position);

                holder.parent.setOnClickListener(listen);
                holder.parent.setTag(position);
                holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        PopupWindow pop=new BasePopUpWindow(mActivity);

                        View view= LayoutInflater.from(mActivity).inflate(R.layout.pop_info,null);

                        View delete = view.findViewById(R.id.delete);
                        delete.setTag(position);
                        delete.setOnClickListener(listen);
                        View share = view.findViewById(R.id.share);
                        share.setTag(position);
                        share.setOnClickListener(listen);

                        View collect = view.findViewById(R.id.collect);
                        collect.setTag(position);
                        collect.setOnClickListener(listen);


                        pop.setContentView(view);
                        pop.showAtLocation(view, Gravity.CENTER,0,0);

                        return false;
                    }
                });


                if (!TextUtils.isEmpty(s.getTitle())){
                    holder.title.setText(s.getTitle());
                }else {
                    holder.title.setText("");
                }

                if (!TextUtils.isEmpty(s.getContent())){
                    holder.content.setText(s.getContent());
                }else {
                    holder.content.setText("");
                }

                holder.time.setText("通知时间："+s.getDatetime());
                int type = s.getType();
                if (type ==0) {
                    holder.state.setText("系统通知");
                    holder.icon.setImageResource(R.drawable.system_icon);
                }else if (type==1){
                    holder.state.setText("加酒通知");
                    holder.icon.setImageResource(R.drawable.system_icon);
                }else if (type==2){
                    holder.state.setText("优惠通知");
                    holder.icon.setImageResource(R.drawable.system_icon);
                }else if (type==3){
                    holder.state.setText("物流通知");
                    holder.icon.setImageResource(R.drawable.system_icon);
                }else {
                    holder.state.setText("其他通知");
                }

                if (s.getReadState()==0){
                    holder.read.setVisibility(View.GONE);
                }else {
                    holder.read.setVisibility(View.VISIBLE);
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


        isNodata(true);
    }

    private void makeSureAddWine(NoticeInfo noticeInfo) {

        popupWindow = new BasePopUpWindow(mActivity);
        popupWindow.setWidth((int) getResources().getDimension(R.dimen.dp300));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        View view=LayoutInflater.from(mActivity).inflate(R.layout.sure_add,null);


        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        x.image().bind(icon,noticeInfo.getHeadIcon(), xUtilsManager.getCircleImageOption());

        ((TextView) view.findViewById(R.id.title)).setText(noticeInfo.getTitle());

        String content = noticeInfo.getContent();
        TextView conTV = (TextView) view.findViewById(R.id.content);
        if (!TextUtils.isEmpty(content)) {
            conTV.setText(content);
        }else {
            conTV.setText("代理商未添加附加信息");
        }


        ((TextView) view.findViewById(R.id.date)).setText("通知加酒时间："+noticeInfo.getAddDate());

        view.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast("确认加酒成功");
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xff000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(parent,Gravity.CENTER,0,0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        x.view().inject(this);

        initView();
        initData();
    }

    private void fresh(){
        getInfo(0,1);
    }

    private void loadMore(){
        getInfo(1,cPage);
    }

    private void getInfo(final int type, int page){
        RequestParams entity = new RequestParams(UrlUtils.achieveNoticeList);

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");


        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){

                    List<NoticeInfo> noticeInfos = JSON.parseArray(netData.getInfo(), NoticeInfo.class);

                    if (type==0){
                        datas.clear();


                       /* for (int i = 0; i < 4; i++) {
                            NoticeInfo e = new NoticeInfo();
                            e.setType(i);
                            e.setContent("卫星多种地图形式供开发者选择，无论基于哪种平台，都可以通过高德开放平台提供的API和SDK轻松的完成地图的构建工作。同时我们还提供强大的地图再开发能力，全面的地图数据支持，离线在线两种使用方式，多种地图交互模式，满足各个场景下对地图的需求");
                            e.setDatetime("2017.7.29");
                            datas.add(e);
                        }*/
                        datas.addAll(noticeInfos);

                        if (datas.size()==0){
                            isNodata(true);
                        }else {
                            isNodata(false);
                        }
                    }


                    adapter.notifyDataSetChanged();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //UIUtils.showToast("网络加载失败");
                UIUtils.showToast("暂无消息");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                frame.refreshComplete();
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.info_parent)
        View parent;
        @ViewInject(R.id.info_content)
        TextView content;
        @ViewInject(R.id.info_time)
        TextView time;
        @ViewInject(R.id.info_state)
        TextView state;
        @ViewInject(R.id.info_icon)
        ImageView icon;
        @ViewInject(R.id.info_read)
        View read;
        @ViewInject(R.id.info_title)
        TextView title;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
