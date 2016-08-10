package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.CrawlerService;
import com.leocai.bbscraw.services.JobInfoService;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:config/spring/*.xml")
public class JobInfoServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Test public void dropTableIfExits() throws Exception {
        jobInfoService.dropTableIfExits();
    }

    @Test public void getJobInfos() throws Exception {
        List<JobInfo> jobInfos = jobInfoService.getJobInfos(false);
        System.out.println(jobInfos.toString());
    }

    @Getter
    @Setter
    @Autowired CrawlerService crawlerService;

    @Getter
    @Setter
    @Autowired JobInfoService jobInfoService;

    @Test public void getLatestDateBySource() throws Exception {
        Date latest = jobInfoService.getLatestDateBySource("上交bbs");
        System.out.println(latest);
    }

    @Test public void insertJobInfo() throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setTitle("阿萨");
        jobInfo.setJobDate(new Date());
        int r = jobInfoService.insertJobInfo(jobInfo);
        assert r >= 1;
    }

    @Test public void getJobInfo() throws Exception {
        List<JobInfo> jobinfos = jobInfoService.getJobInfosFromMemory();
        System.out.println(jobinfos.toString());
    }

    @Test public void getJobByDate() throws Exception {
        List<JobInfo> jobInfos = jobInfoService.getJobInfosByDate(new Date());
        System.out.println(jobInfos.toString());
    }

    @Test public void createTableIfNotExits() throws Exception {
        jobInfoService.createTableIfNotExits();

    }
}
