package cn.hzmeurasia.poetryweather;

import android.app.Application;
import android.content.Context;


import com.mob.MobSDK;

import org.litepal.LitePal;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * 类名: MyApplication<br>
 * 功能:(全局Context)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/22 16:38
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        //注册Mob
        MobSDK.init(this);
        //注册和风天气
        HeConfig.init("HE1808181021011344","c6a58c3230694b64b78facdebd7720fb");
        HeConfig.switchToFreeServerNode();
    }

    public static Context getContext() {
        return context;
    }


}
