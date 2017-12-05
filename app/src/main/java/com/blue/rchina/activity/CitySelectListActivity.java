package com.blue.rchina.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.zhy.view.flowlayout.FlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blue.rchina.manager.UserManager.action_cityChange;

public class CitySelectListActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    public RecyclerView recycler;
    public List<DataWrap> datas;
    public RecyclerView.Adapter adapter;
    private int flag=1;
    private BroadcastReceiver cityReceiver;
    public Bundle bundle;
    private View topPadding;
    private AreaInfo parentArea;
    public FlowLayout flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        topPadding =  findViewById(R.id.top_padding);
        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            topPadding.getLayoutParams().height=statusBarHeight;
        }
        initView();
        initData();

    }

    @Override
    public void initData() {
        super.initData();

        if (flag ==1) {
            isHideLoading(false);
            loadCity();
        }else {
            if (bundle != null) {

                parentArea=((AreaInfo) bundle.getSerializable("parent"));
                AreaInfo info = (AreaInfo) bundle.getSerializable("info");

                DataWrap e = new DataWrap();
                e.setType(0);
                e.setData(info);
                datas.add(e);

                List<AreaInfo> sons = info.getSons();
                if (sons != null) {
                    for (int i = 0; i < sons.size(); i++) {
                        DataWrap e1 = new DataWrap();
                        e1.setType(1);
                        e1.setData(sons.get(i));
                        datas.add(e1);
                    }
                }

                adapter.notifyDataSetChanged();


                LayoutInflater inflater = LayoutInflater.from(mActivity);
                for (int i = 0; i < sons.size(); i++) {
                    View inflate = inflater.inflate(R.layout.city_tag, null);
                    inflate.setOnClickListener(this);
                    inflate.setOnLongClickListener(this);
                    View del = inflate.findViewById(R.id.tag_del);
                    ((TextView) inflate.findViewById(R.id.tab_title)).setText(sons.get(i).getAreaName());
                    del.setTag(inflate);
                    inflate.setTag(del);
                    del.setOnClickListener(this);
                    flow.addView(inflate);
                }
            }else {
                isNodata(true);
            }
        }

        registerCityReceiver();
    }


    private void loadCity(){
        RequestParams entity = new RequestParams(UrlUtils.N_achieveAreaStructure);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData data= JSON.parseObject(result,NetData.class);
                int dataResult = data.getResult();

                if (dataResult==200){
                    List<AreaInfo> areas = JSON.parseArray(data.getInfo(), AreaInfo.class);

                    for (int i = 0; i < areas.size(); i++) {
                        AreaInfo areaInfo = areas.get(i);
                        DataWrap e = new DataWrap();
                        e.setType(1);
                        e.setData(areaInfo);
                        datas.add(e);
                    }


                    adapter.notifyDataSetChanged();

                }else {
                    UIUtils.showToast(data.getMessage()==null?"":data.getMessage());
                    isNodata(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isNodata(true);
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

    private void registerCityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_cityChange);

        cityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AreaInfo area = (AreaInfo) intent.getSerializableExtra("area");
                finish();
            }
        };
        registerReceiver(cityReceiver, filter);
    }
    @Override
    public void initView() {
        super.initView();

        bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            flag= bundle.getInt("flag",1);
        }

        boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);

        if (firstOpen&&flag==1){
            initTop(-1,"选择城市",-1,R.color.bg_color,R.color.middle_gray);
        }else {
            initTop(R.mipmap.left_gray, "城市切换", R.mipmap.change_area,R.color.bg_color,R.color.middle_gray);
        }

        datas = new ArrayList<>();
        recycler = ((RecyclerView) findViewById(R.id.city_rec));
        flow = ((FlowLayout) findViewById(R.id.flow));

        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {


            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                View view =null;
                switch (viewType) {
                    case 0:
                        view = inflater.inflate(R.layout.city_head, parent, false);
                        break;
                    case 1:
                        view = inflater.inflate(R.layout.city_item, parent, false);
                        break;
                }
                return new Holder(view,viewType);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                DataWrap dataWrap = datas.get(position);
                final AreaInfo data = (AreaInfo) dataWrap.getData();

                switch (dataWrap.getType()){
                    case 0:
                        holder.name.setText("切换到\""+data.getAreaName()+"\"");
                        holder.parent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (data.getIsOperate()==1) {

                                    if (parentArea != null) {
                                        /*存储父节点name，用于天气查询*/
                                        data.setProvinceCapital(parentArea.getProvinceCapital());
                                    }
                                    Intent intent = new Intent(action_cityChange);
                                    intent.putExtra("area", data);
                                    sendBroadcast(intent);

                                    setResult(200);
                                    finish();
                                }else {
                                    UIUtils.showToast("该城市还未开通，无法选择");
                                }
                            }
                        });
                        break;
                    case 1:
                        holder.name.setText(data.getAreaName());
                        holder.parent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(mActivity,CitySelectListActivity.class);
                                Bundle value = new Bundle();
                                value.putInt("flag",2);
                                value.putSerializable("info",data);
                                String reg = "[县市区]*$";
                                Pattern compile = Pattern.compile(reg);
                                Matcher matcher = compile.matcher(data.getAreaName());

                                if (matcher.find()){
                                    if (parentArea != null) {
                                        data.setProvinceCapital(parentArea.getAreaName());
                                    }
                                    value.putSerializable("parent",data);
                                }else {
                                    value.putSerializable("parent",parentArea);
                                }
                                intent.putExtra("data", value);
                                startActivity(intent);
                            }
                        });
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
        recycler.setAdapter(adapter);

        flow.setOnClickListener(this);
    }

    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);

        if (flow.getVisibility()==View.GONE) {
            recycler.setVisibility(View.GONE);
            flow.setVisibility(View.VISIBLE);
        }else {
            recycler.setVisibility(View.VISIBLE);
            flow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cityReceiver != null) {
            unregisterReceiver(cityReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        boolean firstOpen = SPUtils.getSP().getBoolean("firstOpen", true);

        if (firstOpen&&flag==1){

        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tag_del){
            View tag = (View) v.getTag();
            flow.removeView(tag);
        }else if(v.getId()==R.id.flow){
            for (int i = 0; i < flow.getChildCount(); i++) {
                View childAt = flow.getChildAt(i);
                childAt.clearAnimation();
                ((View) childAt.getTag()).setVisibility(View.GONE);
            }
        }else {
            UIUtils.showToast("长按删除");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        RotateAnimation rotate  = new RotateAnimation(-3f, 3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(100);//设置动画持续周期
        rotate.setRepeatCount(8);//设置重复次数
        rotate.setFillAfter(false);//动画执行完后是否停留在执行完的状态


        for (int i = 0; i < flow.getChildCount(); i++) {
            View childAt = flow.getChildAt(i);
            ((View) childAt.getTag()).setVisibility(View.VISIBLE);
            childAt.startAnimation(rotate);

        }
        v.startAnimation(rotate);
        return true;
    }

    class Holder extends RecyclerView.ViewHolder{

        public TextView name;
        public View parent;

        public Holder(View itemView, int type) {
            super(itemView);
            switch (type){
                case 0:
                    parent=itemView;
                    name= (TextView) itemView.findViewById(R.id.city_name);
                    break;
                case 1:
                    parent = itemView;
                    name = ((TextView) itemView.findViewById(R.id.city_name));
                    break;
            }
        }
    }
}
