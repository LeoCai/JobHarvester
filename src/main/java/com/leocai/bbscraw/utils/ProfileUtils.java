package com.leocai.bbscraw.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by caiqingliang on 2016/8/7.
 */
public class ProfileUtils {

    private static Map<String, Long> tempTime = new HashMap<>();
    private static Map<String, Long> avgTime  = new TreeMap<>();

    public static void start(String tag) {
        tempTime.put(tag, System.nanoTime());
    }

    public static void end(String tag) {
        Long preTime = tempTime.get(tag);
        Long avgpre = avgTime.get(tag);
        if (preTime == null) return;
        long cuTime = System.nanoTime() - preTime;
        if (avgpre != null && avgpre != 0) {
            avgpre = (avgpre + cuTime) / 2;
        } else {
            avgpre = cuTime;
        }
        avgTime.put(tag, avgpre);
    }

    public static void print() {
//        Set<String> keys = avgTime.keySet();
//        for (String key : keys) {
//            Long rs = avgTime.get(key);
//            System.out.println(key + "-avg: " + rs / 1000000 + "ms");
//        }
    }

}
