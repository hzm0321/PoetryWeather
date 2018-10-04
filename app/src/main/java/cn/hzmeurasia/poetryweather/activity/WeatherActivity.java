package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.CalendarEvent;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * 类名: WeatherActivity<br>
 * 功能:(天气布局的活动)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/22 16:42
 */
public class WeatherActivity extends AppCompatActivity {



    private static final String TAG = "WeatherActivity";

    private TextView tvCityName;
    private TextView tvUpdateTime;
    private TextView tvTemperature;
    private TextView tvWeather;
    private ImageView imgWeatherIcon;
    private LinearLayout forecastLayout;
    private TextView tvSuit;
    private TextView tvAvoid;
//
//    private RelativeLayout relativeLayout;
    private Intent intent = null;
    private String cityCode = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        //注册EventBus
        EventBus.getDefault().register(this);
        //控件初始化
        init();
//        //获取手机屏幕高度
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int height = dm.heightPixels;
//        //获取手机状态栏高度
//        int statusBarHeight1 = -1;
//        //获取status_bar_height资源的ID
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            //根据资源ID获取响应的尺寸值
//            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
//        }
//        //设置RelativeLayout高度为屏幕高度-状态栏高度
//        relativeLayout = findViewById(R.id.rl);
//        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
//        params.height = height-statusBarHeight1;
//        relativeLayout.setLayoutParams(params);

        //注册和风天气
        HeConfig.init("HE1808181021011344","c6a58c3230694b64b78facdebd7720fb");
        HeConfig.switchToFreeServerNode();
        //调用天气数据
        intent = getIntent();
        cityCode = intent.getStringExtra("cityCode");
        heWeather();



    }

    private void init() {
        tvCityName = findViewById(R.id.tv_cityName);
        tvUpdateTime = findViewById(R.id.tv_updateTime);
        tvTemperature = findViewById(R.id.tv_weather_temperature);
        tvWeather = findViewById(R.id.tv_weather);
        imgWeatherIcon = findViewById(R.id.iv_weather_icon);
        forecastLayout = findViewById(R.id.ll_forecast);
        tvSuit = findViewById(R.id.tv_weather_suitable);
        tvAvoid = findViewById(R.id.tv_weather_avoid);
    }

    /**
     * 载入和风天气数据
     */
    private void heWeather() {
        //获取实时天气
        HeWeather.getWeatherNow(this, cityCode, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
                Toast.makeText(WeatherActivity.this, "天气获取失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(List<Now> list) {
                StringBuilder temperature = new StringBuilder();

                for (Now now : list) {
                    tvCityName.setText(now.getBasic().getLocation());
                    tvUpdateTime.setText(now.getUpdate().getLoc());
                    temperature.append(now.getNow().getFl())
                            .append("℃");
                    tvTemperature.setText(temperature.toString());
                    tvWeather.setText(now.getNow().getCond_txt());
                    //载入天气图标
                    loadWeatherIcon(now.getNow().getCond_code(),imgWeatherIcon);
                }

            }
        });

        //获取未来7天天气
        HeWeather.getWeatherForecast(this, cityCode, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
                Toast.makeText(WeatherActivity.this, "未来7天天气获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                forecastLayout.removeAllViews();
                Log.d(TAG, "未来7天天气预报 "+list.size());
                for (Forecast forecast : list) {
                    for (ForecastBase forecastBase : forecast.getDaily_forecast()) {
                        View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
                        TextView dateText = view.findViewById(R.id.tv_date);
                        TextView infoText = view.findViewById(R.id.tv_info);
                        TextView maxAndMin = view.findViewById(R.id.tv_max_min_temperature);
                        ImageView imageView = view.findViewById(R.id.iv_icon);
                        Log.d(TAG, "7天预报数据: "+forecast.getDaily_forecast().size());
                        dateText.setText(forecastBase.getDate());
                        infoText.setText(forecastBase.getCond_txt_d());
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(forecastBase.getTmp_min())
                                .append("-")
                                .append(forecastBase.getTmp_max())
                                .append("℃");
                        maxAndMin.setText(stringBuilder.toString());
                        loadWeatherIcon(forecastBase.getCond_code_d(),imageView
                        );
                        forecastLayout.addView(view);
                    }

                }
            }
        });
    }

    /**
     * 获取万年历数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED,sticky = true)
    public void calendarEvent(CalendarEvent calendarEvent) {
            Log.d(TAG, "reason: "+calendarEvent.getReason());
            Log.d(TAG, "suit: "+calendarEvent.getSuit());
            tvSuit.setText(calendarEvent.getSuit());
            Log.d(TAG, "avoid: "+calendarEvent.getAvoid());
            tvAvoid.setText(calendarEvent.getAvoid());


    }
    /**
     * 载入天气图标
     * @param code
     * @param imageView
     */
    private void loadWeatherIcon(String code,ImageView imageView) {
        StringBuilder uri = new StringBuilder();
        uri.append("http://www.hzmeurasia.cn/weather_icon/")
                .append(code)
                .append(".png");
        Glide.with(MyApplication.getContext()).load(uri.toString()).into(imageView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁EventBus
        EventBus.getDefault().unregister(this);
    }
}
