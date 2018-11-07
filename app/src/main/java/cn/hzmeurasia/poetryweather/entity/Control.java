package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: Control<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/11/7 17:34
 */
public class Control {
    private int dataBaseNumber;
    private int weather_bg_cloud;
    private int weather_bg_rain;

    public int getDataBaseNumber() {
        return dataBaseNumber;
    }

    public void setDataBaseNumber(int dataBaseNumber) {
        this.dataBaseNumber = dataBaseNumber;
    }

    public int getWeather_bg_cloud() {
        return weather_bg_cloud;
    }

    public void setWeather_bg_cloud(int weather_bg_cloud) {
        this.weather_bg_cloud = weather_bg_cloud;
    }

    public int getWeather_bg_rain() {
        return weather_bg_rain;
    }

    public void setWeather_bg_rain(int weather_bg_rain) {
        this.weather_bg_rain = weather_bg_rain;
    }
}
