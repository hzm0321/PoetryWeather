package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: RefreshTimeEvent<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/18 20:14
 */
public class RefreshTimeEvent {
    private String refreshFlag;

    public String getRefreshFlag() {
        return refreshFlag;
    }

    public void setRefreshFlag(String refreshFlag) {
        this.refreshFlag = refreshFlag;
    }

    public RefreshTimeEvent(String refreshFlag) {
        this.refreshFlag = refreshFlag;
    }
}
