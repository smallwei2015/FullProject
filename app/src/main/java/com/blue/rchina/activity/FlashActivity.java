package com.blue.rchina.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blue.rchina.Main2Activity;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.utils.UIUtils;

public class FlashActivity extends BaseActivity {

    public TextView jump;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        jump = ((TextView) findViewById(R.id.jump));
        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) jump.getLayoutParams();

            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,layoutParams.rightMargin,0);
        }

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what){
                    case 0:
                        openMain();
                        break;
                }
                return true;
            }
        });


        handler.sendEmptyMessageDelayed(0,0);


    }

    private void openMain() {
        Intent intent=new Intent(mActivity, Main2Activity.class);
        startActivity(intent);
        handler.removeMessages(0);
        finish();
    }
}
