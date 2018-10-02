package cn.hzmeurasia.poetryweather.entity;

/**
 * 类名: CalendarEvent<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/2 11:59
 */
public class CalendarEvent {
    private String reason;
    private String suit;
    private String avoid;

    public CalendarEvent() {

    }

    public CalendarEvent(String reason, String suit, String avoid) {
        this.reason = reason;
        this.suit = suit;
        this.avoid = avoid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }
}
