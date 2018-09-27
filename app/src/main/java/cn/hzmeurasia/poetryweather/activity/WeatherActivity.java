package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.CityEntity;
import cn.hzmeurasia.poetryweather.entity.SearchCityEntity;
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

    private RelativeLayout relativeLayout;
    private Intent intent = null;
    private String cityCode = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        //控件初始化
        init();
        //获取手机屏幕高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        //获取手机状态栏高度
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        //设置RelativeLayout高度为屏幕高度-状态栏高度
        relativeLayout = findViewById(R.id.rl);
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.height = height-statusBarHeight1;
        relativeLayout.setLayoutParams(params);

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
    }

    /**
     * 载入和风天气数据
     */
    private void heWeather() {
        HeWeather.getWeatherNow(this, cityCode, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ",throwable);
                Toast.makeText(WeatherActivity.this,"天气获取失败",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(List<Now> list) {
                StringBuilder uri = new StringBuilder();
                StringBuilder temperature = new StringBuilder();

                for (Now now : list) {
                    tvCityName.setText(now.getBasic().getLocation());
                    tvUpdateTime.setText(now.getUpdate().getLoc());
                    temperature.append(now.getNow().getFl())
                            .append("°");
                    tvTemperature.setText(temperature.toString());
                    tvWeather.setText(now.getNow().getCond_txt());
                    uri.append("http://www.hzmeurasia.cn/weather_icon/")
                            .append(now.getNow().getCond_code())
                            .append(".png");
                    Glide.with(MyApplication.getContext())
                            .load(uri.toString())
                            .into(imgWeatherIcon);

                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
