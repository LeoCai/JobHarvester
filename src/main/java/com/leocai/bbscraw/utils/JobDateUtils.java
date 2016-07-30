package com.leocai.bbscraw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/30.
 */
public class JobDateUtils {

    public static String getTodayDateStr() {
        Date td = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(td);
    }

    public static String getYearStr() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }
}
