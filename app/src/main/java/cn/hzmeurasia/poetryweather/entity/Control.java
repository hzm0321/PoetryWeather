package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: Control<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/11/7 17:34
 */
public class Control {
    private int versionCode;
    private String versionName;
    private int dataBaseNumber;
    private int city_bg;
    private int weather_bg_cloudy;
    private int weather_bg_rain;
    private int weather_bg_snow;
    private int weather_bg_sunny;
    private int weather_bg_windy;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getDataBaseNumber() {
        return dataBaseNumber;
    }

    public void setDataBaseNumber(int dataBaseNumber) {
        this.dataBaseNumber = dataBaseNumber;
    }

    public int getWeather_bg_cloudy() {
        return weather_bg_cloudy;
    }

    public void setWeather_bg_cloudy(int weather_bg_cloudy) {
        this.weather_bg_cloudy = weather_bg_cloudy;
    }

    public int getWeather_bg_rain() {
        return weather_bg_rain;
    }

    public void setWeather_bg_rain(int weather_bg_rain) {
        this.weather_bg_rain = weather_bg_rain;
    }

    public int getWeather_bg_snow() {
        return weather_bg_snow;
    }

    public void setWeather_bg_snow(int weather_bg_snow) {
        this.weather_bg_snow = weather_bg_snow;
    }

    public int getWeather_bg_sunny() {
        return weather_bg_sunny;
    }

    public void setWeather_bg_sunny(int weather_bg_sunny) {
        this.weather_bg_sunny = weather_bg_sunny;
    }

    public int getWeather_bg_windy() {
        return weather_bg_windy;
    }

    public void setWeather_bg_windy(int weather_bg_windy) {
        this.weather_bg_windy = weather_bg_windy;
    }

    public int getCity_bg() {
        return city_bg;
    }

    public void setCity_bg(int city_bg) {
        this.city_bg = city_bg;
    }
}
