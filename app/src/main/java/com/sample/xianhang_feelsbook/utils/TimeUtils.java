package com.sample.xianhang_feelsbook.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * time utils
 */
public class TimeUtils {

    public static String date2Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        return format.format(date);
    }

    /**
     * get current time
     *
     * @return eg. yyyy-MM-dd HH:mm:ss
     */
    public static String getCurTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        return df.format(new Date());
    }
}
