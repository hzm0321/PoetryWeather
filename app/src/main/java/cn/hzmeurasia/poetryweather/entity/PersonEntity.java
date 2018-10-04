package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: PersonEntity<br>
 * 功能:(适配个人信息界面选项的实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/4 15:29
 */
public class PersonEntity {
    private int imageId;
    private String option;
    private String result;

    public PersonEntity(int imageId, String option, String result) {
        this.imageId = imageId;
        this.option = option;
        this.result = result;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
