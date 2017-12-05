package com.blue.rchina.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.SearchItem;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

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
    public RecyclerView.Adapter adapter;
    private View.OnClickListener listener;
    private String searchStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        datas = new ArrayList<>();

        SearchItem e = new SearchItem();
        e.setType(0);
        e.setContent("热点搜索");
        datas.add(e);

        for (int i = 0; i < 7; i++) {
            SearchItem e1 = new SearchItem();
            e1.setType(1);
            e1.setContent("hot"+i);
            datas.add(e1);
        }


        SearchItem e2 = new SearchItem();
        e2.setType(0);
        e2.setContent("搜索历史");
        datas.add(e2);

        for (int i = 0; i < 6; i++) {
            SearchItem e1 = new SearchItem();
            e1.setType(1);
            e1.setContent("history"+i);
            datas.add(e1);
        }


        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchItem vTag = (SearchItem) v.getTag();


                switch (v.getId()) {
                    case R.id.search_item_tv:
                        seachNews(vTag.getContent());
                        break;
                }


            }
        };
    }

    @Override
    public void initView() {
        super.initView();


        /*//初始化键盘布局，下面在放进 KeyBoardView里面去。
        Keyboard mKeyboard = new Keyboard(mActivity, R.xml.board);
        //配置keyBoardView
        try {
            keyboardView.setKeyboard(mKeyboard);

            //装甲激活~ 咳咳… mKeyboardView.setPreviewEnabled(false);
            // 这个是，效果图按住是出来的预览图。
            // 设置监听，不设置的话会报错。监听放下面了。
            keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {


                @Override
                public void onPress(int primaryCode) {
                    Log.e("sun", "onPress=======:" + primaryCode);

                }

                @Override
                public void onRelease(int primaryCode) {
                    Log.e("sun", "onRelease====:" + primaryCode);
                }

                @Override
                public void onKey(int primaryCode, int[] keyCodes) {
                    Log.e("sun", "onkey=====primaryCode:" + primaryCode + "");
                }

                @Override
                public void onText(CharSequence text) {
                    //pre.setText(text);

                    //ic.commitText(String.valueOf(text),1);
                }

                @Override
                public void swipeLeft() {
                    Log.e("sun", "swipeLeft");
                }

                @Override
                public void swipeRight() {
                    Log.e("sun", "swipeRight");
                }

                @Override
                public void swipeDown() {
                    Log.e("sun", "swipeDown");
                }

                @Override
                public void swipeUp() {
                    Log.e("sun", "swipeUp");
                }
            });

        } catch (Exception e) {
            Log.e("sun", "keyview初始化失败");
        }*/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStr = edit.getText().toString();

                seachNews(searchStr);

            }
        });
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_SEARCH:
                        searchStr = edit.getText().toString();
                        seachNews(searchStr);
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

                if (type == 0) {
                    return 2;
                }
                return 1;
            }
        });

        rec.setLayoutManager(manager);
        adapter = new RecyclerView.Adapter<Holder>() {

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = null;
                Holder holder = null;
                LayoutInflater layoutInflater = LayoutInflater.from(mActivity);

                switch (viewType) {

                    case 0:
                        view = layoutInflater.inflate(R.layout.search_item_title, parent, false);
                        holder = new Holder(view, viewType);
                        break;
                    case 1:
                        view = layoutInflater.inflate(R.layout.search_item_text, parent, false);
                        holder = new Holder(view, viewType);
                        break;
                }

                x.view().inject(holder, view);

                return holder;
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {

                SearchItem searchItem = datas.get(position);

                int type = searchItem.getType();

                switch (type) {
                    case 0:
                        holder.title_tv.setTag(searchItem);
                        holder.title_tv.setText(searchItem.getContent());
                        holder.title_tv.setOnClickListener(listener);
                        break;
                    case 1:
                        holder.item_tv.setTag(searchItem);
                        holder.item_tv.setText(searchItem.getContent());
                        holder.item_tv.setOnClickListener(listener);
                        break;

                }
            }


            @Override
            public int getItemCount() {
                if (datas != null) {
                    return datas.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int position) {
                return datas.get(position).getType();
            }
        };
        rec.setAdapter(adapter);


    }

    private void seachNews(String searchStr) {
        ///UIUtils.showPro(mActivity);
    }


    class Holder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.search_item_tv)
        TextView item_tv;
        @ViewInject(R.id.search_title_tv)
        TextView title_tv;

        public Holder(View view, int viewType) {
            super(view);

            switch (viewType) {
                case 0:
                    title_tv = (TextView) view.findViewById(R.id.search_title_tv);
                    break;
                case 1:
                    item_tv = (TextView) view.findViewById(R.id.search_item_tv);
                    break;
            }
        }
    }
}
