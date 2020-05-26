package com.blue.rchina.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blue.rchina.R;
import com.blue.rchina.activity.NewsSelectActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.ChannelItem;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.UIDUtils;
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

/**
 * A simple {@link Fragment} subclass.
 */
/*首页新闻栏目，fragment*/
@ContentView(R.layout.fragment_news)
public class NewsMainFragment extends BaseFragment {

    @ViewInject(R.id.news_tab)
    TabLayout tab;
    @ViewInject(R.id.news_pager)
    ViewPager pager;

    @ViewInject(R.id.news_plus)
    ImageView plus;

    private FragmentManager fragmentManager;
    private List<BaseFragment> fragments;
    private List<String> titles;
    public FragmentStatePagerAdapter adapter;
    private List<Channel> channels;
    private List<Channel> otherChannels;
    public Channel channel;
    public int flag;

    public NewsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=x.view().inject(this,inflater, container);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        fragments=new ArrayList<>();
        titles=new ArrayList<>();
        channels=new ArrayList<>();
        otherChannels=new ArrayList<>();

        Bundle arguments = getArguments();
        if (arguments != null) {
            flag = arguments.getInt("flag",0);

            channel = ((Channel) arguments.getSerializable("channel"));
            channels=channel.getSons();



            /*这里是默认排序*/
            for (int i = 0; i < channels.size(); i++) {
                Channel channelItem = channels.get(i);


                BaseFragment fragment=null;
                Bundle args = new Bundle();
                args.putSerializable("data",channelItem);
                args.putInt("flag",flag);
                int channelType = channelItem.getChannelType();
                if (channelType ==2) {
                    fragment = new NewsKindFragment();
                }else if (channelType==1){
                    fragment=new NewsWebFragment();
                }else if (channelType==6){
                    fragment=new LiveListFragment();
                }else if(channelType==7){
                    fragment=new DocumentFragment();
                }else {
                    fragment = new NewsKindFragment();
                }
                fragment.setArguments(args);
                fragments.add(fragment);
            }
            getChannelStr();
        }




    }

    private void getChannelStr() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveChannelSort);
        entity.addBodyParameter("areaId",channel.getAreaId());
        entity.addBodyParameter("dataId", UIDUtils.getUID(mActivity));

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200) {

                    JSONArray arr = JSON.parseArray(netData.getInfo());

                    /*选择栏目*/
                    List<ChannelItem> list1 = JSON.parseArray(arr.getJSONObject(0).getString("list"), ChannelItem.class);
                    /*可选栏目*/
                    List<ChannelItem> list2 = JSON.parseArray(arr.getJSONObject(1).getString("list"), ChannelItem.class);
                    otherChannels.clear();
                    otherChannels.addAll(cItem2C(list2));

                    changeStr(cItem2C(list1));

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
                isHideLoading(true);
            }
        });
    }

    private List<Channel> cItem2C(List<ChannelItem> list2) {
        List<Channel> list=new ArrayList<>();
        if (list2!=null){
            for (int i = 0; i < list2.size(); i++) {
                ChannelItem channelItem = list2.get(i);

                Channel e = new Channel();
                e.setId(channelItem.getChannelid());
                e.setTitle(channelItem.getTitle());
                e.setChannelType(channelItem.getChannelType());
                e.setOutLink(channelItem.getOutLink());
                e.setShowicon(channelItem.getShowIcon());
                list.add(e);
            }
        }

        return list;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), NewsSelectActivity.class);
                intent.putExtra("select", (Serializable) channels);
                intent.putExtra("other", (Serializable) otherChannels);
                intent.putExtra("channel",channel);
                startActivityForResult(intent,300);
            }
        });
        fragmentManager = getFragmentManager();
        adapter = new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {

                return fragments.get(position);
            }

            @Override
            public int getCount() {
                if (channels != null) {
                    return channels.size();
                }
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return channels.get(position).getTitle();
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

        };
        pager.setAdapter(adapter);

        changeTabMode();
        tab.setupWithViewPager(pager);

    }

    private void changeTabMode() {
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < channels.size(); i++) {
            builder.append(channels.get(i).getTitle());
        }

        if (builder.toString().length()>12||channels.size()>4){

            if (builder.toString().length()<=10){
                tab.setTabMode(TabLayout.MODE_FIXED);
            }else {
                tab.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }else {
            if (channels.size()==1){
                tab.setTabMode(TabLayout.MODE_SCROLLABLE);
            }else {
                tab.setTabMode(TabLayout.MODE_FIXED);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 300:

                if (resultCode==200) {
                /*刷新结构*/
                    isHideLoading(false);
                    getChannelStr();

                }
                break;
        }
    }

    private void changeStr(List<Channel> selects) {
        if (selects != null && selects.size() > 0) {
            channels.clear();
            fragments.clear();
            adapter.notifyDataSetChanged();
            channels.addAll(selects);

            /*测试文档功能*/
            Channel e = new Channel();
            e.setChannelType(7);
            e.setTitle("文档");
            channels.add(e);

            List<BaseFragment> temps=new ArrayList<>();
            for (int i = 0; i < channels.size(); i++) {
                Channel channelItem = channels.get(i);

                BaseFragment fragment=null;
                Bundle args = new Bundle();
                args.putSerializable("data",channelItem);
                args.putInt("flag",flag);
                int channelType = channelItem.getChannelType();
                /*if (channelType ==2) {
                    fragment = new NewsKindFragment();
                }else if (channelType==1){
                    fragment=new NewsWebFragment();
                }*/

                if (channelType ==2) {
                    fragment = new NewsKindFragment();
                }else if (channelType==1){
                    fragment=new NewsWebFragment();
                }else if (channelType==6){
                    fragment=new LiveListFragment();
                }else if(channelType==7){
                    fragment=new DocumentFragment();
                }else {
                    fragment = new NewsKindFragment();
                }
                //fragment = new NewsKindFragment();
                fragment.setArguments(args);
                temps.add(fragment);

            }
            changeTabMode();
            fragments.addAll(temps);
            adapter.notifyDataSetChanged();
        }
    }
}
