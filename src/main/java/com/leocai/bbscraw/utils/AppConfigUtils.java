package com.leocai.bbscraw.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by caiqingliang on 2016/8/5.
 */

@Component public class AppConfigUtils {

    private static Logger  logger       = Logger.getLogger(AppConfigUtils.class);

    @Getter private static boolean debug        = true;
    @Getter private static boolean mysqlEnabled = true;
    @Getter private static boolean redisEnabled = true;
    @Getter private static int     crawMaxNum   = 50;

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/app.properties");
        Properties appProperties = new Properties();
        try {
            appProperties.load(in);
            debug = Boolean.parseBoolean(appProperties.getProperty("debug"));
            mysqlEnabled = Boolean.parseBoolean(appProperties.getProperty("mysql.enable"));
            redisEnabled = Boolean.parseBoolean(appProperties.getProperty("redis.enable"));
            crawMaxNum = Integer.parseInt(appProperties.getProperty("crawMaxNum"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
