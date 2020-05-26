package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.CartGoods;
import com.blue.rchina.bean.Coupon;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.OrderPrice;
import com.blue.rchina.bean.mall.Address;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerateActivity extends BaseActivity {


    @ViewInject(R.id.cart_rec)
    RecyclerView rec;

    @ViewInject(R.id.cart_buy)
    TextView buy;

    @ViewInject(R.id.cart_goods_count)
    TextView goods_count;
    @ViewInject(R.id.cart_price)
    TextView price;


    @ViewInject(R.id.address)
    TextView address;
    @ViewInject(R.id.name)
    TextView name;
    @ViewInject(R.id.phone)
    TextView phone;

    @ViewInject(R.id.cart_address)
    View cart_address;

    @ViewInject(R.id.cart_coupon)
    TextView cart_coupon;
    @ViewInject(R.id.cart_all_coupon)
    LinearLayout cart_all_coupon;
    @ViewInject(R.id.cart_no_address)
    LinearLayout cart_no_address;


    private List<CartGoods> datas;
    /*记录选择的id*/
    private List<Long> selectedData;
    private RecyclerView.Adapter<Holder> adapter;
    private boolean isLoading;
    private int cPage = 2;

    private int cSelectedCount = 0;
    public static final int SELECT_ADDRESS = 200;
    public static final int SELECT_COUPON = 100;
    public Address addressEx;
    public Coupon coupon;
    public int flag;
    public double totalPrice;
    private View.OnClickListener listener;
    public OrderPrice orderPrice;
    private int number;

    @Override
    public void initView() {
        initTop(R.mipmap.left_white, "订单", -1);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart_all_coupon.getVisibility() == View.VISIBLE) {
                    cart_all_coupon.setVisibility(View.GONE);
                    cart_all_coupon.setAnimation(moveToViewBottom());
                }else if (cart_all_coupon.getVisibility() == View.GONE) {
                    cart_all_coupon.setVisibility(View.VISIBLE);
                    cart_all_coupon.setAnimation(moveToViewLocation());
                }
            }
        };

        cart_all_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CouponActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("money",totalPrice);
                startActivityForResult(intent, SELECT_COUPON);
            }
        });


        datas = new ArrayList<>();
        selectedData = new ArrayList<>();

        rec.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new RecyclerView.Adapter<Holder>() {

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mActivity).inflate(R.layout.goods_item, parent, false);

                Holder holder = new Holder(view);

                x.view().inject(holder, view);

                return holder;
            }

            @Override
            public void onBindViewHolder(final Holder holder, int position) {

                final CartGoods goods = datas.get(position);

                holder.goods_select.setVisibility(View.GONE);

                x.image().bind(holder.goods_img, goods.getPicsrc(), xUtilsManager.getRectangleImageOption());

                holder.goods_des.setText(goods.getTitle());
                holder.goods_price.setText("￥" + goods.getPrice() + " x " + goods.getCount());
                holder.total_price.setText("￥" + String.format("%.2f", goods.getPrice() * goods.getCount()));

                holder.goods_count.setText(goods.getCount() + "");

                holder.cart_bottom.setVisibility(View.GONE);
                holder.parent.setOnClickListener(listener);

            }

            @Override
            public int getItemCount() {
                if (datas != null) {
                    return datas.size();
                }
                return 0;
            }
        };
        rec.setAdapter(adapter);

        rec.setOnScrollListener(new RecyclerView.OnScrollListener() {

            /*滑动到4dp才算滑动了*/
            private int mScrollThreshold = 4;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoading) {
                        //getMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (isSignificantDelta) {
                    if (dy > 0) {
                        //onScrollUp();
                        if (cart_all_coupon.getVisibility() == View.GONE) {
                            cart_all_coupon.setVisibility(View.VISIBLE);
                            cart_all_coupon.setAnimation(moveToViewLocation());
                        }

                    } else {
                        //onScrollDown();

                        if (cart_all_coupon.getVisibility() == View.VISIBLE) {
                            cart_all_coupon.setVisibility(View.GONE);
                            cart_all_coupon.setAnimation(moveToViewBottom());
                        }
                    }
                }

            }

        });

    }

    private void updateBottom() {

        if (orderPrice != null) {
            totalPrice=orderPrice.getMoneySum();
        }
        if (coupon != null) {
            totalPrice -= coupon.getCoupon();
            if (totalPrice<0){
                totalPrice=0;
            }
        }


        if(orderPrice.getFreight()>0) {
            price.setText(String.format("¥%.2f(运费¥%.1f)", totalPrice, orderPrice.getFreight()));
        }else {
            price.setText(String.format("¥%.2f(免运费)", totalPrice));
        }


        cSelectedCount = datas.size();

        if (cSelectedCount < 99) {
            goods_count.setText(cSelectedCount + "");
        } else {
            goods_count.setText("99");
        }
    }


    @Override
    public void initData() {
        super.initData();

        Intent intent = getIntent();
        orderPrice = ((OrderPrice) intent.getSerializableExtra("info"));
        datas = ((List<CartGoods>) intent.getSerializableExtra("goods"));
        addressEx= (Address) intent.getSerializableExtra("address");
        flag = intent.getIntExtra("flag",-1);

        if (flag==1){
            number=intent.getIntExtra("num",1);
        }
        adapter.notifyDataSetChanged();


        //UIUtils.showToast("请选择收货地址");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyAddressActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
            }
        };
        cart_no_address.setOnClickListener(listener);
        cart_address.setOnClickListener(listener);


        if (addressEx != null) {
            address.setText(addressEx.getDistrict() + "-" + addressEx.getReceiveAddress());
            name.setText("收件人：" + addressEx.getReceiveName());
            phone.setText("电话：" + addressEx.getReceivePhone());
        }else {

            RequestParams entity = new RequestParams(UrlUtils.N_getReceiveInfo);
            entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
            x.http().post(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {


                    NetData netData = JSON.parseObject(result, NetData.class);

                    if (netData.getResult() == 200) {

                        List<Address> addresses = JSON.parseArray(netData.getInfo(), Address.class);

                        if (addresses != null && addresses.size() > 0) {
                            for (int i = 0; i < addresses.size(); i++) {
                                Address addressDefault = addresses.get(i);
                                if (addressDefault.getIsDefault()==1){

                                    addressEx = addressDefault;

                                    address.setText(addressEx.getDistrict() + "-" + addressEx.getReceiveAddress());
                                    name.setText("收件人：" + addressEx.getReceiveName());
                                    phone.setText("电话：" + addressEx.getReceivePhone());
                                    break;
                                }
                            }
                        } else {
                            cart_no_address.setVisibility(View.VISIBLE);
                            cart_address.setVisibility(View.GONE);
                        }
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

        updateBottom();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_pay);

        x.view().inject(this);

        initView();
        initData();

    }



    public void btn_buy(View view) {
        if (datas != null && datas.size() > 0) {

            if (addressEx!=null) {
                generateOrder(datas);
            }else {
                UIUtils.showToast("请选择收货地址");
            }
        } else {
            UIUtils.showToast("请选择要购买的物品");
        }
    }

    private void generateOrder(List<CartGoods> goodses) {


        if(addressEx==null){
            UIUtils.showToast("请选择收货地址");
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("正在生成订单...");
        dialog.show();

        StringBuilder arg = new StringBuilder();

        arg.append("[");

        for (int i = 0; i < goodses.size(); i++) {
            arg.append("{");
            arg.append("\"" + "id" + "\"" + ":" + "\"" + goodses.get(i).getGoodsId() + "\"");
            arg.append("}");

            if (i != goodses.size() - 1) {
                arg.append(",");
            }
        }

        arg.append("]");

        RequestParams entity = new RequestParams(UrlUtils.N_getOrderId);

        String value = arg.toString();


        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("arg1", value);

        if (flag!=-1) {
            entity.addBodyParameter("type", "0");
            entity.addBodyParameter("num",number+"");
        }else {
            entity.addBodyParameter("type", "1");
        }
        if (coupon!=null) {
            entity.addBodyParameter("flag", "1");
            entity.addBodyParameter("dataId",coupon.getCouponId()+"");

        }else {
            entity.addBodyParameter("flag","0");
            entity.addBodyParameter("dataId","");
        }


        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                JSONObject object = JSON.parseObject(result);

                Integer code = object.getInteger("result");
                if (code == 200) {

                    String shopcartNum = object.getString("shopcartNum");

                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("id", object.getString("orderNo"));
                    intent.putExtra("money",totalPrice);
                    startActivity(intent);

                    finish();
                } else if (code == 300) {
                    UIUtils.showToast("请先设置收货信息");
                    /*200:操作成功
                    300:请先设置收货信息
                    301:请先选择商品
                    400:参数错误
                    401:请先登录
                    500:服务器异常*/

                } else if (code == 301) {
                    UIUtils.showToast("请先选择商品");
                } else if (code==302){
                    UIUtils.showToast("优惠券不可用");
                }else {
                    UIUtils.showToast("生成订单失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.w("3333",ex.getMessage());
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

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_ADDRESS) {
            if (data != null) {

                if (addressEx == null) {
                    cart_address.setVisibility(View.VISIBLE);
                    cart_no_address.setVisibility(View.GONE);
                }

                addressEx = (Address) data.getSerializableExtra("address");
                address.setText(addressEx.getDistrict() + "-" + addressEx.getReceiveAddress());
                name.setText("收件人：" + addressEx.getReceiveName());
                phone.setText("电话：" + addressEx.getReceivePhone());
            }else {

                if (addressEx==null) {
                    cart_address.setVisibility(View.GONE);
                    cart_no_address.setVisibility(View.VISIBLE);
                }
            }
        }else if (requestCode == SELECT_COUPON) {
            if (data != null) {

                int flag = data.getIntExtra("flag", 0);

                if (flag == 1) {
                    cart_coupon.setText("不使用优惠券");
                    cart_coupon.setTextColor(getResources().getColor(R.color.xxlight_gray));

                    coupon=null;
                } else {
                    coupon = (Coupon) data.getSerializableExtra("coupon");

                    if (true) {
                        cart_coupon.setText("￥" + coupon.getCoupon() + "优惠券");
                        cart_coupon.setTextColor(getResources().getColor(R.color.price_red));
                    }
                }
                updateBottom();
            }
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.goods_select)
        ImageView goods_select;
        @ViewInject(R.id.goods_add)
        ImageView goods_add;
        @ViewInject(R.id.goods_minus)
        ImageView goods_minus;
        @ViewInject(R.id.order_img)
        ImageView goods_img;

        @ViewInject(R.id.goods_count)
        TextView goods_count;
        @ViewInject(R.id.order_des)
        TextView goods_des;
        @ViewInject(R.id.goods_price)
        TextView goods_price;
        @ViewInject(R.id.goods_total_price)
        TextView total_price;

        @ViewInject(R.id.goods_delete)
        TextView goods_delete;

        @ViewInject(R.id.cart_bottom)
        View cart_bottom;

        @ViewInject(R.id.goods_parent)
        View parent;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
