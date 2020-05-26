package com.blue.rchina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.DocAdapter;
import com.blue.rchina.adapter.RecPaddingDecoration;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.DocBean;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
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
@ContentView(R.layout.fragment_document)
public class DocumentFragment extends BaseFragment {


    private PtrClassicFrameLayout ptr;
    private RecyclerView rec;
    private boolean isLoading=false;
    int cPager=1;
    private ArrayList<DocBean> datas;
    private DocAdapter adapter;

    public DocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= x.view().inject(this,inflater, container);

        initView(view);
        initData();
        return view;
        //return inflater.inflate(R.layout.fragment_document, container, false);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        ptr=((PtrClassicFrameLayout) view.findViewById(R.id.nearby_ptr));
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        ptr.setHeaderView(header);
        ptr.setLastUpdateTimeKey(getClass().getName());
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                fresh();
            }
        });

        rec = ((RecyclerView) view.findViewById(R.id.rec));
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        rec.addItemDecoration(new RecPaddingDecoration());

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoading) {
                        loadData(cPager+1);
                    }
                }
            }
        });
        datas=new ArrayList<>();
        adapter = new DocAdapter(mActivity,datas);

        rec.setAdapter(adapter);

        ptr.autoRefresh();
    }

    private void loadData(int i) {
        rec.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIUtils.showToast("没有更多了");
            }
        },1000);
    }

    private void fresh() {

        String test="[{\n" +
                "\t\t\t\t\"fileName\": \"如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品.pdf\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 4,\n" +
                "\t\t\t\t\"description\": \"如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品     by理查德尼德翰姆  （348P）\",\n" +
                "\t\t\t\t\"collectNum\": 2,\n" +
                "\t\t\t\t\"title\": \"《如何做创意》\",\n" +
                "\t\t\t\t\"tagName\": \"其他文案\",\n" +
                "\t\t\t\t\"shareNum\": 13,\n" +
                "\t\t\t\t\"isVip\": 1,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2019-11-22\",\n" +
                "\t\t\t\t\"fileSize\": 2098495,\n" +
                "\t\t\t\t\"collectState\": 1,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 368,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/11/20191122/如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品.pdf\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/11/20191122/f31a677d11d04f82b5cd05f3e5bb0dd6.html\",\n" +
                "\t\t\t\t\"downNum\": 18,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 1920,\n" +
                "\t\t\t\t\"desc\": \"如何做创意——13位美国杰出艺术指导和文案撰稿人的创意观念、方法与作品     by理查德尼德翰姆  （348P）\",\n" +
                "\t\t\t\t\"scanState\": 2\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"改进新闻报道的具体办法.doc\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 1,\n" +
                "\t\t\t\t\"description\": \"为进一步贯彻党落实中央八项规定精神的实施办法有关改进新闻报道的具体办法的通知及修订后的实施细则精神的实施办法，特制定如下具体办法\",\n" +
                "\t\t\t\t\"collectNum\": 3,\n" +
                "\t\t\t\t\"title\": \"改进新闻报道的具体办法\",\n" +
                "\t\t\t\t\"tagName\": \"管理制度\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2019-09-11\",\n" +
                "\t\t\t\t\"fileSize\": 31232,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 198,\n" +
                "\t\t\t\t\"fileUrl\": \"http://okcm.cn/cmzk/static/file/1/20190911/改进新闻报道的具体办法.doc\",\n" +
                "\t\t\t\t\"wenkuLink\": \"http://okcm.cn/cmzk/static/file/1/20190911/3e26356b0a854fa7bde61a305c8da842.html\",\n" +
                "\t\t\t\t\"downNum\": 7,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 926,\n" +
                "\t\t\t\t\"desc\": \"为进一步贯彻党落实中央八项规定精神的实施办法有关改进新闻报道的具体办法的通知及修订后的实施细则精神的实施办法，特制定如下具体办法\",\n" +
                "\t\t\t\t\"scanState\": 1\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"网站建设合同.doc\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"inPayId\": \"DOCPIC2.\",\n" +
                "\t\t\t\t\"description\": \"一份非常完善的协议\",\n" +
                "\t\t\t\t\"collectNum\": 2,\n" +
                "\t\t\t\t\"title\": \"网站建设合同\",\n" +
                "\t\t\t\t\"shareNum\": 1,\n" +
                "\t\t\t\t\"isFree\": 2,\n" +
                "\t\t\t\t\"price\": 12,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 172,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/11/20191125/网站建设合同.doc\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/11/20191125/8f4d0969a64b4402a2df2cfd78e2e1c4.html\",\n" +
                "\t\t\t\t\"downNum\": 2,\n" +
                "\t\t\t\t\"scanState\": 2,\n" +
                "\t\t\t\t\"wenkuType\": 1,\n" +
                "\t\t\t\t\"tagName\": \"其他文案\",\n" +
                "\t\t\t\t\"isVip\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2019-11-25\",\n" +
                "\t\t\t\t\"fileSize\": 23552,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 1932,\n" +
                "\t\t\t\t\"desc\": \"一份非常完善的协议\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"市纪委监委网站集群建设推进方案（9.17）.doc\",\n" +
                "\t\t\t\t\"buyNum\": 1,\n" +
                "\t\t\t\t\"inPayId\": \"DOCPIC2.\",\n" +
                "\t\t\t\t\"description\": \"9月8日，省纪委常委、省监委委员XXX一行来我市调研纪检监察网络安全保障工作，要求我市纪委监委网站进行集约化建设，以实现全市纪委监委网站集群的管理模式，使其符合国家、省、市对网站建设的要求。\",\n" +
                "\t\t\t\t\"collectNum\": 1,\n" +
                "\t\t\t\t\"title\": \"XX市纪委监委网站集群建设工作方案\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isFree\": 2,\n" +
                "\t\t\t\t\"price\": 12,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 129,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/11/20191212/市纪委监委网站集群建设推进方案（9.17）.doc\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/11/20191212/bfba86b653084cabae91f31fc4228f80.html\",\n" +
                "\t\t\t\t\"downNum\": 2,\n" +
                "\t\t\t\t\"scanState\": 2,\n" +
                "\t\t\t\t\"wenkuType\": 1,\n" +
                "\t\t\t\t\"tagName\": \"项目方案\",\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"createTime\": \"2019-12-12\",\n" +
                "\t\t\t\t\"fileSize\": 27136,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 2231,\n" +
                "\t\t\t\t\"desc\": \"9月8日，省纪委常委、省监委委员XXX一行来我市调研纪检监察网络安全保障工作，要求我市纪委监委网站进行集约化建设，以实现全市纪委监委网站集群的管理模式，使其符合国家、省、市对网站建设的要求。\"\n" +
                "\t\t\t},{\n" +
                "\t\t\t\t\"fileName\": \"华侨城东方里2018新媒体传播建议.pdf\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 4,\n" +
                "\t\t\t\t\"description\": \"华侨城东方里2018 新媒体传播建议附微信主视觉范例\",\n" +
                "\t\t\t\t\"collectNum\": 0,\n" +
                "\t\t\t\t\"title\": \"华侨城年度新媒体传播建议\",\n" +
                "\t\t\t\t\"tagName\": \"营销方案\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2020-05-15\",\n" +
                "\t\t\t\t\"fileSize\": 23766545,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 56,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/2/20200515/华侨城东方里2018新媒体传播建议.pdf\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/2/20200515/ed3b9f0f2d854ed0bd5a8c2fb455a55c.html\",\n" +
                "\t\t\t\t\"downNum\": 0,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 3562,\n" +
                "\t\t\t\t\"desc\": \"华侨城东方里2018 新媒体传播建议附微信主视觉范例\",\n" +
                "\t\t\t\t\"scanState\": 1\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"服务器机房安全管理操作规程.docx\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 1,\n" +
                "\t\t\t\t\"description\": \"中心机房安全管理操作规程之七大规范\",\n" +
                "\t\t\t\t\"collectNum\": 0,\n" +
                "\t\t\t\t\"title\": \"中心机房安全管理操作规程\",\n" +
                "\t\t\t\t\"tagName\": \"管理制度\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2020-05-15\",\n" +
                "\t\t\t\t\"fileSize\": 20073,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 24,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/1/20200515/服务器机房安全管理操作规程.docx\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/1/20200515/0d8b9bc1f98547bb8937d3d32aefb396.html\",\n" +
                "\t\t\t\t\"downNum\": 0,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 3561,\n" +
                "\t\t\t\t\"desc\": \"中心机房安全管理操作规程之七大规范\",\n" +
                "\t\t\t\t\"scanState\": 1\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"抖音电动冲牙器小视频拍摄创意.pdf\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 4,\n" +
                "\t\t\t\t\"description\": \"电动冲牙器社交平台推广小视频拍摄创意方案\",\n" +
                "\t\t\t\t\"collectNum\": 0,\n" +
                "\t\t\t\t\"title\": \"电动冲牙器社交平台推广小视频拍摄创意\",\n" +
                "\t\t\t\t\"tagName\": \"营销方案\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2020-05-15\",\n" +
                "\t\t\t\t\"fileSize\": 349129,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 14,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/2/20200515/抖音电动冲牙器小视频拍摄创意.pdf\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/2/20200515/0092cba155564baf850e948e9581a9c7.html\",\n" +
                "\t\t\t\t\"downNum\": 0,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 3560,\n" +
                "\t\t\t\t\"desc\": \"电动冲牙器社交平台推广小视频拍摄创意方案\",\n" +
                "\t\t\t\t\"scanState\": 1\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fileName\": \"服务器管理制度.doc\",\n" +
                "\t\t\t\t\"buyNum\": 0,\n" +
                "\t\t\t\t\"wenkuType\": 1,\n" +
                "\t\t\t\t\"description\": \"公司内部服务器的安全是关系到公司数据保密和安全的一件大事，是保证各个业务系统正常工作的前提条件，因此必须进行科学、有效地管理。\",\n" +
                "\t\t\t\t\"collectNum\": 0,\n" +
                "\t\t\t\t\"title\": \"服务器管理制度\",\n" +
                "\t\t\t\t\"tagName\": \"管理制度\",\n" +
                "\t\t\t\t\"shareNum\": 0,\n" +
                "\t\t\t\t\"isVip\": 2,\n" +
                "\t\t\t\t\"isFree\": 1,\n" +
                "\t\t\t\t\"createTime\": \"2020-05-15\",\n" +
                "\t\t\t\t\"fileSize\": 59904,\n" +
                "\t\t\t\t\"collectState\": 2,\n" +
                "\t\t\t\t\"checkState\": 3,\n" +
                "\t\t\t\t\"clickNum\": 20,\n" +
                "\t\t\t\t\"fileUrl\": \"https://okcm.cn/cmzk/static/file/1/20200515/服务器管理制度.doc\",\n" +
                "\t\t\t\t\"wenkuLink\": \"https://okcm.cn/cmzk/static/file/1/20200515/f34ee4f4dbda468e8e7e6b4eff5f9ff9.html\",\n" +
                "\t\t\t\t\"downNum\": 0,\n" +
                "\t\t\t\t\"fileType\": 4,\n" +
                "\t\t\t\t\"card\": {\n" +
                "\t\t\t\t\t\"cardState\": 2,\n" +
                "\t\t\t\t\t\"cardUrl\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fileId\": 3559,\n" +
                "\t\t\t\t\"desc\": \"公司内部服务器的安全是关系到公司数据保密和安全的一件大事，是保证各个业务系统正常工作的前提条件，因此必须进行科学、有效地管理。\",\n" +
                "\t\t\t\t\"scanState\": 1\n" +
                "\t\t\t}]";
        List<DocBean> docBeans = JSON.parseArray(test, DocBean.class);


        datas.clear();
        datas.addAll(docBeans);
        adapter.notifyDataSetChanged();

        ptr.refreshComplete();
    }
}
