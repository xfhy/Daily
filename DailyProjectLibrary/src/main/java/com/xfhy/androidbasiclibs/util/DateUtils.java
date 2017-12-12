package com.xfhy.androidbasiclibs.util;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author feiyang
 * create at 2017/10/12 11:58
 * description：日期的工具类
 */
public class DateUtils {

    /**
     * 格式化Date
     *
     * @param date   Date
     * @param format 日期的格式
     * @return 返回格式化之后的字符串
     */
    public static String getDateFormatText(@NonNull Date date, @NonNull String format) {
        if (date == null || format == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 计算Date-pastDays的日期,并返回该日期格式化之后的字符串
     *
     * @param date     Date
     * @param pastDays 减去的天数
     * @return 返回减去天数之后的日期对应的Date
     */
    public static Date getPastDate(@NonNull Date date, int pastDays) {
        if (date == null) {
            return new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -pastDays);
        return calendar.getTime();
    }

}
