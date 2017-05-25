package com.njupt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Created by Administrator on 2017/5/25.
 */
public class DateUtils {

    public static boolean isDateFormater(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = format.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
