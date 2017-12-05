package com.blue.rchina.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.bean.Article;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.xUtilsImageUtils;
import com.blue.rchina.views.ImageCycleView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2017/3/15.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Holder> {

    private List<DataWrap> items;
    private Context context;
    private View.OnClickListener listener;

    private ImageOptions options;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public NewsAdapter(List<DataWrap> items) {
        this.items = items;

        options = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setFailureDrawableId(R.color.xxxlight_gray)
                .setLoadingDrawableId(R.color.xxxlight_gray)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;

        switch (viewType){
            case 0:
                view=inflater.inflate(R.layout.news_item_head, parent, false);
                break;
            case 1:
                view =inflater.inflate(R.layout.news_items_plain,parent,false);
                break;
            case 2:
                view =inflater.inflate(R.layout.news_item_single,parent,false);
                break;
            case 3:
                view=inflater.inflate(R.layout.news_items_big,parent,false);
                break;

            case 4:
                view=inflater.inflate(R.layout.news_items_muti,parent,false);
                break;
            case 5:
                view=inflater.inflate(R.layout.news_items_double,parent,false);
                break;
            case 6:
                view=inflater.inflate(R.layout.news_items_triple,parent,false);
                break;
        }


        return new Holder(view,viewType);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final DataWrap dataWrap = items.get(position);

        click(holder.parent,position);
        Callback.CommonCallback<Drawable> callback = new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.firstImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.firstImage.setImageResource(R.mipmap.no_data);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        switch (dataWrap.getType()){
            case 0:

                final List<Article> heads = (List<Article>) dataWrap.getData();
                ImageCycleView circle = holder.circle;
                //circle.setCycleDelayed(4000);//设置自动轮播循环时间
                circle.setIndicationStyle(ImageCycleView.IndicationStyle.COLOR,
                        context.getResources().getColor(R.color.transparent_white),
                        context.getResources().getColor(R.color.colorPrimary), 0.6f);

                ViewGroup.LayoutParams layoutParams = circle.getLayoutParams();
                layoutParams.height= (int) (UIUtils.getWindowWidth((Activity) context)*0.5);
                circle.setLayoutParams(layoutParams);
                List<ImageCycleView.ImageInfo> list=new ArrayList();


                if (heads.size()>0) {

                    for (int i = 0; i < heads.size(); i++) {
                        Article article = heads.get(i);
                        String msg = article.getPicsrc();
                        String[] split = msg.split(";");

                    /*对于多张图，取第一张去显示*/
                        String img = split[0];
                        if (img == null || img.equals("") || img.equalsIgnoreCase("null")) {
                            list.add(new ImageCycleView.ImageInfo(R.mipmap.demo, article.getTitle(), article));
                            continue;
                        }
                        list.add(new ImageCycleView.ImageInfo(img, article.getTitle(), article));

                    }

                    circle.setListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    //使用网络加载图片



                    circle.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {

                        @Override

                        public void onClick(View imageView, ImageCycleView.ImageInfo imageInfo) {
                            /*Intent inent=new Intent(context, NewsKindDetailActivty.class);
                            inent.putExtra("data",(Article)imageInfo.value);
                            context.startActivity(inent);*/
                        }

                    });


                    circle.loadData(list, new ImageCycleView.LoadImageCallBack() {

                        @Override

                        public ImageView loadAndDisplay(final ImageCycleView.ImageInfo imageInfo) {
                            ImageView imageView = new ImageView(context);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            ImageOptions imageOptions = new ImageOptions.Builder()
                                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                                    .setFailureDrawableId(R.color.xlight_gray)
                                    .setLoadingDrawableId(R.color.xlight_gray)
                                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                                    .build();

                            x.image().bind(imageView, ((String) imageInfo.image),imageOptions);

                            return imageView;
                        }

                    });
                }
                break;
            case 1:
                Article article0 = (Article) dataWrap.getData();

                //click(holder.firstImage,position);
                holder.singleTitle.setText(article0.getTitle());
                holder.singleComment.setText(article0.getClickCount()+" 浏览");
                holder.singleTime.setText(article0.getDatetime());
                if (!TextUtils.isEmpty(article0.getLy())) {
                    holder.singleFrom.setText(article0.getLy());
                }
                break;
            case 2:
                Article article = (Article) dataWrap.getData();

                //click(holder.firstImage,position);
                holder.singleTitle.setText(article.getTitle());

                    /*String clickCountStr="";
                    if (clickCount>=10000){
                        if (clickCount%10000/1000>=1) {
                            clickCountStr=String.format("%.1f",clickCount/10000.0)+"万浏览";
                        }else {
                            clickCountStr=clickCount/10000+"万浏览";
                        }
                    }else {
                        clickCountStr=clickCount+"浏览";
                    }*/

                holder.singleComment.setText(article.getClickCount()+" 浏览");


                if (!TextUtils.isEmpty(article.getLy())) {
                    holder.singleFrom.setText(article.getLy());
                }
                if (false){
                    holder.singleKind.setVisibility(View.VISIBLE);
                }else {
                    holder.singleKind.setVisibility(View.GONE);
                }

                String articlePicsrc = article.getPicsrc().split(";")[0];

                if (TextUtils.isEmpty(articlePicsrc)){
                    holder.firstImage.setImageResource(R.mipmap.no_data);
                }else {
                    //xUtilsImageUtils.display(holder.firstImage, articlePicsrc, callback);

                    if (articlePicsrc.contains("dwtt/static/images/logo.png")){
                        options = new ImageOptions.Builder()
                                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                                .setFailureDrawableId(R.mipmap.no_data)
                                .setLoadingDrawableId(R.color.xxxlight_gray)
                                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                                .setUseMemCache(false)
                                .build();
                    }

                    x.image().bind(holder.firstImage,articlePicsrc,options,callback);
                }
                holder.singleTime.setText(article.getDatetime());

                break;
            case 3:
                Article article3 = (Article) dataWrap.getData();
                holder.singleTitle.setText(article3.getTitle());

                xUtilsImageUtils.display(holder.firstImage,article3.getPicsrc(),callback);

                break;
            case 5:
                /*click(holder.firstImage,position);
                click(holder.secondImage,position);*/
                Article article1 = (Article) dataWrap.getData();

                holder.singleTitle.setText(article1.getTitle());
                holder.singleComment.setText(article1.getClickCount()+" 浏览");
                if (!TextUtils.isEmpty(article1.getLy())) {
                    holder.singleFrom.setText(article1.getLy());
                }
                if (false){
                    holder.singleKind.setVisibility(View.VISIBLE);
                }else {
                    holder.singleKind.setVisibility(View.GONE);
                }
                String picsrc2 = article1.getPicsrc();
                String[] split2 = picsrc2.split(";");

                if (split2.length>=2){
                    xUtilsImageUtils.display(holder.firstImage, split2[0],
                            callback);
                    xUtilsImageUtils.display(holder.secondImage, split2[1],
                            callback);
                }

                holder.singleTime.setText(article1.getDatetime());
                break;
            case 6:
                Article article2 = (Article) dataWrap.getData();

                /*click(holder.firstImage,position);
                click(holder.secondImage,position);
                click(holder.thirdImage,position);*/

                holder.singleTitle.setText(article2.getTitle());
                holder.singleComment.setText(article2.getClickCount()+" 浏览");
                if (!TextUtils.isEmpty(article2.getLy())) {
                    holder.singleFrom.setText(article2.getLy());
                }
                if (false){
                    holder.singleKind.setVisibility(View.VISIBLE);
                }else {
                    holder.singleKind.setVisibility(View.GONE);
                }

                String picsrc3 = article2.getPicsrc();
                String[] split3 = picsrc3.split(";");

                if (split3.length>=2){
                    xUtilsImageUtils.display(holder.firstImage, split3[0],
                            callback);
                    xUtilsImageUtils.display(holder.secondImage, split3[1],
                            callback);
                    xUtilsImageUtils.display(holder.thirdImage, split3[2],
                            callback);
                }
                holder.singleTime.setText(article2.getDatetime());
                break;

        }
    }

    private void click(View view, int position) {
        view.setTag(position);
        view.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    class Holder extends RecyclerView.ViewHolder{

        View parent;
        ImageCycleView circle;

        TextView singleKind,singleTime,singleTitle,singleFrom,singleComment;
        ImageView firstImage,secondImage,thirdImage;


        public Holder(View itemView,int type) {
            super(itemView);

            parent=itemView;

            switch (type){
                case 0:
                    circle= (ImageCycleView) itemView.findViewById(R.id.image_cycle);
                    break;
                case 1:
                    singleTitle = ((TextView) itemView.findViewById(R.id.news_single_title));
                    singleTime = ((TextView) itemView.findViewById(R.id.news_single_time));
                    singleKind = ((TextView) itemView.findViewById(R.id.news_single_kind));
                    singleFrom= (TextView) itemView.findViewById(R.id.news_single_from);
                    singleComment= (TextView) itemView.findViewById(R.id.news_single_comment);
                    break;
                case 2:
                    firstImage = ((ImageView) itemView.findViewById(R.id.news_single_icon));
                    singleTitle = ((TextView) itemView.findViewById(R.id.news_single_title));
                    singleTime = ((TextView) itemView.findViewById(R.id.news_single_time));
                    singleKind = ((TextView) itemView.findViewById(R.id.news_single_kind));
                    singleFrom= (TextView) itemView.findViewById(R.id.news_single_from);
                    singleComment= (TextView) itemView.findViewById(R.id.news_single_comment);
                    break;
                case 3:
                    firstImage= (ImageView) itemView.findViewById(R.id.news_big_image);
                    singleTitle= (TextView) itemView.findViewById(R.id.news_big_title);

                    break;
                case 4:

                    break;

                case 5:
                    singleTitle= (TextView) itemView.findViewById(R.id.news_double_title);
                    firstImage= (ImageView) itemView.findViewById(R.id.news_double_icon1);
                    secondImage= (ImageView) itemView.findViewById(R.id.news_double_icon2);

                    singleTime = ((TextView) itemView.findViewById(R.id.news_single_time));
                    singleKind = ((TextView) itemView.findViewById(R.id.news_single_kind));
                    singleFrom= (TextView) itemView.findViewById(R.id.news_single_from);
                    singleComment= (TextView) itemView.findViewById(R.id.news_single_comment);
                    break;
                case 6:
                    singleTitle= (TextView) itemView.findViewById(R.id.news_triple_title);
                    firstImage= (ImageView) itemView.findViewById(R.id.news_triple_icon1);
                    secondImage= (ImageView) itemView.findViewById(R.id.news_triple_icon2);
                    thirdImage= (ImageView) itemView.findViewById(R.id.news_triple_icon3);

                    singleTime = ((TextView) itemView.findViewById(R.id.news_single_time));
                    singleKind = ((TextView) itemView.findViewById(R.id.news_single_kind));
                    singleFrom= (TextView) itemView.findViewById(R.id.news_single_from);
                    singleComment= (TextView) itemView.findViewById(R.id.news_single_comment);
                    break;
            }
        }
    }
}
