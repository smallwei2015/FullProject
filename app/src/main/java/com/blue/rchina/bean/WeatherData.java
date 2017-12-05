package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by cj on 2017/3/19.
 */

@Table(name = "rc_weather")
public class WeatherData extends BaseData {

    @Column(name = "weather")
    private String weather;
    @Column(name = "temperature")
    private String temperature;
    @Column(name = "pm")
    private String pm;
    @Column(name = "city",isId = true)
    private String city;
    @Column(name = "date")
    private String date;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public BaseData parseObject(String json) {
        return null;
    }

    @Override
    public List<BaseData> parseList(String jsonList) {
        return null;
    }
}
