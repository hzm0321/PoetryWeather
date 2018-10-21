package cn.hzmeurasia.poetryweather.util;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.entity.Weather;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static interfaces.heweather.com.interfacesmodule.bean.Lang.CHINESE_SIMPLIFIED;

/**
 * 类名: HeWeatherUtil<br>
 * 功能:(获取和风天气数据)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/3 0:10
 */
public class HeWeatherUtil {
    private static final String TAG = "HeWeatherUtil";

    public static String searchCity(String cityName) {
        final String[] cid = {null};
        //注册和风天气
        HeConfig.init("HE1808181021011344","c6a58c3230694b64b78facdebd7720fb");
        HeConfig.switchToFreeServerNode();
        HeWeather.getSearch(MyApplication.getContext(), cityName, "cn", 1, CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
                Toast.makeText(MyApplication.getContext(),"搜索城市失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Search search) {
                cid[0] = search.getBasic().get(0).getCid();
                Log.d(TAG, "onSuccess: "+cid[0]);
            }
        });
        Log.d(TAG, "searchCity: "+cid[0]);
        return cid[0];
    }

    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
