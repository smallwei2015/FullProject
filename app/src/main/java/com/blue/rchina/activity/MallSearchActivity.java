package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.SearchAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.SearchItem;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MallSearchActivity extends BaseActivity {

    @ViewInject(R.id.top_view)
    View headPadding;
    @ViewInject(R.id.top_search_go)
    ImageView go;
    @ViewInject(R.id.top_search_edit)
    EditText edit;
    @ViewInject(R.id.top_search_cancel)
    TextView cancel;
    @ViewInject(R.id.search_rec)
    RecyclerView rec;


    private List<SearchItem> datas;
    private View.OnClickListener listener;
    private String searchStr;
    public int flag;
    public SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_search);
        x.view().inject(this);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //沉浸后用来占位的视图显示
            headPadding.setVisibility(View.VISIBLE);

            int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
            if (statusBarHeight > 0) {
                headPadding.getLayoutParams().height = statusBarHeight;
            }
        } else {
            //这里做的是不支持沉浸式的适配
            headPadding.setVisibility(View.GONE);
        }
        initData();
        initView();

    }

    @Override
    public void initData() {
        super.initData();

        /*1表示商品搜索2表示附近超市商品搜索*/
        flag = getIntent().getIntExtra("flag",1);
        datas = new ArrayList<>();


        searchAdapter = new SearchAdapter(mActivity,datas);
        searchAdapter.setListener(new SearchAdapter.SearchListener() {
            @Override
            public void itemClick(SearchItem item) {
                Intent intent=new Intent(mActivity,SearchGoodsResultActivity.class);
                intent.putExtra("name",item.getTitle());
                intent.putExtra("flag",flag);
                startActivity(intent);
            }

            @Override
            public void bottomClick() {
                clearHistory(2);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        RequestParams entity = new RequestParams(UrlUtils.N_achieveSearchRecord);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("flag","2");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    datas.clear();

                    String hot = JSON.parseObject(netData.getInfo()).getString("hot");
                    String list = JSON.parseObject(netData.getInfo()).getString("list");
                    SearchItem e = new SearchItem();
                    e.setType(0);
                    e.setTitle("热点搜索");
                    datas.add(0,e);

                    List<SearchItem> hotItems = JSON.parseArray(hot, SearchItem.class);
                    datas.addAll(1,hotItems);

                    SearchItem e2 = new SearchItem();
                    e2.setType(0);
                    e2.setTitle("搜索历史");
                    datas.add(e2);
                    List<SearchItem> listItems = JSON.parseArray(list, SearchItem.class);
                    datas.addAll(listItems);


                    if (listItems.size()>0){
                        SearchItem e3 = new SearchItem();
                        e3.setType(2);
                        e3.setTitle("清空搜索记录");
                        datas.add(e3);
                    }else {
                        SearchItem e3 = new SearchItem();
                        e3.setType(3);
                        e3.setTitle("暂无搜索记录");
                        datas.add(e3);
                    }

                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
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
    public void initView() {
        super.initView();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStr = edit.getText().toString().trim();

                if (TextUtils.isEmpty(searchStr)){
                    UIUtils.showToast("请输入搜索商品");
                    return;
                }

                Intent intent=new Intent(mActivity,SearchGoodsResultActivity.class);
                intent.putExtra("name",searchStr);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });


        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_SEARCH:
                        searchStr = edit.getText().toString().trim();
                        if (TextUtils.isEmpty(searchStr)){
                            UIUtils.showToast("请输入搜索商品");
                        }else {
                            Intent intent = new Intent(mActivity, SearchGoodsResultActivity.class);
                            intent.putExtra("name",searchStr);
                            intent.putExtra("flag",flag);
                            startActivity(intent);
                        }
                        break;

                }
                return false;
            }
        });

        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                SearchItem searchItem = datas.get(position);

                int type = searchItem.getType();

                if (type == 1) {
                    return 1;
                }
                return 2;
            }
        });

        rec.setLayoutManager(manager);
        rec.setAdapter(searchAdapter);
    }

    private void clearHistory(int type) {
        RequestParams entity = new RequestParams(UrlUtils.N_emptySearchRecord);
        entity.addBodyParameter("appuserId",UserManager.getAppuserId());
        entity.addBodyParameter("flag",type+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String r) {
                NetData netData = JSON.parseObject(r, NetData.class);
                if (netData.getResult()==200){
                    loadHistory();
                }else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
