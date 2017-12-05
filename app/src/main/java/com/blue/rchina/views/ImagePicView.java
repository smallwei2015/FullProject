package com.blue.rchina.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blue.rchina.R;

import java.io.Serializable;
import java.util.List;

import cn.finalteam.toolsfinal.BitmapUtils;

/**
 * Created by cj on 2017/11/13.
 */

public class ImagePicView extends LinearLayout{

    private ImgAdapter adapter;
    private List<ImgData> datas;
    private OnClickListener listener;

    public ImagePicView(Context context) {
        super(context);
    }

    public ImagePicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImagePicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAdapter(ImgAdapter adapter) {
        this.adapter = adapter;
        this.datas=adapter.getDatas();

        for (int i = 0; i < datas.size(); i++) {
            addImg(datas.get(i),i);
        }
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void addImg(ImgData data, final int position){
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pic_img_item, null);


        inflate.setTag(position);

        inflate.setOnClickListener(listener);

        if (position==datas.size()-1) {
            ImageView delete = (ImageView) inflate.findViewById(R.id.pic_delete);
            delete.setVisibility(GONE);
        }else {

            ImageView icon = (ImageView) inflate.findViewById(R.id.pic_img);
            if (data != null) {
                if (!TextUtils.isEmpty(data.getPath())) {
                    icon.setImageBitmap(BitmapUtils.compressBitmap(data.getPath()));
                }
            }
            ImageView delete = (ImageView) inflate.findViewById(R.id.pic_delete);
            delete.setVisibility(VISIBLE);
            delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    adapter.notifyDataChanged();
                }
            });
        }

        this.addView(inflate);

    }

    public class ImgAdapter{
        private List<ImgData> datas;

        public List<ImgData> getDatas() {
            return datas;
        }

        public ImgAdapter(List<ImgData> data) {
            this.datas = data;
        }

        public void notifyDataChanged(){
            ImagePicView.this.removeAllViews();

            if (datas != null) {
                for (int i = 0; i < datas.size(); i++) {
                    addImg(datas.get(i),i);
                }
            }

        }
    }

    public static class ImgData implements Serializable{
        private String path;
        private int position;

        public ImgData() {
        }

        public ImgData(String path, int position) {
            this.path = path;
            this.position = position;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}
