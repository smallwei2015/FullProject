package com.blue.rchina.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Coupon;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import net.wujingchao.android.view.SimpleTagImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CouponActivity extends BaseActivity {


    @ViewInject(R.id.coupon_ptr)
    PtrClassicFrameLayout ptrFrame;
    @ViewInject(R.id.coupon_rec)
    RecyclerView rec;
    @ViewInject(R.id.coupon_not)
    TextView not;


    private List<Coupon> datas;
    private RecyclerView.Adapter<Holder> adapter;
    private boolean isLoading;
    public int flag;
    public double money;
    public int windowWidth;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "我的优惠券", -1);

        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("flag", 1);
                setResult(200, intent);

                finish();
            }
        });


        PtrClassicDefaultHeader ptrUIHandler = new PtrClassicDefaultHeader(mActivity);
        ptrFrame.setHeaderView(ptrUIHandler);
        ptrFrame.addPtrUIHandler(ptrUIHandler);

        ptrUIHandler.setLastUpdateTimeKey(getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                fresh();
            }
        });

        datas = new ArrayList<>();

        rec.setLayoutManager(new LinearLayoutManager(mActivity));

        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mActivity).inflate(R.layout.coupon_item, parent, false);


                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                final Coupon coupon = datas.get(position);

                ViewGroup.LayoutParams layoutParams =  holder.parent.getLayoutParams();
                layoutParams.height=windowWidth/2;


                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (coupon.getState()==0) {

                            if (flag==1) {
                                //购物车选择优惠券

                                double v1 = Double.parseDouble(coupon.getUseCondition());

                                if (money>v1&&money>coupon.getCoupon()) {
                                    Intent intent = new Intent();
                                    intent.putExtra("coupon", coupon);
                                    setResult(200, intent);

                                    finish();
                                }else {

                                    if (money<v1) {
                                        UIUtils.showToast("当前订单金额不满足使用条件");
                                    }else {
                                        UIUtils.showToast("订单金额要大于优惠券金额");
                                    }
                                }


                            }
                        }
                    }
                });

                holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(coupon.getRedeemCode());

                        UIUtils.showToast("兑换码已经复制");

                        return false;
                    }
                });
                holder.number.setText(coupon.getCoupon() + "");
                if (coupon.getType() == 0) {
                    holder.kind.setText("全平台红包");
                } else {
                    holder.kind.setText("无效红包");
                }

                holder.deadline.setText(".有效期至" + coupon.getDeathDate());
                holder.condition1.setText(".兑换码："+coupon.getRedeemCode());
                holder.condition2.setText(".满" + coupon.getUseCondition() + "可以使用");
                
                /*0未使用1已使用
                2已过期*/
                int state = coupon.getState();
                if (state == 0) {
                    holder.coupon_tag.setTagText("未使用");
                    holder.coupon_tag.setTagBackgroundColor(Color.parseColor("#27CDC0"));
                } else if (state == 1) {
                    holder.coupon_tag.setTagText("已使用");
                    holder.coupon_tag.setTagBackgroundColor(Color.parseColor("#cccccc"));
                } else {
                    holder.coupon_tag.setTagText("已过期");
                    holder.coupon_tag.setTagBackgroundColor(Color.parseColor("#999999"));
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
                return super.getItemViewType(position);
            }
        };
        rec.setAdapter(adapter);

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoading) {
                        loadMore();
                    } else {
                        UIUtils.showToast("加载中...");
                    }
                }
            }
        });
    }

    private void fresh() {
        getData(0, 1, 3);
    }

    private void loadMore() {
        isLoading = true;
    }

    private void getData(final int type, int page, int state) {

                /*0未使用1已使用
        2已过期3全部*/
        RequestParams entity = new RequestParams(UrlUtils.N_getCouponInfoByAppuser);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("state", state + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    List<Coupon> coupons = JSON.parseArray(netData.getInfo(), Coupon.class);

                    if (type == 0) {
                        datas.clear();
                        if (coupons.size() == 0) {
                            UIUtils.showToast("暂无优惠券");
                            isNodata(true);
                        }else {
                            isNodata(false);
                        }
                    }


                    datas.addAll(coupons);
                    adapter.notifyDataSetChanged();
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
                ptrFrame.refreshComplete();
                isHideLoading(true);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        windowWidth = UIUtils.getWindowWidth(mActivity);
        flag = getIntent().getIntExtra("flag",-1);
        money = getIntent().getDoubleExtra("money",0);

        if (flag==1){
            not.setVisibility(View.VISIBLE);
        }else {
            not.setVisibility(View.GONE);
        }

        fresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        x.view().inject(this);
        initView();
        initData();
    }

    class Holder extends RecyclerView.ViewHolder {

        View parent;
        TextView condition1, condition2, deadline, number, kind;
        SimpleTagImageView coupon_tag;

        public Holder(View itemView) {
            super(itemView);

            parent = itemView;

            kind = ((TextView) itemView.findViewById(R.id.kind));

            number = ((TextView) itemView.findViewById(R.id.money));
            deadline = ((TextView) itemView.findViewById(R.id.deadline));
            condition1 = ((TextView) itemView.findViewById(R.id.condition1));
            condition2 = ((TextView) itemView.findViewById(R.id.condition2));

            coupon_tag= (SimpleTagImageView) itemView.findViewById(R.id.coupon_tag);
        }
    }
}
