package com.blue.rchina.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.blue.rchina.R;
import com.blue.rchina.adapter.InteractAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.DataWrap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class InteractCommentDetailActivity extends BaseActivity {

    @ViewInject(R.id.recycler)
    RecyclerView recycler;

    @ViewInject(R.id.fresh_frame)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.comment)
    View comment;
    private InteractAdapter adapter;
    private List<DataWrap> items;
    private LinearLayoutManager manager;


    @Override
    public void initView() {

        super.initView();
        initTop(R.mipmap.left_white,"回复详情",-1);
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
                    }
                }, 1500);
            }
        });



        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View inflate = LayoutInflater.from(InteractCommentDetailActivity.this).inflate(R.layout.interact_comment_pop, null);
                final AlertDialog.Builder builder=new AlertDialog.Builder(InteractCommentDetailActivity.this);

                builder.setView(inflate);

                final AlertDialog dialog = builder.create();
                dialog.show();

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
                        String string = editText.getText().toString();
                        dialog.dismiss();
                    }
                });
            }
        });

        manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        items=new ArrayList<>();
        adapter=new InteractAdapter(items);

        recycler.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();
        DataWrap data = (DataWrap) getIntent().getSerializableExtra("data");
        items.add(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_comment_detail);
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
