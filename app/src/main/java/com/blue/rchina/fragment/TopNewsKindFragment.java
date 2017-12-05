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
import com.blue.rchina.R;
import com.blue.rchina.activity.NewsKindDetailActivty;
import com.blue.rchina.activity.NewsKindImageActivity;
import com.blue.rchina.adapter.NewsAdapter;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_top_news_kind)
public class TopNewsKindFragment extends BaseFragment {

    @ViewInject(R.id.top_news_ptr)
    PtrClassicFrameLayout ptrFrame;
    @ViewInject(R.id.top_news_rec)
    RecyclerView rec;
    List<DataWrap> datas;
    public NewsAdapter adapter;

    public TopNewsKindFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view= inflater.inflate(R.layout.fragment_top_news_kind, container, false);

        View view=x.view().inject(this,inflater,container);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        datas=new ArrayList<>();


        String json1="{\n" +
                "    \"info\": {\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"articleType\": 1,\n" +
                "                \"attachmentType\": 0,\n" +
                "                \"clickCount\": 0,\n" +
                "                \"collectState\": 2,\n" +
                "                \"commentCount\": 0,\n" +
                "                \"commentState\": 2,\n" +
                "                \"contentId\": 671,\n" +
                "                \"datetime\": \"5天前\",\n" +
                "                \"displayType\": 2,\n" +
                "                \"hateCount\": 0,\n" +
                "                \"likeCount\": 0,\n" +
                "                \"linkUrl\": \"http://news.cnstock.com/news,bwkx-201711-4148676.htm\",\n" +
                "                \"ly\": \"七天酒店\",\n" +
                "                \"outType\": 1,\n" +
                "                \"picsrc\": \"http://pavo.elongstatic.com/i/Hotel180_135/0000pQFg.jpg;http://pavo.elongstatic.com/i/Hotel180_135/nw_0005YTWf.jpg\",\n" +
                "                \"positionState\": 2,\n" +
                "                \"readState\": 2,\n" +
                "                \"shareState\": 2,\n" +
                "                \"shareUrl\": \"http://202.85.214.64:8088/smartchina/static/htmls/f5b71f93-8a54-4315-91e2-8acae3e31d88_671_s.html\",\n" +
                "                \"summary\": \"本市将增7家区级养老机构 \",\n" +
                "                \"title\": \"七天酒店您的第二个家 \"\n" +
                "            }\n" + ","+
                "            {\n" +
                "                \"articleType\": 1,\n" +
                "                \"attachmentType\": 0,\n" +
                "                \"clickCount\": 0,\n" +
                "                \"collectState\": 2,\n" +
                "                \"commentCount\": 0,\n" +
                "                \"commentState\": 2,\n" +
                "                \"contentId\": 671,\n" +
                "                \"datetime\": \"5天前\",\n" +
                "                \"displayType\": 6,\n" +
                "                \"hateCount\": 0,\n" +
                "                \"likeCount\": 0,\n" +
                "                \"linkUrl\": \"http://company.cnstock.com/company/scp_dsy/tcsy_rdgs/201606/3819256.htm\",\n" +
                "                \"ly\": \"七天酒店\",\n" +
                "                \"outType\": 1,\n" +
                "                \"picsrc\": \"http://pavo.elongstatic.com/i/Hotel180_135/0000pQFg.jpg;https://ss0.baidu.com/73F1bjeh1BF3odCf/it/u=1905861984,4292856734&fm=85&s=6BB22CC5441289C048942C8B0300E0C0;https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=3177777517,2227846681&fm=202&mola=new&crop=v1\",\n" +
                "                \"positionState\": 2,\n" +
                "                \"readState\": 2,\n" +
                "                \"shareState\": 2,\n" +
                "                \"shareUrl\": \"http://202.85.214.64:8088/smartchina/static/htmls/f5b71f93-8a54-4315-91e2-8acae3e31d88_671_s.html\",\n" +
                "                \"summary\": \"本市将增7家区级养老机构 \",\n" +
                "                \"title\": \"七天创始人收购法甲球队 \"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        String json2="{\n" +
                "    \"info\": {\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"articleType\": 1,\n" +
                "                \"attachmentType\": 0,\n" +
                "                \"clickCount\": 0,\n" +
                "                \"collectState\": 2,\n" +
                "                \"commentCount\": 0,\n" +
                "                \"commentState\": 2,\n" +
                "                \"contentId\": 671,\n" +
                "                \"datetime\": \"2天前\",\n" +
                "                \"displayType\": 5,\n" +
                "                \"hateCount\": 0,\n" +
                "                \"likeCount\": 0,\n" +
                "                \"linkUrl\": \"https://www.zhihu.com/question/20052866\",\n" +
                "                \"ly\": \"七天酒店\",\n" +
                "                \"outType\": 1,\n" +
                "                \"picsrc\": \"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2807738561,737393118&fm=27&gp=0.jpg;https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509872838161&di=aa372481c9561c25d0233f3e62829164&imgtype=0&src=http%3A%2F%2Fwww.igo.cn%2F2010%2Fimages%2F2014%2F09%2F09%2F43038.jpg\",\n" +
                "                \"positionState\": 2,\n" +
                "                \"readState\": 2,\n" +
                "                \"shareState\": 2,\n" +
                "                \"shareUrl\": \"http://202.85.214.64:8088/smartchina/static/htmls/f5b71f93-8a54-4315-91e2-8acae3e31d88_671_s.html\",\n" +
                "                \"summary\": \"本市将增7家区级养老机构 \",\n" +
                "                \"title\": \"七天酒店为您提供优质的服务 \"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        String json3="{\n" +
                "    \"info\": {\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"articleType\": 1,\n" +
                "                \"attachmentType\": 0,\n" +
                "                \"clickCount\": 0,\n" +
                "                \"collectState\": 2,\n" +
                "                \"commentCount\": 0,\n" +
                "                \"commentState\": 2,\n" +
                "                \"contentId\": 671,\n" +
                "                \"datetime\": \"3天前\",\n" +
                "                \"displayType\": 6,\n" +
                "                \"hateCount\": 0,\n" +
                "                \"likeCount\": 0,\n" +
                "                \"linkUrl\": \"http://company.cnstock.com/company/scp_dsy/tcsy_rdgs/201606/3819256.htm\",\n" +
                "                \"ly\": \"七天酒店\",\n" +
                "                \"outType\": 1,\n" +
                "                \"picsrc\": \"http://pavo.elongstatic.com/i/Hotel180_135/0000pQFg.jpg;http://pavo.elongstatic.com/i/Hotel180_135/nw_0005YTWf.jpg;http://pavo.elongstatic.com/i/Hotel180_135/0000tub0.jpg\",\n" +
                "                \"positionState\": 2,\n" +
                "                \"readState\": 2,\n" +
                "                \"shareState\": 2,\n" +
                "                \"shareUrl\": \"http://202.85.214.64:8088/smartchina/static/htmls/f5b71f93-8a54-4315-91e2-8acae3e31d88_671_s.html\",\n" +
                "                \"summary\": \"本市将增7家区级养老机构 \",\n" +
                "                \"title\": \"七天酒店再获十大影响力经济型酒店品牌\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        int flag = getArguments().getInt("flag", 2);
        String json=null;
        if (flag==0) {
             json= json3;
        }else if(flag==1){
            json=json1;
        }else{
            json=json2;
        }

        NetData netData = JSON.parseObject(json, NetData.class);
        String list = JSON.parseObject(netData.getInfo()).getString("list");
        List<Article> articles = JSON.parseArray(list, Article.class);

        for (int i = 0; i < articles.size(); i++) {
            DataWrap e = new DataWrap();
            e.setType(articles.get(i).getDisplayType());
            e.setData(articles.get(i));
            datas.add(e);


        }


    }

    @Override
    public void initView(View view) {
        super.initView(view);

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                },1000 );
            }
        });

        adapter = new NewsAdapter(datas);
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        rec.setAdapter(adapter);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                DataWrap dataWrap = datas.get(tag);
                Intent intent = null;
                int id = v.getId();
                switch (tag) {
                    case 0:
                        intent= new Intent(getActivity(), NewsKindDetailActivty.class);
                        intent.putExtra("data", ((Article) dataWrap.getData()));
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                        intent.putExtra("data", ((Article) dataWrap.getData()));
                        startActivity(intent);
                        break;
                    case 2:
                        if (id == R.id.news_single_icon) {
                            intent = new Intent(getActivity(), NewsKindImageActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                            intent.putExtra("data", ((Article) dataWrap.getData()));
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                        intent.putExtra("data", ((Article) dataWrap.getData()));
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), NewsKindDetailActivty.class);
                        intent.putExtra("data", ((Article) dataWrap.getData()));
                        startActivity(intent);
                        break;
                    case 6:
                        if (id == R.id.news_triple_icon1) {
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
                        break;
                }
            }
        });


    }
}
