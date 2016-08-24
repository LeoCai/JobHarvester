package com.leocai.bbscraw.utils;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by caiqingliang on 2016/8/5.
 */

@Component public class AppConfigUtils {

    private static Logger logger = Logger.getLogger(AppConfigUtils.class);

    /**
     * 是否调试环境
     */
    @Getter private static boolean debug          = true;
    /**
     * 是否开启mysql
     */
    @Getter private static boolean mysqlEnabled   = true;
    /**
     * 是否删除表
     */
    @Getter private static boolean mysqlDropTable = true;
    /**
     * 是否开启redis
     */
    @Getter private static boolean redisEnabled   = true;
    /**
     * 是否重置redis
     */
    @Getter private static boolean redisFlush     = true;
    /**
     *
     */
    @Getter private static int     crawMaxNum     = 50;
    @Getter private static int     threadNum      = 5;
    @Getter private static boolean expCrawler     = false;

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "config/properties/app.properties");
        Properties appProperties = new Properties();
        try {
            appProperties.load(in);
            debug = Boolean.parseBoolean(appProperties.getProperty("debug"));
            mysqlDropTable = Boolean.parseBoolean(appProperties.getProperty("mysql.droptable"));
            mysqlEnabled = Boolean.parseBoolean(appProperties.getProperty("mysql.enable"));
            redisEnabled = Boolean.parseBoolean(appProperties.getProperty("redis.enable"));
            redisFlush = Boolean.parseBoolean(appProperties.getProperty("redis.flush"));
            crawMaxNum = Integer.parseInt(appProperties.getProperty("crawMaxNum"));
            threadNum = Integer.parseInt(appProperties.getProperty("thread.num"));
            expCrawler = Boolean.parseBoolean(appProperties.getProperty("exp"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static int consumeThreadNum = 5;

    public static int getConsumeThreadNum() {
        return consumeThreadNum;
    }

    public static void setConsumeThreadNum(int consumeThreadNum) {
        AppConfigUtils.consumeThreadNum = consumeThreadNum;
    }
}
