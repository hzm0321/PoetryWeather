package cn.hzmeurasia.poetryweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * 类名: CityDb<br>
 * 功能:(主界面City列表数据库字段<br>
 * 作者:黄振敏 <br>
 * 日期:2018/9/27 15:56
 */
public class CityDb extends LitePalSupport {

    private String cityDb_cid;
    private String cityDb_cityName;
    private String cityDb_txt;
    private String cityDb_temperature;
    private String cityDb_imageId;

    public CityDb() {

    }

    public CityDb(String cityDb_cid, String cityDb_cityName, String cityDb_txt, String cityDb_temperature, String cityDb_imageId) {
        this.cityDb_cid = cityDb_cid;
        this.cityDb_cityName = cityDb_cityName;
        this.cityDb_txt = cityDb_txt;
        this.cityDb_temperature = cityDb_temperature;
        this.cityDb_imageId = cityDb_imageId;
    }

    public String getCityDb_cid() {
        return cityDb_cid;
    }

    public void setCityDb_cid(String cityDb_cid) {
        this.cityDb_cid = cityDb_cid;
    }

    public String getCityDb_cityName() {
        return cityDb_cityName;
    }

    public void setCityDb_cityName(String cityDb_cityName) {
        this.cityDb_cityName = cityDb_cityName;
    }

    public String getCityDb_txt() {
        return cityDb_txt;
    }

    public void setCityDb_txt(String cityDb_txt) {
        this.cityDb_txt = cityDb_txt;
    }

    public String getCityDb_temperature() {
        return cityDb_temperature;
    }

    public void setCityDb_temperature(String cityDb_temperature) {
        this.cityDb_temperature = cityDb_temperature;
    }

    public String getCityDb_imageId() {
        return cityDb_imageId;
    }

    public void setCityDb_imageId(String cityDb_imageId) {
        this.cityDb_imageId = cityDb_imageId;
    }
}
