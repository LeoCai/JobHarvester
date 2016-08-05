package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoCacheService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.JobInfoUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by caiqingliang on 2016/8/4.
 */
@Service public class JobInfoCacheServiceImpl implements JobInfoCacheService {

    public static final String JBSORTED = "jbsorted";
    public static final String JBHASH   = "jbhash:";
    private             Logger logger   = Logger.getLogger(getClass());

    @Getter @Setter @Autowired private Properties redisProperties;
    private                            Jedis      jedis;

    @PostConstruct public void init() {
        logger.info("init");
        try {
            jedis = new Jedis(redisProperties.getProperty("url"),
                              Integer.parseInt(redisProperties.getProperty("port")));
            logger.info("redis connected");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<JobInfo> getFromCache() {
        List<JobInfo> list = new ArrayList<>();
        Set<String> keys = jedis.zrevrange(JBSORTED, 0, -1);
        for (String key : keys) {
            Map<String, String> map = jedis.hgetAll(JBHASH + key);
            JobInfo jobInfo = JobInfoUtils.getJobInfoByMap(map);
            list.add(jobInfo);
        }
        return list;
    }

    //TODO 事务
    public void addCache(List<JobInfo> mysqlInfo) {
        if (jedis == null || !jedis.isConnected()) return;
        for (JobInfo jobInfo : mysqlInfo) {
            Map<String, String> map = JobInfoUtils.getMapByJobInfo(jobInfo);
            jedis.hmset(JBHASH + jobInfo.getId(), map);
            jedis.zadd(JBSORTED, jobInfo.getJobDate().getTime(), String.valueOf(jobInfo.getId()));
        }
    }

    @Override public void flush() {
        jedis.flushAll();
    }

    @PreDestroy public void close() {
        logger.info("close");
        jedis.quit();
    }
}
