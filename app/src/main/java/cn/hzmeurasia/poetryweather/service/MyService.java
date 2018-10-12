package cn.hzmeurasia.poetryweather.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import cn.hzmeurasia.poetryweather.util.CalendarUtil;
import cn.hzmeurasia.poetryweather.util.DateUtil;
import cn.hzmeurasia.poetryweather.util.HttpUtil;
import cn.hzmeurasia.poetryweather.entity.CalendarEvent;
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
        new Thread(() -> {
            updateWeatherBg();
            stopSelf();
        }).start();

        String date = DateUtil.getDateString();
        Log.d(TAG, "date: "+date);
        //读取万年历缓存
        SharedPreferences preferences = getSharedPreferences("date", MODE_PRIVATE);
        String today = preferences.getString("today", null);
        String suit = preferences.getString("suit", null);
        String avoid = preferences.getString("avoid", null);
        if (date.equals(today)) {
            Log.d(TAG, "onStartCommand: "+"读取缓存发送");
            EventBus.getDefault().postSticky(new CalendarEvent("Success",suit,avoid));
        } else {
            Log.d(TAG, "onStartCommand: "+"查询网络数据后发送");
            requestCalendar(date);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    class MyServiceThread extends Thread {
        @Override
        public void run() {

        }
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
                Log.d(TAG, "onResponse: "+calendar.getResult().getResult_data().getAvoid());
                calendarEvent.setReason(calendar.getReason());
                Log.d(TAG, "onResponse: "+calendar.getReason());
                Log.d(TAG, "onResponse: "+calendarEvent.getReason());
                calendarEvent.setSuit(calendar.getResult().getResult_data().getSuit());
                calendarEvent.setAvoid(calendar.getResult().getResult_data().getAvoid());
                //把万年历数据载入缓存
                SharedPreferences.Editor editor = getSharedPreferences("date", MODE_PRIVATE).edit();
                editor.putString("today", date);
                editor.putString("suit", calendar.getResult().getResult_data().getSuit());
                editor.putString("avoid", calendar.getResult().getResult_data().getAvoid());

                editor.apply();
                //发送粘性事件
                EventBus.getDefault().postSticky(calendarEvent);
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
}
