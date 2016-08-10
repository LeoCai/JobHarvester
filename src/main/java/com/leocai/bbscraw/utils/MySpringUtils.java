package com.leocai.bbscraw.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by caiqingliang on 2016/8/8.
 */
public class MySpringUtils {
    public static final String CONFIG_SPRING_XML = "config/spring/*.xml";

    public static ApplicationContext loadContext() {
        return new ClassPathXmlApplicationContext(CONFIG_SPRING_XML);
    }
}
