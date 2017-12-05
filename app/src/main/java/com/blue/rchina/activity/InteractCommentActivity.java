package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.adapter.InteractAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Comment;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.Report;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class InteractCommentActivity extends BaseActivity {


    @ViewInject(R.id.recycler)
    RecyclerView recycler;

    @ViewInject(R.id.fresh_frame)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.comment)
    View comment;
    private LinearLayoutManager manager;
    private InteractAdapter adapter;
    private List<DataWrap> items;
    private Report report;
    private int curPage=1;
    private AlertDialog dialog;


    @Override
    public void initView() {
        super.initView();

        initTop(R.mipmap.left_white,"互动详情",-1);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrameLayout.setPtrHandler(new PtrHandler()
        {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header)
            {
                return !checkContentCanPullDown(frame, recycler, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame)
            {
                frame.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ptrFrameLayout.refreshComplete();
                        fresh();
                    }
                }, 1500);
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View inflate = LayoutInflater.from(InteractCommentActivity.this).inflate(R.layout.interact_comment_pop, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(InteractCommentActivity.this);
                builder.setView(inflate);
                dialog = builder.create();

                ((TextView) inflate.findViewById(R.id.title)).setText("添加评论：");
                final EditText editText = (EditText) inflate.findViewById(R.id.edit);

                inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                inflate.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = editText.getText().toString();
                        if (TextUtils.isEmpty(content)) {
                            UIUtils.showToast("内容不能为空");
                        } else {
                            if (UserManager.isLogin()) {
                                comment(content);
                            }else {
                                Intent intent = new Intent(InteractCommentActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);

        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.canScrollVertically(1)){
                    getComments(report.getReportId(),curPage+1);
                }
            }
        });
        items = new ArrayList<>();

        adapter = new InteractAdapter(items);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                DataWrap dataWrap = items.get(position);

                Report data = (Report) dataWrap.getData();
                Intent intent =null;

                if (v.getId()== R.id.interact_delete) {
                    delete(data);
                }
                switch (dataWrap.getType()) {
                    case 0:

                        break;
                    case 1:
                        intent=new Intent(InteractCommentActivity.this, InteractCommentDetailActivity.class);
                        intent.putExtra("data", dataWrap);
                        startActivity(intent);
                        break;
                    case 2:
                        /*在一种布局上去区分各种控件的点击事件*/
                        /*评论的界面*/
                        if (v.getId()==R.id.interact_bottom_kind2) {
                            intent = new Intent(InteractCommentActivity.this, InteractAgreeActivity.class);
                            intent.putExtra("data", data);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
        recycler.setAdapter(adapter);
    }

    private void delete(Report data) {

        isHideLoading(false);

        RequestParams entity = new RequestParams(UrlUtils.N_delReport);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId",data.getReportId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult()==200){
                    UIUtils.showToast("删除成功");

                    mActivity.sendAutoRefreshBroadcast(mActivity);
                    finish();
                }else {
                    UIUtils.showToast("删除失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("删除失败");
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

    private void comment(String text) {
        RequestParams entity = new RequestParams(UrlUtils.N_commentReport);

        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("content", text);
        entity.addBodyParameter("dataId",report.getReportId()+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);

                Integer code = object.getInteger("result");

                if (code==200){
                    UIUtils.showToast("评论成功");
                    fresh();

                }else {
                    UIUtils.showToast("评论失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
    public void initData() {
        super.initData();

        DataWrap data = (DataWrap) getIntent().getSerializableExtra("data");
        data.setType(2);
        items.add(0,data);

        report = (Report) data.getData();

        getComments(report.getReportId(),curPage);
    }
    private void fresh() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommentReport);
        entity.addBodyParameter("dataId",report.getReportId()+"");
        entity.addBodyParameter("page","1");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code==200){

                    /*第一条不移除，从第二条开始*/
                    for (int i = 1; i < items.size(); i++) {
                        items.remove(i);
                    }
                    List<Comment> comments = JSON.parseArray(object.getString("info"), Comment.class);

                    for (int i = 0; i < comments.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setData(comments.get(i));
                        e.setType(1);
                        items.add(e);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void getComments(int id,int page) {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveCommentReport);
        entity.addBodyParameter("dataId",id+"");
        entity.addBodyParameter("page",page+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");
                if (code==200){
                    List<Comment> comments = JSON.parseArray(object.getString("info"), Comment.class);

                    for (int i = 0; i < comments.size(); i++) {
                        DataWrap e = new DataWrap();
                        e.setData(comments.get(i));
                        e.setType(1);
                        items.add(e);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_comment);
        x.view().inject(this);
        initView();
        initData();
    }


    private boolean checkContentCanPullDown(PtrFrameLayout frame, View content, View header) {
        if(content instanceof RecyclerView) {
            /*这里就单独处理recycler*/
            RecyclerView content1 = (RecyclerView) content;
            return content1.canScrollVertically(-1);
        } else {
            return content.getScrollY() > 0;
        }
    }
}
