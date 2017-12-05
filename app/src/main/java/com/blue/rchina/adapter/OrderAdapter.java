package com.blue.rchina.adapter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.activity.OrderDetailActivity;
import com.blue.rchina.activity.PayActivity;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.mall.Order;
import com.blue.rchina.bean.mall.OrderGoods;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.ListViewForScrollView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;



/**
 * Created by cj on 2017/7/10.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {

    private List<Order> datas;
    private BaseActivity mActivity;
    private LayoutInflater inf;
    private View.OnClickListener listener;
    public BaseAdapter listAdapter;



    public OrderAdapter(final List<Order> datas, BaseActivity activity) {
        this.datas = datas;
        mActivity = activity;

        inf = LayoutInflater.from(mActivity);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = datas.get(((int) v.getTag()));
                switch (v.getId()) {

                    case R.id.order_parent:

                        break;
                    case R.id.order_handler:

                        switch (order.getOrderFlag()) {
                            case 0:
                                pay(order);
                                break;
                            case 1:
                                notification();
                                break;
                            case 2:
                                getTrnInfo(order);
                                break;
                            case 4:
                                //sureGet(order);
                                break;
                        }
                        break;
                    case R.id.order_delete:
                        if (order.getOrderFlag()==0) {
                            delete(order);
                        }else if (order.getOrderFlag()==2){
                            sureGet(order);
                        }
                        break;
                }
            }


        };

    }

    private void delete(final Order order) {

        AlertDialog dialog=new AlertDialog.Builder(mActivity)
                .setTitle("提示:")
                .setMessage("确定要删除该订单吗？订单删除后将不能够找回，如果您在订单中使用了优惠券，优惠券将会返还给您！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
                        progressDialog.setMessage("加载中...");
                        progressDialog.show();

                        RequestParams entity = new RequestParams(UrlUtils.N_deleteOrder);

                        entity.addBodyParameter("dataId",order.getOrderNo());
                        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");

                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                NetData netData = JSON.parseObject(result, NetData.class);
                                if (netData.getResult()==200){
                                    datas.remove(order);
                                    notifyDataSetChanged();
                                    UIUtils.showToast("订单删除成功");

                                    //refreshData();

                                }else{
                                    UIUtils.showToast("删除订单失败");
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
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .create();
        dialog.show();

    }

    private void getTrnInfo(Order order) {
        Intent intent=new Intent(mActivity,OrderDetailActivity.class);
        intent.putExtra("order",order);
        mActivity.startActivity(intent);
    }


    private void sureGet(final Order order) {

        AlertDialog dialog=new AlertDialog.Builder(mActivity)
                .setTitle("提示:")
                .setMessage("确认收货")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
                        progressDialog.setMessage("加载中...");
                        progressDialog.show();


                        RequestParams entity = new RequestParams(UrlUtils.N_confirmReceive);

                        entity.addBodyParameter("dataId",order.getOrderNo());
                        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");


                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                NetData netData = JSON.parseObject(result, NetData.class);

                                if (netData.getResult()==200){

                                    datas.remove(order);
                                    notifyDataSetChanged();
                                    UIUtils.showToast("确认收货成功");
                                }else {
                                    UIUtils.showToast(netData.getMessage());
                    /*200:操作成功
                    300:订单未支付
                    301:订单未发货
                    302:订单已完成
                    400:参数错误
                    401:请先登录
                    402:订单不存在
                    500:服务器异常*/
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
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .create();
        dialog.show();


    }

    private void notification() {

    }

    private void pay(Order order) {
        Intent intent=new Intent(mActivity, PayActivity.class);
        intent.putExtra("id",order.getOrderNo());
        intent.putExtra("money",order.getMoneySum());
        mActivity.startActivity(intent);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        Holder holder = null;
        View view = null;
        switch (viewType) {
            case 0:
                view = inf.inflate(R.layout.order_item, parent, false);
                holder = new Holder(view, viewType);
                break;

        }
        x.view().inject(holder, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Order order = datas.get(position);

        switch (order.getType()) {
            case 0:

                int state = order.getOrderFlag();
                holder.parent.setTag(position);
                holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delete(order);
                        return false;
                    }
                });
                holder.parent.setOnClickListener(listener);

                holder.handler.setVisibility(View.VISIBLE);



                if (state == 0) {
                    holder.state.setText("待付款");
                    holder.handler.setText("去付款");

                    holder.delete.setText("删除订单");
                    holder.delete.setVisibility(View.VISIBLE);
                } else if (state == 1) {
                    holder.state.setText("待发货");
                    //holder.handler.setText("提醒发货");
                    holder.handler.setVisibility(View.GONE);

                    holder.delete.setVisibility(View.GONE);
                } else if (state == 2) {
                    holder.state.setText("待收货");
                    holder.handler.setText("查看物流");

                    holder.delete.setText("确认收货");
                    holder.delete.setVisibility(View.VISIBLE);
                } else if (state == 4) {
                    holder.state.setText("已完成");
                    holder.handler.setText("再次购买");

                    holder.handler.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);
                }
                holder.handler.setTag(position);
                holder.handler.setOnClickListener(listener);

                holder.delete.setTag(position);
                holder.delete.setOnClickListener(listener);



                holder.number.setText(order.getOrderNo());
                holder.totalPrice.setText("￥" + order.getMoneySum());

                holder.listView.setAdapter(new MyListAdapter(order.getGoodsInfo()));

                break;
            case 1:

                break;
            case 2:

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

    class MyListAdapter extends BaseAdapter{

        private List<OrderGoods> goodsData;

        public MyListAdapter(List<OrderGoods> listdata) {
            goodsData=listdata;
        }

        @Override
        public int getCount() {
            if (goodsData != null) {
                return goodsData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = LayoutInflater.from(mActivity).inflate(R.layout.order_goods_item, parent, false);

            OrderGoods goods = goodsData.get(position);


            ImageView icon = (ImageView) inflate.findViewById(R.id.order_img);

            x.image().bind(icon,goods.getPicsrc(), xUtilsManager.getRectangleImageOption());

            ((TextView) inflate.findViewById(R.id.order_des)).setText(goods.getTitle());

            ((TextView) inflate.findViewById(R.id.order_count)).setText("￥"+goods.getPrice()+"(x"+goods.getCount()+")");

            ((TextView) inflate.findViewById(R.id.order_price)).setText("￥"+String.format("%.2f",goods.getPrice()*goods.getCount()));


            return inflate;
        }
    };

    class Holder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.order_parent)
        View parent;
        @ViewInject(R.id.order_count)
        TextView count;
        @ViewInject(R.id.order_number)
        TextView number;
        @ViewInject(R.id.order_state)
        TextView state;
        @ViewInject(R.id.order_des)
        TextView des;
        @ViewInject(R.id.order_price)
        TextView price;
        @ViewInject(R.id.order_totalPrice)
        TextView totalPrice;
        @ViewInject(R.id.order_handler)
        TextView handler;
        @ViewInject(R.id.order_img)
        ImageView img;
        @ViewInject(R.id.order_list)
        ListViewForScrollView listView;
        @ViewInject(R.id.order_delete)
        TextView delete;



        public Holder(View itemView, int type) {
            super(itemView);
        }
    }
}
