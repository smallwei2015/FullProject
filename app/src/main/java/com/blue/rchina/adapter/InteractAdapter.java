package com.blue.rchina.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blue.rchina.R;
import com.blue.rchina.activity.NewsKindImageActivity;
import com.blue.rchina.bean.Alliance;
import com.blue.rchina.bean.Comment;
import com.blue.rchina.bean.DataWrap;
import com.blue.rchina.bean.Report;
import com.blue.rchina.bean.Size;
import com.blue.rchina.bean.User;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.xUtilsImageUtils;
import com.blue.rchina.views.NoScrollGridView;

import java.util.List;

/**
 * Created by cj on 2017/3/14.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.Holder> {


    private Context context;
    private View.OnClickListener listener;
    private List<DataWrap> items;
    private int windowWidth;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public InteractAdapter(List<DataWrap> items) {
        this.items = items;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = null;
        switch (viewType) {
            case 0:
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.interact_item, parent, false);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.interact_item_single, parent, false);
                break;
            case 5:
                view = LayoutInflater.from(context).inflate(R.layout.interact_item_double, parent, false);
                break;
            case 6:
                view = LayoutInflater.from(context).inflate(R.layout.interact_item_plain, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.interact_comment, parent, false);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.interact_agree, parent, false);
                break;
            case 7:
                view=LayoutInflater.from(context).inflate(R.layout.interact_top,parent,false);
                break;
            default:
                view = new View(context);
                break;

        }
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        windowWidth = UIUtils.getWindowWidth((Activity) context);

        DataWrap dataWrap = items.get(position);

        holder.parent.setTag(position);
        holder.parent.setOnClickListener(listener);

        int type = dataWrap.getType();
        switch (type) {
            case 0:
                /*多图*/
            case 4:
                /*单图*/
            case 5:
                /*双图*/
            case 6:
                /*无图*/
                if (holder.delete != null) {
                    holder.delete.setVisibility(View.GONE);
                }

                holder.interact_bottom_kind2.setVisibility(View.GONE);

                final Report data = (Report) dataWrap.getData();

                holder.content.setText(data.getContent());
                /*对于多行文本，在列表页面限制最多五行，在详细列表中全部显示*/
                //holder.content.setMaxLines(5);
                holder.comment.setText("评论 (" + data.getCommentCount() + ")");


                if (data.getDistance() < 1000) {
                    holder.distance.setText("距离我:" + data.getDistance() + "米");
                } else {
                    holder.distance.setText("距离我:" + data.getDistance() / 1000 + "千米");
                }

                if (!TextUtils.isEmpty(data.getLocation())) {
                    holder.location.setText(data.getLocation());
                }else {
                    holder.location.setText("未知位置");
                }


                holder.agree_parent.setTag(position);
                holder.agree_parent.setOnClickListener(listener);
                holder.comment_parent.setTag(position);
                holder.report_parent.setOnClickListener(listener);
                holder.report_parent.setTag(position);
                holder.comment_parent.setOnClickListener(listener);

                /*1 已点赞 2未点赞*/
                if (data.getPraiseState() == 1) {
                    holder.agree_icon.setSelected(true);
                } else {
                    holder.agree_icon.setSelected(false);
                }
                holder.agree.setText("赞(" + data.getPraiseCount() + ")");


                if (TextUtils.isEmpty(data.getHeadIcon())){
                    holder.icon.setImageResource(R.color.bg_color);
                }else {
                    xUtilsImageUtils.displayRadio(holder.icon, data.getHeadIcon());
                }
                holder.icon.setTag(position);
                holder.icon.setOnClickListener(listener);

                holder.name.setText(data.getNickName());
                holder.date.setText(data.getDatetime());
                final List<String> manyPic = data.getManyPic();

                if (type == 4) {
                    /*单图*/
                    ImageView img = holder.singleImg;

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, NewsKindImageActivity.class);
                            intent.putExtra("data", data);
                            //intent.putExtra("position", 0);
                            context.startActivity(intent);
                        }
                    });

                    int screenW = UIUtils.getWindowWidth((Activity) context);
                    Size size1 = Size.defaultSize();


                    if (data.getPicsize() != null && data.getPicsize().size() > 0) {
                        size1 = data.getPicsize().get(0);
                    }

                    ViewGroup.LayoutParams layoutParams = img.getLayoutParams();

                    float percent = 0.5f;
                    if (size1.getWidth() > size1.getHeight()) {
                        layoutParams.width = (int) (screenW * percent);
                        layoutParams.height = (int) (screenW * percent * size1.getHeight() / size1.getWidth());
                    } else {
                        layoutParams.height = (int) (screenW * percent);
                        layoutParams.width = (int) (screenW * percent * size1.getWidth() / size1.getHeight());
                    }

                    xUtilsImageUtils.display(img, manyPic.get(0));

                } else if (type == 5) {
                    /*双图*/
                    int screenW = UIUtils.getWindowWidth((Activity) context);

                    ViewGroup.LayoutParams params1 = holder.doubleImg1.getLayoutParams();
                    params1.height = screenW / 3;
                    holder.doubleImg1.setLayoutParams(params1);
                    xUtilsImageUtils.display(holder.doubleImg1, manyPic.get(0));

                    holder.doubleImg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, NewsKindImageActivity.class);
                            intent.putExtra("data", data);
                            intent.putExtra("position", 0);
                            context.startActivity(intent);
                        }
                    });

                    ViewGroup.LayoutParams params2 = holder.doubleImg2.getLayoutParams();
                    params2.height = screenW / 3 ;
                    holder.doubleImg2.setLayoutParams(params2);
                    xUtilsImageUtils.display(holder.doubleImg2, manyPic.get(1));

                    holder.doubleImg2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, NewsKindImageActivity.class);
                            intent.putExtra("data", data);
                            intent.putExtra("position", 1);
                            context.startActivity(intent);
                        }
                    });

                } else if (type == 6) {

                } else {

                    if (manyPic.size() > 0) {

                        holder.grid.setNumColumns(manyPic.size() < 3 ? manyPic.size() : 3);

                        holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, NewsKindImageActivity.class);
                                intent.putExtra("data", data);
                                intent.putExtra("position", position);
                                context.startActivity(intent);
                            }
                        });
                        holder.grid.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                if (manyPic != null) {
                                    return manyPic.size();
                                }
                                return 0;
                            }

                            @Override
                            public Object getItem(int position) {
                                return manyPic.get(position);
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {


                                if (convertView == null) {
                                    convertView = LayoutInflater.from(context).inflate(R.layout.interact_grid_item, parent, false);
                                }
                                convertView = LayoutInflater.from(context).inflate(R.layout.interact_grid_item, parent, false);
                                int size = manyPic.size();
                                ImageView icon = (ImageView) convertView.findViewById(R.id.image);
                                int screenW = UIUtils.getWindowWidth((Activity) context);


                                Size size1 = Size.defaultSize();

                                if (data.getPicsize() != null) {
                                    size1 = data.getPicsize().get(0);
                                }
                                ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();

                                if (size == 1) {
                                    float percent = 0.6f;
                                    if (size1.getWidth() > size1.getHeight()) {
                                        layoutParams.width = (int) (screenW * percent);
                                        layoutParams.height = (int) (screenW * percent * size1.getHeight() / size1.getWidth());
                                    } else {
                                        layoutParams.height = (int) (screenW * percent);
                                        layoutParams.width = (int) (screenW * percent * size1.getWidth() / size1.getHeight());
                                    }
                                } else {
                                    layoutParams.height = screenW / (size < 3 ? size+1 : 4) - 20;

                                }
                                icon.setLayoutParams(layoutParams);
                                xUtilsImageUtils.display(icon, manyPic.get(position));
                                return convertView;
                            }
                        });
                    }
                }

                break;
            case 1:
                /*评论item*/
                Comment data1 = (Comment) dataWrap.getData();

                xUtilsImageUtils.display(holder.icon, data1.getHeadIcon(), true);
                holder.name.setText(data1.getNickName());
                holder.date.setText(data1.getDatetime());
                holder.content.setText(data1.getContent());
                holder.reply.setText("已收到" + 0 + "条回复");

                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showToast("暂时无法查看评论详情");
                    }
                });

                break;
            case 2:
                /*互动详情页*/
                final Report data2 = (Report) dataWrap.getData();
                /*判断是否显示删除按钮*/
                if (data2.getDisplayType()==1) {
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.delete.setTag(position);
                    holder.delete.setOnClickListener(listener);
                }else {
                    holder.delete.setVisibility(View.GONE);
                }

                holder.interact_bottom_kind1.setVisibility(View.GONE);



                holder.interact_bottom_kind2.setTag(position);
                holder.interact_bottom_kind2.setOnClickListener(listener);


                holder.content.setText(data2.getContent());
                holder.comment.setText("评论 (" + data2.getCommentCount() + ")");

                if (data2.getDistance() < 1000) {
                    holder.distance.setText("距离我:" + data2.getDistance() + "米");
                } else {
                    holder.distance.setText("距离我:" + data2.getDistance() / 1000 + "千米");
                }
                //holder.distance.setText("距离" + data2.getDistance() + "米");
                if (!TextUtils.isEmpty(data2.getLocation())) {
                    holder.location.setText(data2.getLocation());
                }else {
                    holder.location.setText("未知位置");
                }

                holder.agree_parent.setTag(position);
                holder.agree_parent.setOnClickListener(listener);
                holder.comment_parent.setTag(position);
                holder.report_parent.setOnClickListener(listener);
                holder.report_parent.setTag(position);
                holder.comment_parent.setOnClickListener(listener);

                if (TextUtils.isEmpty(data2.getHeadIcon())) {
                    holder.icon.setImageResource(R.color.bg_color);
                }else {
                    xUtilsImageUtils.displayRadio(holder.icon, data2.getHeadIcon());
                }
                holder.name.setText(data2.getNickName());
                holder.date.setText(data2.getDatetime());


                holder.interact_agree_count.setText(data2.getPraiseCount() + "人点赞");


                View demo = holder.interact_icon_container.getChildAt(0);
                demo.setVisibility(View.GONE);
                ViewGroup.LayoutParams demoLayoutParams = demo.getLayoutParams();

                holder.interact_icon_container.removeAllViews();
                holder.interact_icon_container.addView(demo);

                for (int i = 0; i < data2.getPraiseCount() && i < 6; i++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setLayoutParams(demoLayoutParams);
                    String headIcon = data2.getPraiseList().get(i).getHeadIcon();

                    if (TextUtils.isEmpty(headIcon)) {
                        xUtilsImageUtils.display(imageView, "", true);
                    } else {
                        xUtilsImageUtils.display(imageView, headIcon, true);
                    }

                    holder.interact_icon_container.addView(imageView);
                }

                final List<String> manyPic2 = data2.getManyPic();

                if (manyPic2.size() > 0) {

                    holder.grid.setNumColumns(manyPic2.size() < 3 ? manyPic2.size() : 3);

                    holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(context, NewsKindImageActivity.class);
                            intent.putExtra("data", data2);
                            intent.putExtra("position", position);
                            context.startActivity(intent);
                        }
                    });
                    holder.grid.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            if (manyPic2 != null) {
                                return manyPic2.size();
                            }
                            return 0;
                        }

                        @Override
                        public Object getItem(int position) {
                            return manyPic2.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            /*if (convertView == null) {
                                convertView = LayoutInflater.from(context).inflate(R.layout.interact_grid_item, parent, false);
                            }*/
                            convertView = LayoutInflater.from(context).inflate(R.layout.interact_grid_item, parent, false);

                            int size = manyPic2.size();
                            ImageView icon = (ImageView) convertView.findViewById(R.id.image);
                            int screenW = UIUtils.getWindowWidth((Activity) context);
                            Size size1 = Size.defaultSize();
                            if (data2.getPicsize() != null) {
                                size1 = data2.getPicsize().get(0);
                            }
                            ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();

                            if (size == 1) {
                                float percent = 0.6f;
                                if (size1.getWidth() > size1.getHeight()) {
                                    layoutParams.width = (int) (screenW * percent);
                                    layoutParams.height = (int) (screenW * percent * size1.getHeight() / size1.getWidth());
                                } else {
                                    layoutParams.height = (int) (screenW * percent);
                                    layoutParams.width = (int) (screenW * percent * size1.getWidth() / size1.getHeight());
                                }
                            } else {
                                layoutParams.height = screenW / (size < 3 ? size+1: 4);

                            }
                            icon.setLayoutParams(layoutParams);
                            xUtilsImageUtils.display(icon, manyPic2.get(position));
                            return convertView;
                        }
                    });
                }
                break;

            case 3:
                /*点赞条目*/
                User data3 = (User) dataWrap.getData();
                holder.name.setText(data3.getNickName());
                xUtilsImageUtils.display(holder.icon, data3.getHeadIcon(), true);
                break;
            case 7:
                /*置顶条目*/
                Alliance data4 = (Alliance) dataWrap.getData();
                if (data4 != null) {
                    holder.content.setText(data4.getDesc());
                    holder.name.setText(data4.getTitle());
                }

                break;
        }

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

    class Holder extends RecyclerView.ViewHolder {
        ImageView icon, agree_icon, singleImg, doubleImg1, doubleImg2;
        TextView name, date, content, location, distance, agree, comment, report, reply;
        TextView interact_agree_count;
        NoScrollGridView grid;

        LinearLayout agree_parent, comment_parent, report_parent;
        LinearLayout interact_bottom_kind1, interact_bottom_kind2, interact_icon_container;
        View parent,delete;

        public Holder(View itemView, int type) {
            super(itemView);

            parent = itemView;

            switch (type) {
                case 0:
                case 2:
                    delete=itemView.findViewById(R.id.interact_delete);
                case 4:
                case 5:
                case 6:
                    icon = (ImageView) itemView.findViewById(R.id.interact_icon);
                    agree_icon = (ImageView) itemView.findViewById(R.id.interact_agree_icon);

                    name = (TextView) itemView.findViewById(R.id.interact_nickname);
                    date = (TextView) itemView.findViewById(R.id.interact_date);
                    content = (TextView) itemView.findViewById(R.id.interact_content);
                    location = (TextView) itemView.findViewById(R.id.interact_location);
                    distance = (TextView) itemView.findViewById(R.id.interact_distance);
                    agree = (TextView) itemView.findViewById(R.id.interact_agree);
                    comment = (TextView) itemView.findViewById(R.id.interact_comment);
                    report = (TextView) itemView.findViewById(R.id.interact_report);

                    if (type == 4) {
                        singleImg = (ImageView) itemView.findViewById(R.id.interact_single_img);
                    } else if (type == 5) {
                        doubleImg1 = (ImageView) itemView.findViewById(R.id.interact_double_img1);
                        doubleImg2 = (ImageView) itemView.findViewById(R.id.interact_double_img2);

                    } else if (type == 6) {

                    } else {
                        grid = (NoScrollGridView) itemView.findViewById(R.id.interact_grid);
                    }

                    agree_parent = (LinearLayout) itemView.findViewById(R.id.interact_agree_parent);
                    comment_parent = (LinearLayout) itemView.findViewById(R.id.interact_comment_parent);
                    report_parent = (LinearLayout) itemView.findViewById(R.id.interact_report_parent);

                    interact_bottom_kind1 = (LinearLayout) itemView.findViewById(R.id.interact_bottom_kind1);
                    interact_bottom_kind2 = (LinearLayout) itemView.findViewById(R.id.interact_bottom_kind2);
                    interact_icon_container = (LinearLayout) itemView.findViewById(R.id.interact_icon_container);


                    interact_agree_count = (TextView) itemView.findViewById(R.id.interact_agree_count);

                    break;
                case 1:

                    icon = (ImageView) itemView.findViewById(R.id.comment_icon);
                    name = (TextView) itemView.findViewById(R.id.comment_name);
                    date = (TextView) itemView.findViewById(R.id.comment_date);
                    content = (TextView) itemView.findViewById(R.id.comment_content);
                    reply = (TextView) itemView.findViewById(R.id.comment_reply);

                    break;
                case 3:

                    icon = (ImageView) itemView.findViewById(R.id.interact_agree_icon);
                    name = (TextView) itemView.findViewById(R.id.interact_agree_name);

                    break;
                case 7:
                    content= (TextView) itemView.findViewById(R.id.interact_top_content);
                    name= (TextView) itemView.findViewById(R.id.interact_top_name);

                    break;

            }

        }

    }
}
