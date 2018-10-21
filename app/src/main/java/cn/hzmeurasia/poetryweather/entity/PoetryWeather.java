package cn.hzmeurasia.poetryweather.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 类名: PoetryWeather<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/20 14:25
 */
public class PoetryWeather {

    @SerializedName("poetry")
    public List<Poetry> poetryList;
}
