package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by caiqingliang on 2016/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration(locations = "classpath:spring-config.xml") public class JobInfoCacheServiceImplTest
        extends AbstractJUnit4SpringContextTests {

    @Autowired JobInfoCacheService jobInfoCacheService;

    @Test public void init() throws Exception {

    }

    @Test public void addCache() throws Exception {
        List<JobInfo> minfos = new ArrayList<JobInfo>();
        for (int i = 0; i < 4; i++) {
            JobInfo jbInfo = new JobInfo();
            jbInfo.setId(i);
            jbInfo.setTitle("asf" + i);
            jbInfo.setJobDate(new Date(new Date().getTime()-i*1000));
            minfos.add(jbInfo);
        }
        jobInfoCacheService.addCache(minfos);
    }

    @Test public void getFromCache() throws Exception {
        System.out.println(jobInfoCacheService.getFromCache());

    }

    @Test public void close() throws Exception {

    }

    @Test public void getRedisProperties() throws Exception {

    }

    @Test public void setRedisProperties() throws Exception {

    }

    @Test public void flush() throws Exception {
        jobInfoCacheService.flush();
    }

}
