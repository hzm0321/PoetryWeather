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
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
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

    public static void handleWeatherLifeStyleResponse(Context context,String cid,TextView tv01,TextView tv02,
                                                      TextView tv03,TextView tv04,TextView tv05,TextView tv06) {
        HeWeather.getWeatherLifeStyle(MyApplication.getContext(), cid, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
            }

            @Override
            public void onSuccess(List<Lifestyle> list) {
                String comf = list.get(0).getLifestyle().get(0).getBrf();
                String drsg = list.get(0).getLifestyle().get(1).getBrf();
                String flu = list.get(0).getLifestyle().get(2).getBrf();
                String uv = list.get(0).getLifestyle().get(5).getBrf();
                String sport = list.get(0).getLifestyle().get(3).getBrf();
                String air = list.get(0).getLifestyle().get(7).getBrf();
                String comfText = list.get(0).getLifestyle().get(0).getTxt();
                String drsgText = list.get(0).getLifestyle().get(1).getTxt();
                String fluText = list.get(0).getLifestyle().get(2).getTxt();
                String uvText = list.get(0).getLifestyle().get(5).getTxt();
                String sportText = list.get(0).getLifestyle().get(3).getTxt();
                String airText = list.get(0).getLifestyle().get(7).getTxt();
                tv01.setText("舒适度:" + comf);
                tv02.setText("穿衣建议:" + drsg);
                tv03.setText("感冒指数:" + flu);
                tv04.setText("紫外线:" + uv);
                tv05.setText("运动建议:" + sport);
                tv06.setText("空气品质:"+air);
                SharedPreferences.Editor editor = context.getSharedPreferences("lifeStyle", Context.MODE_PRIVATE).edit();
                editor.putString("comf", comfText);
                editor.putString("drsg", drsgText);
                editor.putString("flu", fluText);
                editor.putString("uv", uvText);
                editor.putString("sport", sportText);
                editor.putString("air", airText);
                editor.apply();


            }
        });
    }




}
