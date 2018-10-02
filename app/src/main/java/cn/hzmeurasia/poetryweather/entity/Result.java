package cn.hzmeurasia.poetryweather.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 类名: Result<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/1 15:31
 */
public class Result {
    @SerializedName("data")
    private Data result_data;

    public Data getResult_data() {
        return result_data;
    }

    public void setResult_data(Data result_data) {
        this.result_data = result_data;
    }
}
