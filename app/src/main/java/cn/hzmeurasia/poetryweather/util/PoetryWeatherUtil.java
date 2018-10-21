package cn.hzmeurasia.poetryweather.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.hzmeurasia.poetryweather.entity.PoetryWeather;

/**
 * 类名: PoetryWeatherUtil<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/20 14:39
 */
public class PoetryWeatherUtil {

    public static PoetryWeather handlePoetryWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("PoetryWeather");
            String poetryContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(poetryContent, PoetryWeather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
