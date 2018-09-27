package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: CityEntity<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/27 16:49
 */
public class CityEntity {
    private String cityCode;

    public CityEntity(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
