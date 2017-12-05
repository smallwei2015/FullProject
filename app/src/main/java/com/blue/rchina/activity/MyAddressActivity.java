package com.blue.rchina.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.mall.Address;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

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

public class MyAddressActivity extends BaseActivity {


    private static final int ADD_ADDRESS = 100;
    private static final int EDIT_ADDRESS = 200;
    @ViewInject(R.id.address_ptr)
    PtrClassicFrameLayout ptrFrame;

    @ViewInject(R.id.address_rec)
    RecyclerView rec;
    @ViewInject(R.id.address_bar)
    ContentLoadingProgressBar bar;


    private List<Address> datas;
    private RecyclerView.Adapter<Holder> adapter;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "收货地址", -1);

        PtrClassicDefaultHeader ptrUIHandler = new PtrClassicDefaultHeader(mActivity);
        ptrFrame.addPtrUIHandler(ptrUIHandler);
        ptrFrame.setHeaderView(ptrUIHandler);

        ptrUIHandler.setLastUpdateTimeKey(getClass().getName());

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getAddress();
            }
        });


        datas = new ArrayList<>();
        rec.setLayoutManager(new LinearLayoutManager(mActivity));

        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.address_item, parent, false);

                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(final Holder holder, final int position) {

                final Address address = datas.get(position);

                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (address.getIsDefault()==1) {
                            Intent intent = new Intent();

                            intent.putExtra("address", address);
                            setResult(200, intent);
                            finish();
                        }else {
                            UIUtils.showToast("只能选择默认地址为收货地址");
                        }
                    }
                });
                holder.address.setText(address.getDistrict() + "-" + address.getReceiveAddress());

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editAddres(address);
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (address.getIsDefault() == 1) {
                            UIUtils.showToast("默认地址不可以删除");
                        } else {
                            modiftAddress(1, address);
                        }
                    }
                });

                holder.name.setText("收件人：" + address.getReceiveName());

                holder.phone.setText("电话：" + address.getReceivePhone());

                holder.address_normal.setSelected(address.getIsDefault() == 1);

                holder.address_normal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int isDefault = address.getIsDefault();

                        if (isDefault == 1) {
                            UIUtils.showToast("默认地址不可以取消");
                            return;
                        }
                        AlertDialog dialog = new AlertDialog.Builder(mActivity).setTitle("提示：")
                                .setMessage("确认要修改默认地址吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //modiftAddress(0, address);
                                        setDefaultReceiveInfo(address);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();


                    }
                });

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

    }


    @Override
    public void initData() {
        super.initData();

        if (UserManager.isLogin()) {
            getAddress();
        } else {

        }
    }

    private void getAddress() {
        RequestParams entity = new RequestParams(UrlUtils.N_getReceiveInfo);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {

                    List<Address> addresses = JSON.parseArray(netData.getInfo(), Address.class);

                    if (addresses != null && addresses.size() > 0) {

                        isNodata(false);
                        datas.clear();
                        datas.addAll(addresses);
                    } else {
                        UIUtils.showToast("暂无收货地址，请前往添加");
                        isNodata(true);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.showToast("网络请求失败");
                isNodata(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ptrFrame.refreshComplete();
                if (bar.isShown()) {
                    bar.hide();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        x.view().inject(this);
        initView();

        initData();
    }

    public void btn_addAddress(View view) {
        Intent intent = new Intent(mActivity, AddAddressActivity.class);
        intent.putExtra("flag", 100);
        startActivityForResult(intent, ADD_ADDRESS);
    }

    private void editAddres(Address address) {
        Intent intent = new Intent(mActivity, AddAddressActivity.class);
        intent.putExtra("flag", 200);
        intent.putExtra("address", address);
        startActivityForResult(intent, EDIT_ADDRESS);
    }

    private void modiftAddress(final int flag, final Address address) {
        /*flag 0修改默认地址，1删除地址*/

        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中...");
        dialog.show();

        RequestParams entity = new RequestParams(UrlUtils.N_setDefaultReceiveInfo);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("dataId", address.getReceiveId() + "");
        entity.addBodyParameter("flag", "0");
        entity.addBodyParameter("arg1", address.getReceiveName());
        entity.addBodyParameter("arg2", address.getReceivePhone());
        entity.addBodyParameter("arg3", address.getReceiveAddress());
        entity.addBodyParameter("arg4", address.getDistrict());
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {
                    List<Address> addresses = JSON.parseArray(netData.getInfo(), Address.class);

                    datas.clear();
                    datas.addAll(addresses);
                    adapter.notifyDataSetChanged();

                    UIUtils.showToast("修改成功");
                } else {
                    UIUtils.showToast("修改失败");
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

    private void setDefaultReceiveInfo(final Address address) {

        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("修改中...");
        dialog.show();

        RequestParams entity = new RequestParams();

        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId() + "");
        entity.addBodyParameter("dataId", address.getReceiveId() + "");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                NetData netData = JSON.parseObject(result, NetData.class);

                if (netData.getResult() == 200) {

                    List<Address> addresses = JSON.parseArray(netData.getInfo(), Address.class);

                    datas.clear();
                    datas.addAll(addresses);
                    adapter.notifyDataSetChanged();

                    UIUtils.showToast("修改成功");
                } else {
                    UIUtils.showToast("修改失败");
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

    private void deleteAddress(Address address) {

        datas.remove(address);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Address address = (Address) data.getSerializableExtra("address");
            if (requestCode == ADD_ADDRESS) {
                datas.add(address);
                adapter.notifyItemInserted(datas.size());
            } else if (requestCode == EDIT_ADDRESS) {

                /*for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getId() == address.getId()) {
                        datas.remove(datas.get(i));
                        datas.add(i, address);
                    }
                }

                adapter.notifyDataSetChanged();*/
                getAddress();
            }

        }
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView name, phone, address, delete, edit;
        ImageView address_normal;

        View parent;

        public Holder(View itemView) {
            super(itemView);

            parent = itemView;
            name = ((TextView) itemView.findViewById(R.id.name));
            phone = ((TextView) itemView.findViewById(R.id.phone));
            address = ((TextView) itemView.findViewById(R.id.address));
            delete = ((TextView) itemView.findViewById(R.id.address_delete));
            edit = ((TextView) itemView.findViewById(R.id.address_edit));
            address_normal = (ImageView) itemView.findViewById(R.id.address_normal);
        }
    }
}
