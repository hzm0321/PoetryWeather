package cn.hzmeurasia.poetryweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.activity.WeatherActivity;
import cn.hzmeurasia.poetryweather.entity.Weather;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
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
    private static String parentCity;

    /**
     * 空气质量
     * @param cid
     * @param view
     */
    public static void handleAirResponse(String cid, TextView view) {
        HeWeather.getAirNow(MyApplication.getContext(), cid, new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
                handleSearchCityResponse(cid,view);

            }

            @Override
            public void onSuccess(List<AirNow> list) {
                Log.d(TAG, "onSuccess: 加载到的AQI"+list.get(0).getAir_now_city().getAqi());
                Log.d(TAG, "onSuccess: 加载AQI方法结束");
                view.setText("空气质量·"+list.get(0).getAir_now_city().getAqi());
            }
        });
    }

    public static void handleSearchCityResponse(String cid,TextView view) {
        HeWeather.getSearch(MyApplication.getContext(), cid, "cn", 1, CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);

            }

            @Override
            public void onSuccess(Search search) {
                parentCity = search.getBasic().get(0).getParent_city();
                handleAirResponse(parentCity,view);
            }
        });
    }

    public static void handleNowResponse(String cid, TextView cityName,TextView temperature,TextView text) {
        HeWeather.getWeatherNow(MyApplication.getContext(), cid, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
            }

            @Override
            public void onSuccess(List<Now> list) {

            }
        });
    }




}
