package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.CrawlerStarter;
import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by caiqingliang on 2016/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class JobInfoServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    CrawlerStarter crawlerStarter;

    @Autowired
    JobInfoService jobInfoService;

    @Test public void insertJobInfo() throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setTitle("dfgdfg");
        jobInfo.setJobDate(new Date());
        jobInfoService.insertJobInfo(jobInfo);
        System.out.println("asd");
    }

    @Test public void getJobInfo() throws Exception {
        List<JobInfo> jobinfos = jobInfoService.getJobInfos();
        System.out.println(jobinfos.toString());

    }

    @Test public void getJobByDate() throws Exception {
        List<JobInfo> jobInfos = jobInfoService.getJobInfosByDate(new Date());
        System.out.println(jobInfos.toString());
    }

}
