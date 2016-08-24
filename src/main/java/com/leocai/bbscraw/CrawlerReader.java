package com.leocai.bbscraw;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.leocai.bbscraw.utils.HtmlUtils;
import com.leocai.bbscraw.utils.MySpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/31.
 * 从数据库读取JobInfo，并写入Html
 */
@Component public class CrawlerReader {

    @Autowired JobInfoService jobInfoService;

    public static void main(String[] args) {
        ApplicationContext applicationContext = MySpringUtils.loadContext();
        CrawlerReader crawlerReader = applicationContext.getBean("crawlerReader", CrawlerReader.class);
        crawlerReader.start();
    }

    public void start() {
        List<JobInfo> jobInfoList = jobInfoService.getJobInfos(AppConfigUtils.isRedisEnabled());

        //        for (JobInfo jobInfo : jobInfoList) {
//            System.out.println(jobInfo.getJobDate().getTime());
//        }
        HtmlUtils.writeHtml(jobInfoList, jobInfoService);
    }

}
