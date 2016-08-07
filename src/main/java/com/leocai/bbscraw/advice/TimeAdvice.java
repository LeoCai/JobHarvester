package com.leocai.bbscraw.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by caiqingliang on 2016/8/7.
 */

@Aspect public class TimeAdvice {

    @Around("execution(* com.leocai.bbscraw.services.*.*(..))||execution(* com.leocai.bbscraw.*.*(..))||execution(* com.leocai.bbscraw.mappers.*.*(..))") public void logTime(
            ProceedingJoinPoint pointcut) throws Throwable {
        long pre = System.nanoTime();
        pointcut.proceed();
        System.out.println(
                pointcut.getTarget().getClass().getSimpleName() + "." + pointcut.getSignature().getName() + ": "
                + (System.nanoTime() - pre) / 1000000 + " ms");
    }

}
