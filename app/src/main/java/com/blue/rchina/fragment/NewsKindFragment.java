package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.activity.NewsKindDetailActivty;
import com.blue.rchina.adapter.NewsAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsKindFragment extends BaseFragment {


    private PtrFrameLayout ptrFrame;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private NewsAdapter adapter;
    private List<DataWrap> items;
    private Channel channel;

    private int curPage = 1;
    private Bundle arguments;

    private boolean isloading;
    public int flag;

    public NewsKindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_kind, container, false);

        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        recycler = ((RecyclerView) view.findViewById(R.id.recycler));


        ptrFrame = ((PtrFrameLayout) view.findViewById(R.id.fresh_frame));
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !checkContentCanPullDown(frame, recycler, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                if (channel != null) {
                    fresh(channel.getId());
                }else {
                    ptrFrame.refreshComplete();
                }

            }
        });


        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recycler.canScrollVertically(1) && items.size() > 0) {
                    if (!isloading) {
                        load(channel.getId(), curPage + 1);
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        items = new ArrayList<>();

        adapter = new NewsAdapter(items);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                DataWrap dataWrap = items.get(position);
                Intent intent = null;
                int id = v.getId();
                switch (dataWrap.getType()) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                        intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                        intent.putExtra("data", ((Article) dataWrap.getData()));
                        if (flag>0){
                            intent.putExtra("title",channel.getTitle());
                        }
                        /*阅读返回刷新数据*/
                        startActivityForResult(intent,3001);
                        break;

                        /*if (id == R.id.news_single_icon) {
                            intent = new Intent(getActivity(), NewsKindImageActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                            intent.putExtra("data", ((Article) dataWrap.getData()));
                            startActivity(intent);
                        }*/

                        /*if (id == R.id.news_triple_icon1) {
                            UIUtils.showToast("picture1");
                        } else if (id == R.id.news_triple_icon2) {
                            UIUtils.showToast("picture2");
                        } else if (id == R.id.news_triple_icon3) {
                            UIUtils.showToast("picture3");
                        } else {
                            intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                            intent.putExtra("data", ((Article) dataWrap.getData()));
                            startActivity(intent);
                        }
                        break;*/
                }
            }
        });
        recycler.setAdapter(adapter);

    }


    @Override
    public void initData() {
        super.initData();

        arguments = getArguments();
        if (arguments != null) {
            channel = ((Channel) arguments.getSerializable("data"));
            flag = arguments.getInt("flag",0);

            if (channel != null) {
                int channelid = channel.getId();
                fresh(channelid);
            }
        }


        adapter.notifyDataSetChanged();
    }

    private void fresh(int channelid) {


        RequestParams entity = new RequestParams(UrlUtils.N_achieveChannelData);
        entity.addBodyParameter("channelId", channelid + "");
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        entity.addBodyParameter("page", 1 + "");
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData data = JSON.parseObject(result, NetData.class);
                if (data.getResult() == 200) {
                    /*重置当前page*/
                    curPage = 1;
                    //items.removeAll(items);
                    items.clear();

                    JSONObject object = JSON.parseObject(data.getInfo());
                    List<Article> articles = JSON.parseArray(object.getString("list"), Article.class);
                    List<Article> hots = JSON.parseArray(object.getString("top"), Article.class);

                    if (hots != null && hots.size() > 0) {
                        DataWrap e1 = new DataWrap();
                        e1.setType(0);
                        e1.setData(hots);
                        items.add(e1);
                    }

                    for (int i = 0; i < articles.size(); i++) {

                        DataWrap e = new DataWrap();
                        Article article = articles.get(i);
                        e.setData(article);
                        e.setType(article.getDisplayType());

                        items.add(e);
                    }


                    if (items.size()==0){
                        isNodata(true);
                    }else {
                        isNodata(false);
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
                ptrFrame.refreshComplete();
            }
        });
    }

    private void load(int channelid, final int page) {

        /*修改正在加载，这时就不可以上拉加载，避免网络较慢的情况下，多此加载相同数据*/
        //loading.setVisibility(View.VISIBLE);
        isloading = true;

        RequestParams entity = new RequestParams(UrlUtils.N_achieveChannelData);
        entity.addBodyParameter("channelId", channelid + "");
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        entity.addBodyParameter("page", page+ "");
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData data = JSON.parseObject(result, NetData.class);
                if (data.getResult() == 200) {

                    JSONObject object = JSON.parseObject(data.getInfo());
                    List<Article> articles = JSON.parseArray(object.getString("list"), Article.class);
                    List<Article> hots = JSON.parseArray(object.getString("top"), Article.class);

                    if (hots != null && hots.size() > 0) {
                        DataWrap e1 = new DataWrap();
                        e1.setType(0);
                        e1.setData(hots);
                        items.add(e1);
                    }

                    for (int i = 0; i < articles.size(); i++) {

                        DataWrap e = new DataWrap();
                        Article article = articles.get(i);
                        e.setData(article);
                        e.setType(article.getDisplayType());

                        items.add(e);
                    }

                    adapter.notifyDataSetChanged();

                    if (articles.size()>0) {
                        curPage++;
                    }else {
                        UIUtils.showToast("没有更多了");
                    }
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
                /*上拉加载结束，隐藏掉*/
                //loading.setVisibility(View.GONE);
                isloading = false;
            }
        });
    }

    private boolean checkContentCanPullDown(PtrFrameLayout frame, View content, View header) {
        if (content instanceof RecyclerView) {
            /*这里就单独处理recycler*/
            RecyclerView content1 = (RecyclerView) content;
            return content1.canScrollVertically(-1);
        } else {
            return content.getScrollY() > 0;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==3001){
            fresh(channel.getId());
        }
    }
}
