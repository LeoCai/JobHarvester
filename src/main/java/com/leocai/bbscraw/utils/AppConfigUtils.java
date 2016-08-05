package com.leocai.bbscraw.utils;

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

    private static boolean debug        = true;
    private static boolean mysqlEnabled = true;
    private static boolean redisEnabled = true;
    private static Logger  logger       = Logger.getLogger(AppConfigUtils.class);

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/app.properties");
        Properties appProperties = new Properties();
        try {
            appProperties.load(in);
            debug = Boolean.parseBoolean(appProperties.getProperty("debug"));
            mysqlEnabled = Boolean.parseBoolean(appProperties.getProperty("mysql.enable"));
            redisEnabled = Boolean.parseBoolean(appProperties.getProperty("redis.enable"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isMySQLEnabled() {
        return mysqlEnabled;
    }

    public static boolean isRedisEnabled() {
        return redisEnabled;
    }

}
