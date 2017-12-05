package com.blue.rchina.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.CartGoods;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class MallDetailActivity extends BaseActivity {

    private Goods data;
    @ViewInject(R.id.mall_detail_rec)
    RecyclerView rec;
    @ViewInject(R.id.pay_goods)
    TextView pay;
    @ViewInject(R.id.add_goods)
    TextView add;

    private List<DataWrap> infos;
    private RecyclerView.Adapter<Holder> adapter;
    public static final String qqNum="1781293168";

    //public DbManager db = x.getDb(BaseApplication.daoConfig);

    @Override
    public void initData() {
        super.initData();

        data = ((Goods) getIntent().getSerializableExtra("data"));

        infos=new ArrayList<>();

        DataWrap first = new DataWrap();
        first.setType(1);
        List<String> firstData = new ArrayList<>();


        firstData.add(data.getPicsrc());

        first.setData(firstData);
        infos.add(first);

        DataWrap second = new DataWrap();
        second.setType(2);
        infos.add(second);

        DataWrap third = new DataWrap();
        third.setType(3);
        third.setData("产品规格");
        infos.add(third);

        DataWrap fourth = new DataWrap();
        fourth.setType(3);
        fourth.setData("产品介绍");
        infos.add(fourth);

        DataWrap fifth = new DataWrap();
        fifth.setType(3);
        fifth.setData("配送时间");
        infos.add(fifth);

        DataWrap sixth = new DataWrap();
        sixth.setType(3);
        sixth.setData("注意事项");
        infos.add(sixth);
    }

    @Override
    public void initView() {
        super.initView();
        //initTop(R.mipmap.arrow_left,"商品详情",R.mipmap.qq_white);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {

                    if (data.getStockCount()>0) {
                        payGoods();

                    }else {
                        UIUtils.showToast("当前库存量为0");
                    }
                }else {
                    UserManager.toLogin();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {

                    if (data.getStockCount()>0) {
                        addGoods();
                    }else {
                        UIUtils.showToast("当前库存量为0");
                    }

                }else {
                    UserManager.toLogin();
                }
            }
        });

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);

                View view=null;
                switch (viewType){
                    case 1:
                        view=inflater.inflate(R.layout.mall_detail_first,parent,false);
                        break;
                    case 2:
                        view=inflater.inflate(R.layout.mall_detail_second,parent,false);
                        break;
                    case 3:
                        view=inflater.inflate(R.layout.mall_detail_third,parent,false);
                        break;
                    case 4:
                        view=inflater.inflate(R.layout.mall_detail_fourth,parent,false);
                        break;
                }

                return new Holder(view,viewType);
            }

            @Override
            public void onBindViewHolder(final Holder holder, int position) {
                final DataWrap dataWrap = infos.get(position);

                switch (dataWrap.getType()){
                    case 0:

                        break;
                    case 1:

                        final List<ImageView> views=new ArrayList<>();
                        final List<String> first = (List<String>) dataWrap.getData();

                        for (int i = 0; i < first.size(); i++) {
                            ImageView imageView = new ImageView(mActivity);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imageView.setTransitionName(NewsKindImageActivity.TRA_NAME);
                            }
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            views.add(imageView);
                        }
                        holder.text.setText(1+"/"+first.size());

                        holder.pager.setAdapter(new PagerAdapter() {
                            @Override
                            public int getCount() {

                                if (first != null) {
                                    return first.size();
                                }
                                return 0;
                            }

                            @Override
                            public boolean isViewFromObject(View view, Object object) {
                                return view==object;
                            }

                            @Override
                            public Object instantiateItem(ViewGroup container, int position) {
                                String path = first.get(position);
                                final ImageView child = views.get(position);

                                child.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(mActivity,NewsKindImageActivity.class);
                                        intent.putExtra("data",data);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,child, NewsKindImageActivity.TRA_NAME);
                                            startActivity(intent, options.toBundle());
                                        }else {
                                            startActivity(intent);
                                        }
                                    }
                                });
                                x.image().bind(child,path, xUtilsManager.getRectangleImageOption());

                                container.addView(child);

                                return views.get(position);
                            }

                            @Override
                            public void destroyItem(ViewGroup container, int position, Object object) {
                                container.removeView(views.get(position));
                            }

                        });
                        holder.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                holder.text.setText(position+1+"/"+first.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        break;

                    case 2:
                        holder.text.setText(data.getTitle());
                        holder.price.setText("价格：￥"+data.getPrice());

                        break;
                    case 3:

                        String titleStr = (String) dataWrap.getData();
                        holder.title.setText(titleStr);

                        if (position==2) {
                            holder.text.setText(data.getParams());
                        }else if (position==3){
                            holder.text.setText(data.getDesc());
                        }else if (position==4){
                            if (TextUtils.isEmpty(data.getDCTime())) {
                                holder.text.setText("具体时间请联系客服");
                            }else {
                                holder.text.setText(data.getDCTime());
                            }
                        }else if (position==5){
                            if (TextUtils.isEmpty(data.getAttention())) {
                                holder.text.setText("具体注意事项请联系客服");
                            }else {
                                holder.text.setText("" + data.getAttention());
                            }
                        }


                        break;
                }
            }

            @Override
            public int getItemCount() {
                if (infos != null) {
                    return infos.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int position) {
                return infos.get(position).getType();
            }
        };
        rec.setAdapter(adapter);
    }

    private void addGoods() {



        final ProgressDialog dialog=new ProgressDialog(mActivity);
        dialog.setMessage("加载中...");

        dialog.show();

        RequestParams entity = new RequestParams(UrlUtils.N_joinShopCart);

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId", data.getGoodsId()+"");
        entity.addBodyParameter("num","1");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {



                NetData netData = JSON.parseObject(result, NetData.class);

                Integer shopcarNum = JSON.parseObject(result).getInteger("shopcarNum");

                if (netData.getResult()==200){
                    Intent intent = new Intent();
                    intent.setAction("");
                    intent.putExtra("count",shopcarNum);
                    sendBroadcast(intent);

                    UIUtils.showToast("成功添加到购物车");

                }else {
                    UIUtils.showToast("添加失败");
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
                dialog.dismiss();


                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_FULLSCREEN
                                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        });
    }

    private void payGoods() {
        Intent intent = new Intent(mActivity,OrderGenerateActivity.class);

        List<CartGoods> cartGoodses = new ArrayList<CartGoods>();
        CartGoods e = new CartGoods();

        e.setGoodsId(data.getGoodsId());
        e.setCount(1);
        e.setDesc(data.getDesc());
        e.setPicsrc(data.getPicsrc());
        e.setPrice(data.getPrice());
        e.setTitle(data.getTitle());

        cartGoodses.add(e);

        intent.putExtra("goods", (Serializable) cartGoodses);
        intent.putExtra("flag",1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_detail);

        x.view().inject(this);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void btn_back(View view) {
        finish();
    }

    public void btn_qq(View view) {
        if (checkApkExist(this, "com.tencent.mobileqq")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+qqNum+"&version=1")));
        }else{
            Toast.makeText(this,"本机未安装QQ应用",Toast.LENGTH_SHORT).show();
        }
    }

    class Holder extends RecyclerView.ViewHolder{
        View parent;
        ViewPager pager;
        TextView text,price,title;

        public Holder(View itemView,int type) {
            super(itemView);

            parent=itemView;

            switch (type){
                case 0:

                    break;
                case 1:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    pager = ((ViewPager) itemView.findViewById(R.id.mall_pager));
                    break;
                case 2:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    price = ((TextView) itemView.findViewById(R.id.mall_price));
                    break;
                case 3:
                    text = ((TextView) itemView.findViewById(R.id.mall_text));
                    title = ((TextView) itemView.findViewById(R.id.mall_title));
                    break;
            }
        }
    }
}
