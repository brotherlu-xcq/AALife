package com.aalife.utils;

import com.aalife.constant.SystemConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-08
 */
public class FormatUtil {
    private FormatUtil(){}

    /**
     * 时间字符串转时间
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseString2Date(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateStr);
    }

    /**
     * 时间转时间字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate2String(Date date, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatDate2CommonString(Date date, String pattern){
        int hours = DateUtil.getHoursGap(date, new Date());
        int dayGap = hours/24;
        if (dayGap == 0){
            return SystemConstant.TODAY;
        } else if (dayGap == 1){
            return SystemConstant.YESTODAY;
        } else {
            return formatDate2String(date, pattern);
        }
    }

    public static String formatDate2SpecialString(Date date){
        return "1小时前";
    }
}
