package com.blue.rchina.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.utils.SPUtils;

import java.io.Serializable;

/**
 * Created by cj on 2017/10/27.
 */

public class ProDialog extends DialogFragment {
    private ConfirmDialogListener mListener;
    public Bundle arguments;

    //对外开放的接口
    public  interface ConfirmDialogListener {
        void onConform(ProDialog proDialog);
    }


    public void setmListener(ConfirmDialogListener mListener) {
        this.mListener = mListener;
    }

    /**
     * @param title
     * @param message
     * @param cancelable
     * @return
     */
    public static ProDialog newInstance(String title, String message, boolean cancelable,Serializable data){
        ProDialog instance = new ProDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("message",message);
        args.putBoolean("cancelable",cancelable);
        args.putSerializable("data",data);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();
        if (arguments != null) {
            boolean cancelable = arguments.getBoolean("cancelable");
            setCancelable(cancelable);
        }else {
            setCancelable(false);
        }
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style,theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.sure_change_address, container,false);

        if (arguments != null) {
            String title = arguments.getString("title");
            String message = arguments.getString("message");


            ((TextView) inflate.findViewById(R.id.title)).setText(title);
            ((TextView) inflate.findViewById(R.id.message)).setText(message);
        }

        inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        inflate.findViewById(R.id.never_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //SPUtils.getSP().edit().putBoolean("shouldOpenCityLocation",true).apply();
                if (arguments != null) {
                    SPUtils.getSP().edit().putString("lastLocationCity",((String) arguments.getSerializable("data"))).apply();
                }
            }
        });

        inflate.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConform(ProDialog.this);
            }
        });

        return inflate;
    }
}
