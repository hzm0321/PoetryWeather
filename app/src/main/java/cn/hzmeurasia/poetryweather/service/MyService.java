package cn.hzmeurasia.poetryweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hzmeurasia.poetryweather.db.PoetryDb;
import cn.hzmeurasia.poetryweather.entity.Control;
import cn.hzmeurasia.poetryweather.entity.Poetry;
import cn.hzmeurasia.poetryweather.entity.PoetryWeather;
import cn.hzmeurasia.poetryweather.entity.RefreshTimeEvent;
import cn.hzmeurasia.poetryweather.util.CalendarUtil;
import cn.hzmeurasia.poetryweather.util.ControlUtil;
import cn.hzmeurasia.poetryweather.util.DateUtil;
import cn.hzmeurasia.poetryweather.util.HttpUtil;
import cn.hzmeurasia.poetryweather.entity.CalendarEvent;
import cn.hzmeurasia.poetryweather.util.PoetryWeatherUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * 类名: MyService<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/1 15:10
 */
public class MyService extends Service {

    private static final String TAG = "MyService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LitePal.getDatabase();
        //读取control
        String controlUri = "http://hzmeurasia.cn/PoetryWeather/config";
        HttpUtil.sendOkHttpRequest(controlUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsText = response.body().string();
                Control control = ControlUtil.handleControlResponse(responsText);
                if (control != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("control", MODE_PRIVATE).edit();
                    editor.putInt("weather_bg_cloudy", control.getWeather_bg_cloudy());
                    editor.putInt("weather_bg_rain", control.getWeather_bg_rain());
                    editor.putInt("weather_bg_snow", control.getWeather_bg_rain());
                    editor.putInt("weather_bg_sunny", control.getWeather_bg_rain());
                    editor.putInt("weather_bg_windy", control.getWeather_bg_rain());
                    editor.putInt("versionCode", control.getVersionCode());
                    editor.putString("versionName", control.getVersionName());
                    editor.putInt("city_bg", control.getCity_bg());
                    editor.apply();
//                    try {
//                        Log.d(TAG, "onResponse: 服务器版本号"+control.getVersionCode());
//                        if (control.getVersionCode() > getVersionCode()) {
//                            Log.d(TAG, "onResponse: 执行了更新");
//                            SharedPreferences.Editor editor1 = getSharedPreferences("control", MODE_PRIVATE).edit();
//                            editor1.putBoolean("isUpdate", true);
//                            editor1.apply();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    createPoetryDatabase(dataBases[0]);
                }
            }
        });
        String date = DateUtil.getDateString();
        Log.d(TAG, "date: "+date);
        //读取万年历缓存
        SharedPreferences preferences = getSharedPreferences("date", MODE_PRIVATE);
        String today = preferences.getString("today", null);
        String reason = preferences.getString("reason", null);
        String suit = preferences.getString("suit", null);
        String avoid = preferences.getString("avoid", null);
        String lunar = preferences.getString("lunar", null);
        String lunarYear = preferences.getString("lunarYear", null);
        if (date.equals(today)) {
            Log.d(TAG, "onStartCommand: "+"读取缓存发送");
            EventBus.getDefault().postSticky(new CalendarEvent(reason,suit,avoid,lunar,lunarYear,today));
        } else {
            Log.d(TAG, "onStartCommand: "+"查询网络数据后发送");
            requestCalendar(date);
        }



        updateCityWeather();
        return super.onStartCommand(intent, flags, startId);

    }


    /**
     * 查询万年历显示宜忌
     * @param date
     */
    private void requestCalendar(final String date) {
        final CalendarEvent calendarEvent = new CalendarEvent();
        String calendarUri = "http://v.juhe.cn/calendar/day?date=" + date + "&key=3ec186487910553df15ad59c08761c55";
        HttpUtil.sendOkHttpRequest(calendarUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responsText = response.body().string();
                final cn.hzmeurasia.poetryweather.entity.Calendar calendar = CalendarUtil.handleCalendarResponse(responsText);

                if (calendar != null) {
                    calendarEvent.setReason(calendar.getReason());
                    Log.d(TAG, "onResponse: " + calendar.getReason());
                    Log.d(TAG, "onResponse: " + calendarEvent.getReason());
                    calendarEvent.setSuit(calendar.getResult().getResult_data().getSuit());
                    calendarEvent.setAvoid(calendar.getResult().getResult_data().getAvoid());
                    calendarEvent.setLunar(calendar.getResult().getResult_data().getLunar());
                    calendarEvent.setLunarYear(calendar.getResult().getResult_data().getLunarYear());
                    calendarEvent.setDate(calendar.getResult().getResult_data().getToday());
                    //把万年历数据载入缓存
                    SharedPreferences.Editor editor = getSharedPreferences("date", MODE_PRIVATE).edit();
                    editor.putString("reason", calendar.getReason());
                    editor.putString("today", date);
                    editor.putString("suit", calendar.getResult().getResult_data().getSuit());
                    editor.putString("avoid", calendar.getResult().getResult_data().getAvoid());
                    editor.putString("lunar", calendar.getResult().getResult_data().getLunar());
                    editor.putString("lunarYear", calendar.getResult().getResult_data().getLunarYear());
                    editor.apply();
                    //发送粘性事件
                    EventBus.getDefault().postSticky(calendarEvent);
                }
            }

        });
    }

    /**
     * 后台更新图片
     */
    private void updateWeatherBg() {
        //向服务器发送请求,是否有新图片
        //如果有新图片,开始加载
        String uri = "http://www.hzmeurasia.cn/background/bg.png";
        HttpUtil.sendOkHttpRequest(uri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: 服务中图片加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bg = response.body().string();
                SharedPreferences.Editor editor = getSharedPreferences("background", MODE_PRIVATE).edit();
                editor.putString("Bg", bg);
                editor.apply();
            }
        });
    }

    /**
     * 定时任务自动更新城市列表天气
     */
    private void updateCityWeather() {
        Log.d(TAG, "updateCityWeather: 开始刷新天气");
        SharedPreferences preferences = getSharedPreferences("person", MODE_PRIVATE);
        int time = preferences.getInt("refreshFlag", 3);
        List<Integer> timeList = new ArrayList<>();
        timeList.add(1);
        timeList.add(2);
        timeList.add(4);
        timeList.add(8);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Log.d(TAG, "updateCityWeather: time"+time);
        //设定间隔时间
        Log.d(TAG, "updateCityWeather: 刷新间隔时间"+timeList.get(time));
        int hours = timeList.get(time) * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + hours;
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        assert manager != null;
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    /**
     * 获取服务器诗句数据库数据
     */
//    private void createPoetryDatabase(int dataBases) {
//        int locationOfficialPoetryDatabases = LitePal.where("poetryDb_id < ?", "200").count(PoetryDb.class);
//        if (dataBases > locationOfficialPoetryDatabases) {
//            String poetryWeatherUrl = "http://hzmeurasia.cn/poetry_weather/poetry";
//            HttpUtil.sendOkHttpRequest(poetryWeatherUrl, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    LitePal.deleteAll(PoetryDb.class);
//                    final String responsText = response.body().string();
//                    final PoetryWeather poetryWeather = PoetryWeatherUtil.handlePoetryWeatherResponse(responsText);
//                    for (Poetry poetry : poetryWeather.poetryList) {
//                        PoetryDb poetryDb = new PoetryDb();
//                        poetryDb.setPoetryDb_id(poetry.id);
//                        poetryDb.setPoetryDb_poetry(poetry.poetry);
//                        poetryDb.setPoetryDb_poetry_link(poetry.poetry_link);
//                        poetryDb.setPoetryDb_weather(poetry.weather);
//                        Log.d(TAG, "onResponse: 作者"+poetry.author);
//                        poetryDb.setPoetryDb_author(poetry.author);
//                        poetryDb.setPoetryDb_annotation(poetry.annotation);
//                        poetryDb.setPoetryDb_qwxl(poetry.qwxl);
//                        poetryDb.setPoetryDb_jygk(poetry.jygk);
//                        poetryDb.setPoetryDb_yyql(poetry.yyql);
//                        poetryDb.save();
//                    }
//                }
//            });
//        }
//    }

    /**
     * 获取当前程序的版本名
     */
    private String getVersionName() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG","版本号"+packInfo.versionCode);
        Log.e("TAG","版本名"+packInfo.versionName);
        return packInfo.versionName;
    }

    /**
     * 获取当前程序的版本号
     */
    private int getVersionCode() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG","版本号"+packInfo.versionCode);
        Log.e("TAG","版本名"+packInfo.versionName);
        return packInfo.versionCode;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
