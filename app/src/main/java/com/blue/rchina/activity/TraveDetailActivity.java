package com.blue.rchina.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();

        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) back.getLayoutParams();

            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,0,0);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "10086");
                intent.setData(data);
                startActivity(intent);

                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);*/
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
