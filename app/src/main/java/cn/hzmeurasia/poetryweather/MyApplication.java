package cn.hzmeurasia.poetryweather;

import android.app.Application;
import android.content.Context;

/**
 * 类名: MyApplication<br>
 * 功能:(全局Context)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/22 16:38
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
