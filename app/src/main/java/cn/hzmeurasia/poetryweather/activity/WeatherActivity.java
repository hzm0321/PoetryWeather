package cn.hzmeurasia.poetryweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.xujiaji.happybubble.BubbleDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.PoetryDialog;
import cn.hzmeurasia.poetryweather.R;
import cn.hzmeurasia.poetryweather.db.PoetryDb;
import cn.hzmeurasia.poetryweather.entity.CalendarEvent;
import cn.hzmeurasia.poetryweather.entity.Weather;
import cn.hzmeurasia.poetryweather.util.HeWeatherUtil;
import cn.hzmeurasia.poetryweather.util.ImageUtil;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.support.v7.widget.ListPopupWindow.WRAP_CONTENT;
import static cn.hzmeurasia.poetryweather.MyApplication.getContext;

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
    @BindView(R.id.tv_weather_date)
    TextView tvDate;
    @BindView(R.id.tv_weather_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_poetry1)
    TextView tvPoetry01;
    @BindView(R.id.tv_poetry2)
    TextView tvPoetry02;
    @BindView(R.id.iv_weather_icon)
    ImageView imgWeatherIcon;
    @BindView(R.id.btn_weather_back)
    Button btnWeatherBack;
    @BindView(R.id.btn_weather_share)
    Button btnShare;
    @BindView(R.id.tv_weather_aqi)
    TextView tvAqi;
    LinearLayout forecastLayout;
    LinearLayout alternationLayout;
    @BindView(R.id.iv_weather_bg)
    ImageView ivBg;
    @BindView(R.id.viewPage)
    ViewPager mViewPager;
    @BindView(R.id.rf_weather)
    QMUIPullRefreshLayout mPullRefreshLayout;

    PoetryDb getPoetryDb;
    LayoutInflater mInflater;
    View view01,view02,view03,view04,lifeStyleView;
    TextView tvSuit,tvAvoid,tvComf,tvDrsg,tvFlu,tvUv,tvSport,tvAir,tvLifeStyle;


    private String lunar,date,comf,drsg,flu,uv,sport,air;
    private Intent intent = null;
    private String cityCode = null;
    private List<View> mListView = new ArrayList<>();
    private int dateOnclickFlag = 0;

    QMUITipDialog tipDialog;
    private BubbleDialog.Position mPosition = BubbleDialog.Position.RIGHT;

    @OnClick({R.id.tv_poetry1,R.id.tv_poetry2,R.id.btn_weather_back,R.id.tv_cityName,R.id.tv_weather_date,R.id.btn_weather_share})
    void onClick(View v){
        switch(v.getId()) {
            case R.id.tv_poetry1:
            case R.id.tv_poetry2:
                if (getPoetryDb.getPoetryDb_annotation() != null) {
                    PoetryDialog poetryDialog = new PoetryDialog(this)
                            .setPosition(mPosition)
                            .setClickedView(tvPoetry02);
                    poetryDialog.setClickListener(str ->intent());
                    poetryDialog.show();
                }
                break;
            case R.id.btn_weather_back:
                finish();
                break;
            case R.id.tv_cityName:
            case R.id.tv_weather_date:
                if (dateOnclickFlag % 2 == 0) {
                    tvDate.setText(lunar);
                    dateOnclickFlag++;
                } else {
                    tvDate.setText(date);
                    dateOnclickFlag++;
                }
                break;
            case R.id.btn_weather_share:
                FrameLayout frameLayout = findViewById(R.id.frameLayout);
                btnWeatherBack.setVisibility(View.GONE);
                btnShare.setVisibility(View.GONE);
                ImageUtil.getBitmapByView(this,frameLayout);
                btnWeatherBack.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
                showShare();
                break;
            default:
                break;
        }
    }

    private void intent() {
        Intent intent = new Intent(WeatherActivity.this, WebViewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

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
        initLifeStyleSharePreference();


        //刷新监听
        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                heWeather();
                initLifeStyleSharePreference();
                mPullRefreshLayout.finishRefresh();
            }
        });


    }


    /**
     * 初始化lifestyle数据
     */
    private void initLifeStyleSharePreference() {
        SharedPreferences sp = getSharedPreferences("lifeStyle", MODE_PRIVATE);
        comf = sp.getString("comf","暂未获取到数据,请刷新后重试");
        drsg = sp.getString("drsg","暂未获取到数据,请刷新后重试");
        flu = sp.getString("flu","暂未获取到数据,请刷新后重试");
        uv = sp.getString("uv","暂未获取到数据,请刷新后重试");
        sport = sp.getString("sport","暂未获取到数据,请刷新后重试");
        air = sp.getString("air","暂未获取到数据,请刷新后重试");

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.shareName));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://hzmeurasia.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //确保SDcard下面存在此张图片
        oks.setImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenPoetryWeather.png");
        // url在微信、微博，Facebook等平台中使用
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams shareParams) {
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    shareParams.setText(null);
                    shareParams.setTitle(null);
                    shareParams.setTitleUrl(null);
                    shareParams.setImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenPoetryWeather.png");
                }
            }
        });
        oks.setUrl("http://hzmeurasia.cn");
        // 启动分享GUI
        oks.show(this);
    }

    /**
     * viewPager初始化
     */
    private void initViews() {
        mInflater = getLayoutInflater();
        view01 = mInflater.inflate(R.layout.weather_fortune, null);
        view02 = mInflater.inflate(R.layout.weather_alternation, null);
        view03 = mInflater.inflate(R.layout.weather_forecast, null);
        view04 = mInflater.inflate(R.layout.weather_lifestyle, null);


        alternationLayout = view02.findViewById(R.id.ll_alternation_forecast);
        forecastLayout = view03.findViewById(R.id.ll_forecast_forecast);

        tvSuit = view01.findViewById(R.id.tv_weather_suitable);
        tvAvoid = view01.findViewById(R.id.tv_weather_avoid);

        tvComf = view04.findViewById(R.id.tv_weather_comf);
        tvDrsg = view04.findViewById(R.id.tv_weather_drsg);
        tvFlu = view04.findViewById(R.id.tv_weather_flu);
        tvUv = view04.findViewById(R.id.tv_weather_uv);
        tvSport = view04.findViewById(R.id.tv_weather_sport);
        tvAir = view04.findViewById(R.id.tv_weather_air);

        //文字dialog监听
        tvComf.setOnClickListener(v -> showLifeStyleDialog(comf,tvComf));
        tvDrsg.setOnClickListener(v -> showLifeStyleDialog(drsg,tvDrsg));
        tvFlu.setOnClickListener(v -> showLifeStyleDialog(flu,tvFlu));
        tvUv.setOnClickListener(v -> showLifeStyleDialog(uv,tvUv));
        tvSport.setOnClickListener(v -> showLifeStyleDialog(sport,tvSport));
        tvAir.setOnClickListener(v -> showLifeStyleDialog(air,tvAir));

        mListView.add(view01);
        mListView.add(view02);
        mListView.add(view03);
        mListView.add(view04);

        mViewPager.setAdapter(new MyPagerAdapter());
        //设置当前page
        mViewPager.setCurrentItem(0);
    }

    private void showLifeStyleDialog(String text,TextView view) {
        lifeStyleView = mInflater.inflate(R.layout.weather_lifestyle_dialog, null);
        tvLifeStyle = lifeStyleView.findViewById(R.id.tv_weather_lifeStyle_dialog);
        tvLifeStyle.setText(text);
        new BubbleDialog(WeatherActivity.this)
                        .addContentView(lifeStyleView)
                        .setClickedView(view)
                        .calBar(true)
                        .show();

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
     * 加载背景监听
     */
    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // todo log exception
            Log.d(TAG,"onException:e="+e.toString()+";target:"+target+";isFirstResource="+isFirstResource);
            Toast.makeText(getApplicationContext(),"资源加载异常",Toast.LENGTH_SHORT).show();

            // important to return false so the error placeholder can be placed
            return false;
        }


        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e(TAG, "isFromMemoryCache:"+isFromMemoryCache+"  model:"+model+" isFirstResource: "+isFirstResource);
            Toast.makeText(getApplicationContext(),"背景图片加载完成",Toast.LENGTH_SHORT).show();
            return false;
        }
    };



    /**
     * 载入和风天气数据
     */
    private void heWeather() {
        showLoading("正在加载天气数据......");
        HeWeatherUtil.handleAirResponse(cityCode,tvAqi);
        //加载背景图片
        Log.d(TAG, "heWeather: 加载背景");
        Glide.with(this)
                .load("http://www.hzmeurasia.cn/background/bg.png")
                .listener(requestListener)
                .placeholder(R.drawable.default_bg)
                .into(ivBg);

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
                    temperature.append(now.getNow().getFl())
                            .append("°");
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

        //获取生活指数
        HeWeatherUtil.handleWeatherLifeStyleResponse(WeatherActivity.this,cityCode,tvComf,tvDrsg,tvFlu,tvUv,tvSport,tvAir);

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
                getPoetry();

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
        lunar = calendarEvent.getLunarYear() + calendarEvent.getLunar();
        date = calendarEvent.getDate();
        TextPaint textPaint = tvDate.getPaint();
        textPaint.setFakeBoldText(true);
        tvDate.setText(date);
    }

    /**
     * 获取诗句
     */
    private void getPoetry() {
        String poetry = null;
        SharedPreferences sharedPreferences = getSharedPreferences("person", MODE_PRIVATE);
        String p = sharedPreferences.getString("preferenceFlag","[]");
        p = p.replace("[","");
        p = p.replace("]","");
        p = p.replaceAll(",", "");
        String qwxl = "0";
        String jygk = "0";
        String yyql = "0";
        if (p.contains("0")) {  qwxl="1";}
        if (p.contains("1")) {  jygk="1";}
        if (p.contains("2")) {  yyql="1";}
        Log.d(TAG, "getPoetry: P "+p );
        Log.d(TAG, "getPoetry: qjy "+ qwxl+jygk+yyql);
        Log.d(TAG, "getPoetry: text"+tvWeather.getText().toString());
        List<PoetryDb> poetryDbs = LitePal
                .select("poetryDb_poetry","poetryDb_author","poetryDb_annotation","poetryDb_poetry_link")
                .where("poetryDb_weather like ? and (poetryDb_qwxl=? or poetryDb_jygk=? or poetryDb_yyql=?)", "%"+tvWeather.getText().toString()+"%",qwxl,jygk,yyql)
                .find(PoetryDb.class);
        Log.d(TAG, "getPoetry: listSize"+poetryDbs.size());
        if (poetryDbs.size() >= 1) {
            getPoetryDb = poetryDbs.get(new Random().nextInt(poetryDbs.size()));
            poetry = getPoetryDb.getPoetryDb_poetry();
            String author = getPoetryDb.getPoetryDb_author();
            String annotation = getPoetryDb.getPoetryDb_annotation();
            String poetry_link = getPoetryDb.getPoetryDb_poetry_link();
            Log.d(TAG, "getPoetry: "+author);
            //添加缓存
            SharedPreferences.Editor editor = getSharedPreferences("poetry_detail", MODE_PRIVATE).edit();
            editor.putString("poetry_link", poetry_link);
            editor.putString("author", author);
            editor.putString("annotation", annotation);
            editor.apply();
        } else {
            Toast.makeText(WeatherActivity.this,"诗句获取失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "getPoetry: " + poetry);
        String[] poetrys = poetry.split(",");
        tvPoetry01.setText(poetrys[0]);
        tvPoetry02.setText(poetrys[1]);
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
        Glide.with(getContext()).load(uri.toString()).into(imageView);
    }

    /**
     * 显示加载进度框
     */
    private void showLoading(String text) {
        tipDialog = new QMUITipDialog.Builder(WeatherActivity.this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(text)
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
