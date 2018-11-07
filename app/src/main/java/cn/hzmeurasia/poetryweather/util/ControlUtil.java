package cn.hzmeurasia.poetryweather.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import cn.hzmeurasia.poetryweather.entity.Calendar;
import cn.hzmeurasia.poetryweather.entity.Control;

/**
 * 类名: ControlUtil<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/11/7 17:33
 */
public class ControlUtil {
    public static Control handleControlResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return new Gson().fromJson(jsonObject.toString(), Control.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
