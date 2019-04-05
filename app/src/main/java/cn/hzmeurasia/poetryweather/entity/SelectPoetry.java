package cn.hzmeurasia.poetryweather.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectPoetry {
    public int total;
    @SerializedName("data")
    public List<SelectPoetryId> selectId;

    public class SelectPoetryId {
        public String id;
    }
}
