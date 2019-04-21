package cn.hzmeurasia.poetryweather.entity;

import java.util.List;

public class PoetryDetail {
    public String title;//诗的标题
    public String dynasty;//诗的朝代
    public String writer;//诗的作者
    public String content;//诗的内容
    public List<String> type;//诗的类型
    public String audioUrl;//音频地址
    public String translation;//译文
    public String remark;//注释
    public String shangxi;//赏析


    public PoetryDetail(String title, String dynasty, String writer, String content,
                        List<String> type, String audioUrl, String translation, String remark, String shangxi) {
        this.title = title;
        this.dynasty = dynasty;
        this.writer = writer;
        this.content = content;
        this.type = type;
        this.audioUrl = audioUrl;
        this.translation = translation;
        this.remark = remark;
        this.shangxi = shangxi;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
