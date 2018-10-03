package cn.hzmeurasia.poetryweather.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 类名: DateUtil<br>
 * 功能:(日期工具类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/10/2 17:37
 */
public class DateUtil {

    /**
     * 获取当前日期几月几号
     */
    public static String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取今天往后一周的集合
     */
    public static List<String> getWeeks() {
        List<String> weeks = new ArrayList<>();
        String mMonth;
        String mDay;
        int current_day;
        int current_month;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        current_day = c.get(Calendar.DAY_OF_MONTH);
        current_month = c.get(Calendar.MONTH);
        for (int i = 0; i < 7; i++) {
            c.clear();//记住一定要clear一次
            c.set(Calendar.MONTH, current_month);
            c.set(Calendar.DAY_OF_MONTH, current_day);
            c.add(Calendar.DATE, +i);
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            String date = mMonth + "-" + mDay;
            weeks.add(date);
        }
        return weeks;
    }
}







