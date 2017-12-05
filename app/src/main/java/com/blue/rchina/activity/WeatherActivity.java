package com.blue.rchina.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blue.rchina.R;
import com.blue.rchina.base.BaseActivity;
import com.blue.rchina.bean.AreaInfo;
import com.blue.rchina.bean.Weather;
import com.blue.rchina.utils.UIUtils;
import com.blue.rchina.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends BaseActivity {

    @ViewInject(R.id.weather_back)
    View back;


    @ViewInject(R.id.weather_image)
    ImageView image;

    @ViewInject(R.id.weather_pm)
    TextView pm;

    @ViewInject(R.id.weather_wea)
    TextView wea;

    @ViewInject(R.id.weather_city)
    TextView cit;
    @ViewInject(R.id.weather_temp)
    TextView temp;

    @ViewInject(R.id.weather_recycler)
    RecyclerView recyclerView;

    private List<String> titles;
    private List<View> images;
    private AreaInfo info;
    private List<Weather.ResultsBean.WeatherDataBean> weathers;
    private RecyclerView.Adapter adapter;

    @Override
    public void initView() {
        super.initView();

        isHideLoading(false);

        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity);
        if (statusBarHeight>0){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) back.getLayoutParams();

            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin+statusBarHeight,0,0);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titles=new ArrayList<>();
        images=new ArrayList<>();

        weathers=new ArrayList<>();

        titles.add("天气");
        titles.add("扫一扫");
        titles.add("头条");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_item, parent, false);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                Weather.ResultsBean.WeatherDataBean weatherData = weathers.get(position);
                if (position==0){
                    holder.icon.setImageResource(R.mipmap.arr_right_gray);
                }else {
                    //holder.icon.setVisibility(View.INVISIBLE);
                    x.image().bind(holder.icon,weatherData.getDayPictureUrl());
                }


                holder.day.setText(weatherData.getDate());

                holder.temp.setText(weatherData.getTemperature());
                holder.wea.setText(weatherData.getWeather());
            }

            @Override
            public int getItemCount() {
                if (weathers != null) {
                    return weathers.size();
                }
                return 0;
            }


        };
        recyclerView.setAdapter(adapter);

    }

    private String nuber2Day(int number) {
        String day="";
        switch (number){
            case 1:
                day= "星期一";
                break;
            case 2:
                day= "星期二";
                break;
            case 3:
                day= "星期三";
                break;
            case 4:
                day= "星期四";
                break;
            case 5:
                day= "星期五";
                break;
            case 6:
                day= "星期六";
                break;
            case 7:
                day= "星期日";
                break;
        }

        return day;
    }

    @Override
    public void initData() {
        super.initData();

        info = ((AreaInfo) getIntent().getSerializableExtra("city"));
        if (info!=null&&!TextUtils.isEmpty(info.getAreaName())){
            getWeather(info.getAreaName());
        }else {
            getWeather("北京市");
        }


    }

    private void getWeather(final String city) {

        isHideLoading(false);
        try {
            RequestParams entity = new RequestParams(String.format(UrlUtils.BWEATHER, URLEncoder.encode(city, "UTF-8")));

            x.http().get(entity, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {

                    Weather weather = JSON.parseObject(result, Weather.class);

                    if (weather != null) {

                        List<Weather.ResultsBean> results = weather.getResults();

                        if (results!=null&&results.size()>0) {
                            String pm25 = results.get(0).getPm25();


                            pm.setText(!TextUtils.isEmpty(pm25) ? "PM2.5\t" + pm25 : "暂无pm数据");
                            cit.setText(city);

                            List<Weather.ResultsBean.WeatherDataBean> weather_data = results.get(0).getWeather_data();

                            for (int i = 0; i < weather_data.size(); i++) {
                                weathers.add(weather_data.get(i));
                            }
                            adapter.notifyDataSetChanged();

                            Weather.ResultsBean.WeatherDataBean weatherDataBean = weather_data.get(0);

                            String temperature = weatherDataBean.getTemperature();

                            String weather1 = weatherDataBean.getWeather();
                            wea.setText(weather1);
                            temp.setText(!TextUtils.isEmpty(temperature) ? temperature : "暂无温度数据");
                            if (TextUtils.isEmpty(weather1)) {

                            } else {

                                if (weather1.contains("晴")) {
                                    image.setImageResource(R.drawable.sunshine);
                                } else if (weather1.contains("雪")) {
                                    image.setImageResource(R.drawable.sunshine);
                                } else if (weather1.contains("雨")) {
                                    image.setImageResource(R.drawable.sunshine);
                                } else if (weather1.contains("云")) {
                                    image.setImageResource(R.drawable.sunshine);
                                } else {
                                    image.setImageResource(R.drawable.sunshine);
                                }
                            }
                        }
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (info != null) {
                        getWeather(info.getProvinceCapital());
                    }

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    isHideLoading(true);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        x.view().inject(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        initView();
        initData();
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView day,wea,temp;


        public Holder(View itemView) {
            super(itemView);

            icon= (ImageView) itemView.findViewById(R.id.wea_icon);
            day= (TextView) itemView.findViewById(R.id.wea_day);
            wea= (TextView) itemView.findViewById(R.id.wea_wea);
            temp= (TextView) itemView.findViewById(R.id.wea_temp);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
