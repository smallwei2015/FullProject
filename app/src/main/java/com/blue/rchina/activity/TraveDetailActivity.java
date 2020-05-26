package com.blue.rchina.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.bean.TraveDetail;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.manager.xUtilsManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

@ContentView(R.layout.activity_trave_detail)
public class TraveDetailActivity extends BaseActivity {

    @ViewInject(R.id.trave_back)
    ImageView back;
    @ViewInject(R.id.phone)
    LinearLayout phone;
    @ViewInject(R.id.collect)
    LinearLayout collect;
    @ViewInject(R.id.order)
    TextView order;
    @ViewInject(R.id.trave_detail_pager)
    ViewPager pager;
    @ViewInject(R.id.trave_tag)
    TextView tag;
    @ViewInject(R.id.trave_share)
    ImageView share;


    @ViewInject(R.id.trave_detail_title)
    TextView title;
    @ViewInject(R.id.trave_detail_address)
    TextView address;
    @ViewInject(R.id.trave_detail_time)
    TextView time;
    @ViewInject(R.id.trave_detail_des)
    TextView des;
    @ViewInject(R.id.trave_detail_price)
    TextView price;


    private List<String> images;
    private List<ImageView> views;
    public PagerAdapter adapter;
    public TraveDetail detail;
    private String dataId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 允许使用transitions
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //getWindow().setSharedElementEnterTransition(new Explode());
            //getWindow().setSharedElementExitTransition(new Explode());//new Slide()  new Fade()
        }
        x.view().inject(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void initData() {
        super.initData();
        dataId = getIntent().getStringExtra("data");

        images=new ArrayList<>();
        views=new ArrayList<>();

    }

    private void getDetail() {


        isHideLoading(false);


        RequestParams entity = new RequestParams(UrlUtils.N_getScenicInfo);
        entity.addBodyParameter("dataId",dataId==null?"":dataId);
        entity.addBodyParameter("appuserId",UserManager.isLogin()?UserManager.getUser().getAppuserId()+"":"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.w("vode",result);

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==200){
                    detail = JSON.parseObject(netData.getInfo(), TraveDetail.class);

                    des.setText(detail.getContent());
                    collect.setSelected(detail.getCollectState()==1);
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShare(detail);
                        }
                    });

                    title.setText(detail.getTitle());
                    address.setText(detail.getLocation());
                    time.setText(detail.getOpenTime());
                    price.setText("￥"+detail.getPrice());


                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri dataU = Uri.parse("tel:" + detail.getPhone());
                            intent.setData(dataU);
                            startActivity(intent);

                        }
                    });

                    if (detail.getManyPic()!=null){

                        images.addAll(detail.getManyPic());
                        tag.setText(1+"/"+images.size());

                        for (int i = 0; i < images.size(); i++) {
                            ImageView imageView = new ImageView(mActivity);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                imageView.setTransitionName(NewsKindImageActivity.TRA_NAME);
                            }
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            views.add(imageView);
                        }

                        adapter.notifyDataSetChanged();
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
                isHideLoading(true);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();

        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) back.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,0,0);

            layoutParams = (RelativeLayout.LayoutParams) share.getLayoutParams();
            layoutParams.setMargins(0,layoutParams.topMargin+statusBarHeight,layoutParams.rightMargin,0);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail!=null) {
                    if (detail.getCollectState()==1){
                        collectTrave(v,2);
                    }else {
                        collectTrave(v, 1);
                    }
                }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIUtils.showToast("该功能即将开通");
                buy();
            }
        });

        adapter = new PagerAdapter() {
            @Override
            public int getCount() {

                if (images != null) {
                    return images.size();
                }
                return 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                String path = images.get(position);
                final ImageView child = views.get(position);

                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mActivity,NewsKindImageActivity.class);
                        intent.putExtra("data",detail);
                        intent.putExtra("position",position);
                        startActivityForResult(intent,200);

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

        };
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tag.setText(position+1+"/"+images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        getDetail();
    }

    private void collectTrave(final View v, int state) {
        if (!UserManager.isLogin()){
            UserManager.toLogin();
            return;
        }

        if (detail == null) {
            return;
        }
        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_collectScenic);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId",detail.getScenicId());
        entity.addBodyParameter("state",state+"");
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult()==201){
                    UIUtils.showToast("收藏成功");
                    v.setSelected(true);
                    detail.setCollectState(1);
                }else if (netData.getResult()==202){
                    UIUtils.showToast("取消收藏");
                    v.setSelected(false);
                    detail.setCollectState(2);
                }else {
                    UIUtils.showToast(netData.getMessage());
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
                isHideLoading(true);
            }
        });
    }

    private void buy() {
        if (!UserManager.isLogin()){
            UserManager.toLogin();
            return;
        }

        if (detail == null) {
            return;
        }
        isHideLoading(false);
        RequestParams entity = new RequestParams(UrlUtils.N_getTicketOrderId);
        entity.addBodyParameter("appuserId",UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("dataId",dataId);
        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("result")==200){
                    String orderNo = object.getString("orderNo");

                    Intent intent=new Intent(mActivity,PayActivity.class);
                    intent.putExtra("id",orderNo);
                    intent.putExtra("money",detail.getPrice());
                    intent.putExtra("flag",2);
                    startActivity(intent);
                }else {
                    UIUtils.showToast(object.getString("message"));
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
                isHideLoading(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isHideLoading(true);
    }

    private void showShare(final TraveDetail data) {
        String path=data.getShareLink();
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(data.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(data.getDesc());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(FileUtils.APP_LOGO);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("融城中国");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);

        oks.setImageUrl(data.getPicsrc());

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

                if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    //微信必须要设置具体的方分享类型
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                } else if (platform.getName().equals(WechatMoments.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setTitle(data.getDesc());
                }

            }
        });
        // 启动分享GUI
        oks.show(this);
    }

}
