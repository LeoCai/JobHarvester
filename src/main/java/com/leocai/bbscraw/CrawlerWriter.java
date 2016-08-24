package com.leocai.bbscraw;

import com.leocai.bbscraw.advice.TimeAdvice;
import com.leocai.bbscraw.services.CrawlerService;
import com.leocai.bbscraw.utils.MySpringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by caiqingliang on 2016/8/1.
 * 抓取并写数据库入口
 */
@Component public class CrawlerWriter {

    @Autowired @Setter @Getter private CrawlerService crawlerService;
    @Autowired private                 TimeAdvice     timeAdvice;

    public static void main(String[] args) {
        ApplicationContext applicationContext = MySpringUtils.loadContext();
        CrawlerWriter crawlerWriter = applicationContext.getBean("crawlerWriter", CrawlerWriter.class);
        crawlerWriter.start();
        System.exit(0);
    }

    public void start() {
        //        crawlerService.crawByPage();
        crawlerService.continueCraw();
        timeAdvice.print();
    }

}
