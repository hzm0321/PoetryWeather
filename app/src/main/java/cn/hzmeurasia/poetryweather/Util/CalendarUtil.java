package cn.hzmeurasia.poetryweather.Util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.hzmeurasia.poetryweather.entity.Calendar;
import cn.hzmeurasia.poetryweather.entity.Data;
import cn.hzmeurasia.poetryweather.entity.Result;

/**
 * 类名: CalendarUtil<br>
 * 功能:(解析calendar返回的json数据)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/1 15:47
 */
public class CalendarUtil {
    private static final String TAG = "CalendarUtil";

    public static Calendar handleCalendarResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d(TAG, "handleCalendarResponse: "+jsonObject.toString());
            return new Gson().fromJson(jsonObject.toString(), Calendar.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
