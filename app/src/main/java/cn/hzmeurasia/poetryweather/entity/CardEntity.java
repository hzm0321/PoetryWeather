package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: CardEntity<br>
 * 功能:(主页面卡片布局实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/15 14:34
 */
public class CardEntity {

    private String address;
    private String weatherMessage;
    private String temperature;
    private int imageId;

    public CardEntity(String adress, String weatherMessage, String temperature,int imageId) {
        this.address = adress;
        this.weatherMessage = weatherMessage;
        this.temperature = temperature;
        this.imageId = imageId;
    }

    public String getAddress() {
        return address;
    }


    public String getWeatherMessage() {
        return weatherMessage;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getImageId() {
        return imageId;
    }
}
