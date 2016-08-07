package com.leocai.bbscraw.advice;

import com.leocai.bbscraw.utils.MapUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by caiqingliang on 2016/8/7.
 * 切面，计算函数耗时，按函数总耗时排序输出（总耗时、平均单次耗时、调用次数）
 */
@Component @Aspect public class TimeAdvice {

    private Map<String, MyProfile> map = new ConcurrentHashMap<>();

    @Around("execution(* com.leocai.bbscraw.crawlers.MyCrawler.nextPage(..))"
            + "||execution(* com.leocai.bbscraw.crawlers.MyCrawler.getCuCaoTarget(..))"
            + "||execution(* com.leocai.bbscraw.crawlers.MyCrawler.getInfoDTO(..))"
            + "||execution(* com.leocai.bbscraw.services.*.*(..))"
            //            + "||execution(* com.leocai.bbscraw.*.*(..))"
            //            + "||execution(* com.leocai.bbscraw.mappers.*.*(..))"
            + "") public Object logTime(ProceedingJoinPoint pointcut) throws Throwable {
        long pre = System.nanoTime();
        Object rs = pointcut.proceed();//此处注意要返回
        String sig = pointcut.getSignature().getName();
        long time = (System.nanoTime() - pre) / 1000000;
        MyProfile profile = map.get(sig);
        if (profile == null) {
            profile = new MyProfile();
            map.put(sig, profile);
        }
        profile.incr();
        profile.addTime(time);
        return rs;
    }

    /**
     * 输出的数据结构，包含总耗时、平均单次耗时、调用次数
     * 大小根据总耗时比较
     */
    public static class MyProfile implements Comparable {

        List<Long> times = new ArrayList<>();

        {
            times = Collections.synchronizedList(times);
        }

        AtomicLong count = new AtomicLong(0);

        public void incr() {
            count.incrementAndGet();
        }

        public void addTime(long time) {
            times.add(time);
        }

        public long getAvg() {
            long avg = 0;
            int count = 0;
            for (long t : times) {
                avg += t;
                count++;
            }
            avg /= count;
            return avg;
        }

        public long getTotalCost() {
            return getAvg() * count.get();
        }

        @Override public String toString() {
            return "total-cost " + getTotalCost() + "ms\tavg-cost " + getAvg() + " ms\tcalled " + count + " times";
        }

        @Override public int compareTo(Object o) {
            return (int) (((MyProfile) o).getTotalCost() - getTotalCost());
        }
    }

    @PreDestroy public void print() {
        map = MapUtil.sortByValue(map);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println(key+"\t"+map.get(key));
        }
    }
}
