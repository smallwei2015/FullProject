package com.blue.rchina.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.adapter.DragAdapter;
import com.blue.rchina.adapter.OtherAdapter;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.Channel;
import com.blue.rchina.bean.NetData;
import com.blue.rchina.utils.UIDUtils;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;
import com.blue.rchina.views.DragGridView;
import com.blue.rchina.views.OtherGridView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NewsSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    /**
     * 用户栏目的GRIDVIEW
     */
    @ViewInject(R.id.userGridView)
    private DragGridView userGridView;
    /**
     * 其它栏目的GRIDVIEW
     */
    @ViewInject(R.id.otherGridView)
    private OtherGridView otherGridView;


    /**
     * 用户栏目对应的适配器，可以拖动
     */
    DragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    OtherAdapter otherAdapter;
    /**
     * 其它栏目列表
     */
    ArrayList<Channel> otherChannelList = new ArrayList<Channel>();
    /**
     * 用户栏目列表
     */
    ArrayList<Channel> userChannelList = new ArrayList<Channel>();
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;

    private List<Channel> all;
    private List<Channel> selects;
    public List<Channel> other;
    public Channel channel;


    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white, "栏目筛选", R.mipmap.sure);

        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    @Override
    public void onRightIconClick(View view) {
        super.onRightIconClick(view);
        view.setEnabled(false);
        updateSort(view);
    }

    @Override
    public void UIfinish() {
        //super.UIfinish();
        /*点击返回键事件处理*/

        onBackPressed();

    }

    /*避免前端出现传递了重复数据的情况*/
    private ArrayList<Channel> removeDuplicateItem(List<Channel> datas){
        ArrayList<Channel> news=new ArrayList<>();

        for (Channel c : datas) {
            if (news.indexOf(c)<0){
                news.add(c);
            }
        }
        return news;
    }

    private void updateSort(final View view) {
        RequestParams entity = new RequestParams(UrlUtils.N_sortChannel);
        entity.addBodyParameter("areaId",channel.getAreaId());
        entity.addBodyParameter("dataId", UIDUtils.getUID(mActivity));
        if (userChannelList != null) {

            userChannelList=removeDuplicateItem(userChannelList);

            StringBuilder builder=new StringBuilder();
            for (int i = 0; i < userChannelList.size(); i++) {
                builder.append(userChannelList.get(i).getId());
                if (i!=userChannelList.size()-1){
                    builder.append(",");
                }
            }

            if (TextUtils.isEmpty(builder.toString())) {
                entity.addBodyParameter("arg1","");
            }else {
                entity.addBodyParameter("arg1", builder.toString());
            }
        }

        if (otherChannelList != null) {
            otherChannelList=removeDuplicateItem(otherChannelList);

            StringBuilder builder=new StringBuilder();
            for (int i = 0; i < otherChannelList.size(); i++) {
                builder.append(otherChannelList.get(i).getId());
                if (i!=otherChannelList.size()-1){
                    builder.append(",");
                }
            }
            if (TextUtils.isEmpty(builder.toString())) {
                entity.addBodyParameter("arg2","");
            }else {
                entity.addBodyParameter("arg2", builder.toString());
            }
        }

        isHideLoading(false);

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                NetData netData = JSON.parseObject(result, NetData.class);
                if (netData.getResult() == 200) {
                    UIUtils.showToast("修改成功");

                    setResult(200);
                    finish();
                } else {
                    UIUtils.showToast(netData.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                UIUtils.showToast("网络异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isHideLoading(true);
                view.setEnabled(true);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_select);
        x.view().inject(this);
        initData();
        initView();

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        Intent intent = getIntent();
        channel = ((Channel) intent.getSerializableExtra("channel"));
        selects=new ArrayList<>();
        other=new ArrayList<>();

        selects.addAll((List<Channel>) intent.getSerializableExtra("select"));
        other.addAll((List<Channel>) intent.getSerializableExtra("other"));


        for (int i = 0; i < selects.size(); i++) {
            Channel channelItem = selects.get(i);
            userChannelList.add(channelItem);
        }

        for (int i = 0; i < other.size(); i++) {
            Channel channelItem = other.get(i);
            otherChannelList.add(channelItem);
        }


    }


    @Override
    public void onBackPressed() {


        boolean modified = false;

        if (selects.size() != userChannelList.size()) {
            modified = true;
        } else {
            for (int i = 0; i < userChannelList.size(); i++) {
                if (!userChannelList.get(i).equals(selects.get(i))) {
                    modified = true;
                    break;
                }
            }
        }

        /*如果内容修改了，就弹窗显示*/
        if (modified) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("提示：");
            builder.setMessage("当前设置还没有保存，确定退出吗？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.create().show();
        } else {
            finish();
        }
    }


    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0 的不可以进行任何操作
                if (position != 0) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final Channel channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        //添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final Channel channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final Channel moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGridView) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

}
