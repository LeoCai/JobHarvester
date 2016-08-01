package com.leocai.bbscraw;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/31.
 * 从数据库读取JobInfo，并写入Html
 */
@Component public class CrawerReader {

    @Autowired JobInfoService jobInfoService;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        CrawerReader crawerReader = applicationContext.getBean("crawerReader", CrawerReader.class);
        crawerReader.start();

    }

    public void start() {
        List<JobInfo> jobInfoList = jobInfoService.getJobInfos();
        HtmlUtils.writeHtml(jobInfoList, jobInfoService);
    }

}
