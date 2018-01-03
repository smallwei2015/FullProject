package com.blue.rchina.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;

import com.blue.rchina.R;
import com.blue.rchina.utils.AnimationUtils;

/**
 * Created by cj on 2017/3/6.
 */

public abstract class BaseFragment extends Fragment implements BaseUIContainer {
    public Activity mActivity;
    public View nodata;
    public ContentLoadingProgressBar loading;
    public View serverDie;

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {
        nodata = view.findViewById(R.id.no_data);
        serverDie = view.findViewById(R.id.server_die);
        loading = (ContentLoadingProgressBar) view.findViewById(R.id.loading);
        if (nodata != null) {
            nodata.setVisibility(View.GONE);
        }
        if (serverDie != null) {
            serverDie.setVisibility(View.GONE);
        }
        if (loading != null) {
            loading.setVisibility(View.GONE);
        }
    }

    private String tag;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BaseApplication.push(this);
        type=TYPE_FRAGMENT;
        tag=getClass().getName();
        mActivity= (Activity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //BaseApplication.pop(this);
    }

    @Override
    public void UIfinish() {
    }

    @Override
    public String getUITag() {
        return tag;
    }

    @Override
    public int getType() {
        return type;
    }

    public void isNodata(boolean no){
        if (nodata != null) {
            if (no){
                if (nodata.getVisibility()!= View.VISIBLE) {
                    nodata.setVisibility(View.VISIBLE);
                    nodata.setAnimation(AnimationUtils.scaleToSelfSize());
                }
            }else {
                nodata.setVisibility(View.GONE);
            }
        }
    }

    public void isHideLoading(boolean show){
        if (loading != null) {
            if (show){
                if (loading.getVisibility()==View.VISIBLE) {
                    loading.setVisibility(View.GONE);
                }
            }else {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }
}
