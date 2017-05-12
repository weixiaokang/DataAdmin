package com.njupt;

import java.util.Calendar;
import java.util.Date;

/**
 * @author weixk
 * @version Created time 17/5/4. Last-modified time 17/5/4.
 */
public class Test {

    public static void main(String[] args) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.DATE));
    }
}
