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
import com.blue.rchina.R;
import com.blue.rchina.activity.NewsSelectActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Channel;
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
@ContentView(R.layout.fragment_news)
public class NewsFragment extends BaseFragment {

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
    public Channel channel;
    public int flag;

    public NewsFragment() {
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
                }
                fragment.setArguments(args);
                fragments.add(fragment);
            }
            //getChannelStr();
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
                    List<Channel> all = JSON.parseArray(netData.getInfo(), Channel.class);

                    //changeStr(all);
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

            }
        });
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        plus.setVisibility(View.GONE);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), NewsSelectActivity.class);
                intent.putExtra("select", (Serializable) channels);
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

        if (channels.size()==0){
            isNodata(true);
        }
    }

    private void changeTabMode() {
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < channels.size(); i++) {
            builder.append(channels.get(i).getTitle());
        }

        if (builder.toString().length()>=10||channels.size()>4){
            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
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

                if (data!=null) {
                    List<Channel> selects = (List<Channel>) data.getSerializableExtra("select");

                    //changeStr(selects);
                }
                break;
        }
    }

    private void changeStr(List<Channel> selects) {
        if (selects != null && selects.size() > 0) {
            channels.clear();
            fragments.clear();
            channels.addAll(selects);
            adapter.notifyDataSetChanged();

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
                }

                fragment.setArguments(args);
                fragments.add(fragment);
            }

            adapter.notifyDataSetChanged();
        }
    }
}
