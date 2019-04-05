package cn.hzmeurasia.poetryweather.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import cn.hzmeurasia.poetryweather.MyApplication;
import cn.hzmeurasia.poetryweather.entity.PoetryDetail;
import cn.hzmeurasia.poetryweather.entity.SelectPoetry;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 查找随机生成的诗句是否能有具体的解析数据
 */
public class SelectPoetryUtil {
    private static final String TAG = "SelectPoetryUtil";
    static  SelectPoetry selectPoetry;
    static String id;
    static PoetryDetail poetryDetail;
    public static void getPoetry(String address, Context ctx){


        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                selectPoetry = handleSelectPoetryResponse(response.body().string());
                Log.d(TAG, "onResponse: 查询到的诗句" + selectPoetry.total);
                if (selectPoetry.total > 0){
                    id = "https://www.caoxingyu.club/guwen/selectbyid?id="+selectPoetry.selectId.get(0).id;
                    Log.d(TAG, "onResponse: 拿到的id" + id);
                    HttpUtil.sendOkHttpRequest(id, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            poetryDetail = handlePoetryDetailResponse(response.body().string());
                            PrefUtils.putPoetryDetail(poetryDetail, ctx);
                        }
                    });
                }else {
                    PrefUtils.clearPoetryDetail(ctx);
                }
            }
        });

    }

    /**
     * 把拿到的json数据转化为实体类形式
     * @param response
     * @return
     */
    public static SelectPoetry handleSelectPoetryResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d(TAG, "handleSelectPoetryResponse: 返回查询到的诗词" + jsonObject.toString());
            return new Gson().fromJson(jsonObject.toString(), SelectPoetry.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PoetryDetail handlePoetryDetailResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d(TAG, "handlePoetryDetailResponse: 返回的详细诗词" + jsonObject.toString());
            return new Gson().fromJson(jsonObject.toString(), PoetryDetail.class);

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
