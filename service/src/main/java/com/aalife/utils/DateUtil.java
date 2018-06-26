package com.aalife.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author mosesc
 * @date 2018-06-26
 */
public class DateUtil {
    private DateUtil(){}

    public static int getHoursGap(Date start, Date end){
        long startTime = start.getTime();
        long endTime = end.getTime();
        int diff = (int) (endTime-startTime/(60*60*1000));
        return diff;
    }
}
