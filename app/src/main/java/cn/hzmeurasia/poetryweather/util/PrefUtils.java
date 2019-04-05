package cn.hzmeurasia.poetryweather.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.hzmeurasia.poetryweather.entity.PoetryDetail;

/**
 * 操作本地share工具类
 */
public class PrefUtils {
    private static final String TAG = "PrefUtils";
    public static void putPoetryDetail(PoetryDetail data, Context ctx){
        SharedPreferences sp = ctx.getSharedPreferences("PoetryDetail",
                Context.MODE_PRIVATE);
        sp.edit().putString("title", data.title).apply();
        sp.edit().putString("dynasty", data.dynasty).apply();
        sp.edit().putString("writer", data.writer).apply();
        sp.edit().putString("content", data.content).apply();
        sp.edit().putString("type", toString(data.type)).apply();
        sp.edit().putString("audioUrl", data.audioUrl).apply();
        sp.edit().putString("translation", data.translation).apply();
        sp.edit().putString("remark", data.remark).apply();
        sp.edit().putString("shangxi", data.shangxi).apply();
    }

    public static void clearPoetryDetail(Context ctx){
        SharedPreferences sp = ctx.getSharedPreferences("PoetryDetail", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static PoetryDetail getPoetryDetail(Context ctx){
        SharedPreferences sp = ctx.getSharedPreferences("PoetryDetail", Activity.MODE_PRIVATE);
        String title = sp.getString("title","");
        String dynasty = sp.getString("dynasty","");
        String writer = sp.getString("writer","");
        String content = sp.getString("content","");
        String type = sp.getString("type","");
        String audioUrl = sp.getString("audioUrl","");
        String translation = sp.getString("translation","");
        String remark = sp.getString("remark","");
        String shangxi = sp.getString("shangxi","");
        String[] types = type.split("、");
        List<String> typeList = new ArrayList<>(Arrays.asList(types));
        return new PoetryDetail(title, dynasty, writer, content, typeList, audioUrl, translation, remark, shangxi);
    }

    public static String toString(List<String> data){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            stringBuilder.append(data.get(i));
            stringBuilder.append("、");
        }
        if (stringBuilder.length() > 0){
            return stringBuilder.toString().substring(0,stringBuilder.length()-1);
        }else {
            return "";
        }
    }
}
