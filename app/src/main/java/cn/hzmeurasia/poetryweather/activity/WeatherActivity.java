package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.entity.CalendarEvent;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
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
    @BindView(R.id.tv_cityName)
    TextView tvCityName;
    @BindView(R.id.tv_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.tv_weather_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.iv_weather_icon)
    ImageView imgWeatherIcon;
//    @BindView(R.id.ll_forecast)
    LinearLayout forecastLayout;
    LinearLayout alternationLayout;
//    @BindView(R.id.tv_weather_suitable)
    TextView tvSuit;
//    @BindView(R.id.tv_weather_avoid)
    TextView tvAvoid;
    @BindView(R.id.iv_weather_bg)
    ImageView ivBg;
    @BindView(R.id.viewPage)
    ViewPager mViewPager;
    LayoutInflater mInflater;
    View view01,view02,view03;
//
//    private RelativeLayout relativeLayout;
    private Intent intent = null;
    private String cityCode = null;
    private List<View> mListView = new ArrayList<>();

    QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        //注册和风天气
        HeConfig.init("HE1808181021011344","c6a58c3230694b64b78facdebd7720fb");
        HeConfig.switchToFreeServerNode();

        //绑定初始化BufferKnife
        ButterKnife.bind(this);


        //注册EventBus
        EventBus.getDefault().register(this);

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

        //隐藏状态栏
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //调用天气数据
        intent = getIntent();
        cityCode = intent.getStringExtra("cityCode");
        initViews();
        heWeather();




    }

    /**
     * viewPager初始化
     */
    private void initViews() {
        mInflater = getLayoutInflater();
        view01 = mInflater.inflate(R.layout.weather_fortune, null);
        view02 = mInflater.inflate(R.layout.weather_alternation, null);
        view03 = mInflater.inflate(R.layout.weather_forecast, null);
        alternationLayout = view02.findViewById(R.id.ll_alternation_forecast);
        forecastLayout = view03.findViewById(R.id.ll_forecast_forecast);
        tvSuit = view01.findViewById(R.id.tv_weather_suitable);
        tvAvoid = view01.findViewById(R.id.tv_weather_avoid);

        mListView.add(view01);
        mListView.add(view02);
        mListView.add(view03);

        mViewPager.setAdapter(new MyPagerAdapter());
        //设置当前page
        mViewPager.setCurrentItem(0);
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mListView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListView.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListView.get(position),0);
            return mListView.get(position);
        }
    }


    /**
     * 载入和风天气数据
     */
    private void heWeather() {
        showLoading();
        Glide.with(this).load("http://www.hzmeurasia.cn/background/bg.png").into(ivBg);
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
                    loadWeatherIcon("weather_icon",now.getNow().getCond_code(),imgWeatherIcon);
                }

            }
        });

        //获取逐时天气
        HeWeather.getWeatherHourly(this, cityCode, new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
                Toast.makeText(WeatherActivity.this, "逐时天气获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<Hourly> list) {
                alternationLayout.removeAllViews();
                for (Hourly hourly : list) {
                    for (HourlyBase hourlyBase:hourly.getHourly()) {
                        Log.d(TAG, "逐小时天气 " + hourly.getHourly().size());
                        View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_alternation_item,
                                alternationLayout, false);
                        TextView timeText = view.findViewById(R.id.tv_date);
                        TextView temperature = view.findViewById(R.id.tv_temperature);
                        ImageView imageView = view.findViewById(R.id.iv_icon);
                        String[] splitTime = hourlyBase.getTime().split("\\s+");
                        timeText.setText(splitTime[1]);
                        temperature.setText(hourlyBase.getTmp()+"°");
                        loadWeatherIcon("weather_hour_icon",hourlyBase.getCond_code(),imageView);
                        alternationLayout.addView(view);
                    }
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
                        View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_forecast_item,
                                forecastLayout, false);
                        TextView dateText = view.findViewById(R.id.tv_date);
                        TextView infoText = view.findViewById(R.id.tv_weather_text);
                        TextView maxTemperature = view.findViewById(R.id.tv_max_temperature);
                        TextView minTemperature = view.findViewById(R.id.tv_min_temperature);
                        Log.d(TAG, "7天预报数据: "+forecast.getDaily_forecast().size());
                        String[] splitDate = forecastBase.getDate().split("-");

                        dateText.setText(splitDate[1]+"-"+splitDate[2]);
                        infoText.setText(forecastBase.getCond_txt_d());
                        minTemperature.setText(forecastBase.getTmp_min()+"°");
                        maxTemperature.setText(forecastBase.getTmp_max()+"°");
                        forecastLayout.addView(view);
                    }
                }
                closeLoading();
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
     *
     * @param code
     * @param imageView
     */
    private void loadWeatherIcon(String fileName, String code, ImageView imageView) {
        StringBuilder uri = new StringBuilder();
        uri.append("http://www.hzmeurasia.cn/")
                .append(fileName)
                .append("/")
                .append(code)
                .append(".png");
        Glide.with(MyApplication.getContext()).load(uri.toString()).into(imageView);
    }

    /**
     * 显示加载进度框
     */
    private void showLoading() {
        tipDialog = new QMUITipDialog.Builder(WeatherActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载天气数据......")
                .create();
        tipDialog.show();

    }

    /**
     * 关闭加载进度框
     */
    private void closeLoading() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁EventBus
        EventBus.getDefault().unregister(this);
    }
}
