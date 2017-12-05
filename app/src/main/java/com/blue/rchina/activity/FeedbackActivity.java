package com.blue.rchina.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.manager.UserManager;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class FeedbackActivity extends BaseActivity {

    private EditText content;

    @Override
    public void initView() {
        super.initView();
        initTop(R.mipmap.left_white,"意见反馈",-1);

        content = (EditText) findViewById(R.id.feedback_content);

        findViewById(R.id.feedback_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentStr = content.getText().toString();
                if (TextUtils.isEmpty(contentStr)) {
                    UIUtils.showToast("请填写相关内容");
                }else {
                    if (UserManager.isLogin()) {
                        feedback(contentStr);
                    }else {
                        Intent intent=new Intent(FeedbackActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void feedback(String content) {
        RequestParams entity = new RequestParams(UrlUtils.N_feedBack);
        entity.addBodyParameter("appuserId", UserManager.getUser().getAppuserId()+"");
        entity.addBodyParameter("content",content);

        x.http().post(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject object = JSON.parseObject(result);
                Integer code = object.getInteger("result");

                if (code==200){
                    UIUtils.showToast("您的意见我们意见收到，感谢您的建议");
                    finish();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
        initData();
    }
}
