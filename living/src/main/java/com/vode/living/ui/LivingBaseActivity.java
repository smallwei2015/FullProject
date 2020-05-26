package com.vode.living.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vode.living.R;

/**
 * Created by cj on 2018/9/10.
 */

public class LivingBaseActivity extends AppCompatActivity {

    void initView(){}
    void initData(){}

    void initTop(int leftrsc,String content,int rightsrc){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundResource(R.color.colorPrimary);
        if (toolbar != null) {

            ImageView right = (ImageView) findViewById(R.id.right_icon);
            setSupportActionBar(toolbar);
            if (rightsrc>0){
                right.setVisibility(View.VISIBLE);
                right.setImageResource(rightsrc);
            }else {
                right.setVisibility(View.GONE);
            }

            ImageView left = (ImageView) findViewById(R.id.left);
            left.setImageResource(leftrsc);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ((TextView) findViewById(R.id.center_text)).setText(content);

        }



    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }
}
