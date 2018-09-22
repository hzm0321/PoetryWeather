package cn.hzmeurasia.poetryweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.hzmeurasia.poetryweather.R;

/**
 * 类名: WeatherActivity<br>
 * 功能:(天气布局的活动)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/22 16:42
 */
public class WeatherActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
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
    }


}
