package com.blue.rchina.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.Main2Activity;
import com.blue.rchina.R;
import com.blue.rchina.activity.MallDetailActivity;
import com.blue.rchina.base.BaseFragment;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.Goods;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.SPUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.ImageCycleView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_mall)
public class MallFragment extends BaseFragment {

    @ViewInject(R.id.mall_rec)
    RecyclerView rec;

    @ViewInject(R.id.framelayout)
    PtrFrameLayout mPtrFrameLayout;
    @ViewInject(R.id.mall_cycle)
    ViewPager cycleView;

    private List<Goods> datas;
    private List<Goods> hotGoods;
    public List<View> contains;

    private GridLayoutManager gridLayoutManager;
    private RecyclerView.Adapter<Holder> adapter;
    private int cPage=2;
    private boolean isLoading;
    public AreaInfo area;
    private boolean isLocal;
    public Intent intent;

    public MallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = x.view().inject(this, inflater, container);
        initData();
        initView(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        freshMallCount();
    }

    private void freshMallCount() {
        String n_getGoodsInfo;
        if (!isLocal) {
            n_getGoodsInfo=UrlUtils.N_getAreaGoodsInfo;
        }else {
            n_getGoodsInfo=UrlUtils.N_getGoodsInfo;
        }

        RequestParams entity = new RequestParams(n_getGoodsInfo);
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        entity.addBodyParameter("page", "1");
        if (area != null) {
            entity.addBodyParameter("areaId",area.getAreaId());
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {

                    Integer shopcarNum = JSON.parseObject(result).getInteger("shopcarNum");

                    /*初始化购物车数量*/

                    intent.putExtra("count", shopcarNum);
                    mActivity.sendBroadcast(intent);
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

            }
        });
    }

    @Override
    public void initView(View view) {

        super.initView(view);
        hotGoods=new ArrayList<>();
        contains = new ArrayList<>();


        ViewGroup.LayoutParams layoutParams = cycleView.getLayoutParams();
        layoutParams.height= (int) (UIUtils.getWindowWidth(mActivity)*0.5);
        cycleView.setLayoutParams(layoutParams);

        gridLayoutManager = new GridLayoutManager(mActivity, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==0){
                    if (datas.get(0).getLayoutType()==0) {
                        return 2;
                    }else {
                        return 1;
                    }
                }
                return 1;
            }
        });

        rec.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                LayoutInflater from = LayoutInflater.from(mActivity);
                View view= null;


                switch (viewType) {
                    case 0:

                        view = from.inflate(R.layout.news_item_head, parent, false);
                        break;
                    case 1:

                        view=from.inflate(R.layout.mall_item,parent,false);
                        break;
                }

                return new Holder(view,viewType);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {

                final Goods goods = datas.get(position);

                int layoutType = goods.getLayoutType();
                if (layoutType ==1) {
                    holder.parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, MallDetailActivity.class);
                            intent.putExtra("data", goods);
                            startActivity(intent);
                        }
                    });

                    holder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UserManager.isLogin()) {

                                if (goods.getStockCount() > 0) {
                                    addGoods(goods);
                                } else {
                                    UIUtils.showToast("当前库存量为0");
                                }
                            } else {
                                UserManager.toLogin();
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(goods.getPicsrc())) {
                        x.image().bind(holder.img, goods.getPicsrc(), xUtilsManager.getRectangleImageOption());
                    }


                    holder.title.setText(goods.getTitle());

                    holder.price.setText("￥" + goods.getPrice());

                    holder.count.setText("库存量" + goods.getStockCount() + "件");
                }else if (layoutType==0){

                    final ImageCycleView circle = holder.pager;
                    if (hotGoods.size()>1) {
                        circle.setAutoCycle(true);
                    }else {
                        circle.setAutoCycle(false);
                    }
                    circle.setCycleDelayed(4000);//设置自动轮播循环时间
                    circle.setIndicationStyle(ImageCycleView.IndicationStyle.COLOR,
                            mActivity.getResources().getColor(R.color.white),
                            mActivity.getResources().getColor(R.color.primaryColor), 0.6f);

                    ViewGroup.LayoutParams layoutParams = circle.getLayoutParams();
                    layoutParams.height= (int) (UIUtils.getWindowWidth(mActivity)*0.5);
                    circle.setLayoutParams(layoutParams);
                    List<ImageCycleView.ImageInfo> list=new ArrayList();


                    if (hotGoods.size()>0) {

                        for (int i = 0; i < hotGoods.size(); i++) {
                            Goods article = hotGoods.get(i);
                            String msg = article.getPicsrc();
                            String[] split = msg.split(";");

                    /*对于多张图，取第一张去显示*/
                            String img = split[0];
                            if (img == null || img.equals("") || img.equalsIgnoreCase("null")) {
                                list.add(new ImageCycleView.ImageInfo(R.mipmap.no_data, article.getTitle(), article));
                                continue;
                            }
                            list.add(new ImageCycleView.ImageInfo(img, article.getTitle(), article));

                        }

                        circle.setListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        //使用网络加载图片



                        circle.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {

                            @Override

                            public void onClick(View imageView, ImageCycleView.ImageInfo imageInfo) {

                                Goods value = (Goods) imageInfo.value;
                                /*Intent intent = new Intent(mActivity, MallDetailActivity.class);
                                intent.putExtra("data", value);
                                startActivity(intent);*/
                            }

                        });


                        circle.loadData(list, new ImageCycleView.LoadImageCallBack() {

                            @Override

                            public ImageView loadAndDisplay(final ImageCycleView.ImageInfo imageInfo) {
                                ImageView imageView = new ImageView(mActivity);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                ImageOptions imageOptions = new ImageOptions.Builder()
                                        .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                                        .setFailureDrawableId(R.color.xlight_gray)
                                        .setLoadingDrawableId(R.color.xlight_gray)
                                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                                        .build();

                                x.image().bind(imageView, ((String) imageInfo.image),imageOptions);

                                return imageView;
                            }

                        });
                    }
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
                return datas.get(position).getLayoutType();
            }
        };
        rec.setAdapter(this.adapter);




        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        header.setLastUpdateTimeKey(this.getClass().getName());

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(in.srain.cube.views.ptr.PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rec, header);
                //return !checkContentCanPullDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final in.srain.cube.views.ptr.PtrFrameLayout frame) {
                fresh();
            }
        });

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rec.canScrollVertically(1) && datas.size() > 0) {
                    if (!isLoading) {
                        getGoods(cPage,2);
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();


        intent = new Intent();
        intent.setAction("action_change_mall");

        datas=new ArrayList<>();

        Bundle arguments = getArguments();
        if (arguments != null) {
            area=((AreaInfo) arguments.getSerializable("data"));
            isLocal=false;
        }else {
            String areaStr = SPUtils.getSP().getString("area", "");
            area = JSON.parseObject(areaStr, AreaInfo.class);

            isLocal=true;
        }

        fresh();
    }
    private void fresh(){
        /*type 1是刷新2是加载更多*/
        getGoods(1,1);
    }

    private void getGoods(int page, final int type) {
        isLoading = true;
        /*1是刷新2是加载*/
        String n_getGoodsInfo;
        if (!isLocal) {
            n_getGoodsInfo=UrlUtils.N_getAreaGoodsInfo;
        }else {
            n_getGoodsInfo=UrlUtils.N_getGoodsInfo;
        }

        RequestParams entity = new RequestParams(n_getGoodsInfo);
        if (UserManager.isLogin()) {
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        }
        entity.addBodyParameter("page", page + "");
        if (area != null) {
            entity.addBodyParameter("areaId",area.getAreaId());
        }
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                        /*刷新*/
                    if (type == 1) {
                        Integer shopcarNum = JSON.parseObject(result).getInteger("shopcarNum");

                        /*初始化购物车数量*/
                        //((Main2Activity) getActivity()).setMallCount(shopcarNum);
                        intent.putExtra("count",shopcarNum);
                        mActivity.sendBroadcast(intent);

                        datas.clear();
                        hotGoods.clear();
                        cPage = 2;
                        /*轮播图*/
                        List<Goods> hotGoods1 = JSON.parseArray(netData.getHot(), Goods.class);

                        if (hotGoods1 != null && hotGoods1.size() > 0) {
                            /*hotGoods存放多个商品。用于轮播*/
                            hotGoods.addAll(hotGoods1);
                            Goods e = new Goods();
                            e.setLayoutType(0);
                            datas.add(e);
                        }

                        /*商品*/
                        List<Goods> goodses = JSON.parseArray(netData.getInfo(), Goods.class);

                        if (goodses != null && goodses.size() > 0) {
                            datas.addAll(goodses);
                        }

                        adapter.notifyDataSetChanged();


                    } else if (type == 2) {


                        /*商品*/
                        List<Goods> goodses = JSON.parseArray(netData.getInfo(), Goods.class);

                        if (goodses != null && goodses.size() > 0) {
                            datas.addAll(goodses);
                            /*的确有数据*/
                            cPage++;
                        }

                        adapter.notifyDataSetChanged();
                    }

                    if (datas.size() == 0) {
                        isNodata(true);
                    } else {
                        isNodata(false);
                    }
                } else {
                    UIUtils.showToast("服务器错误");
                }

            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                UIUtils.showToast("网络请求失败");
                if (datas.size() == 0) {
                    isNodata(true);
                } else {
                    isNodata(false);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mPtrFrameLayout.refreshComplete();
                isLoading = false;

                isHideLoading(true);
            }

        });
    }


    private void addGoods(final Goods goods) {

        final ProgressDialog dialog=new ProgressDialog(mActivity);
        dialog.setMessage("加载中...");

        dialog.show();

        RequestParams entity = new RequestParams(UrlUtils.N_joinShopCart);

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId", goods.getGoodsId()+"");
        entity.addBodyParameter("num","1");

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);

                Integer shopcarNum = JSON.parseObject(result).getInteger("shopcarNum");

                intent.putExtra("count",shopcarNum);
                mActivity.sendBroadcast(intent);

                if (isLocal) {
                    ((Main2Activity) getActivity()).setMallCount(shopcarNum);
                }

                if (netData.getResult()==200){

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
            }
        });

    }
    private boolean checkContentCanPullDown(PtrFrameLayout frame, View content, View header) {

        int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

        if (firstVisibleItemPosition==0&&rec.getChildAt(0).getY()==0){
            return false;
        }

        return true;
    }



    class Holder extends RecyclerView.ViewHolder{

        View parent;
        TextView title,price,count;
        ImageView img,add;
        ImageCycleView pager;
        public Holder(View itemView,int type) {
            super(itemView);

            parent=itemView;

            if (type==1) {
                img = (ImageView) itemView.findViewById(R.id.goods_img);
                add = (ImageView) itemView.findViewById(R.id.mall_add);
                title = (TextView) itemView.findViewById(R.id.goods_title);
                price = (TextView) itemView.findViewById(R.id.mall_goods_price);
                count = (TextView) itemView.findViewById(R.id.mall_sale_count);

                int windowWidth = UIUtils.getWindowWidth(mActivity);

                ViewGroup.LayoutParams params = img.getLayoutParams();
                //设置图片的相对于屏幕的宽高比
                //params.width = windowWidth/2;
                params.height = (int) (windowWidth / 2.2);
                img.setLayoutParams(params);
            }else {
                pager= (ImageCycleView) itemView.findViewById(R.id.image_cycle);
            }
        }
    }
}
