package com.aalife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
