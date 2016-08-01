package com.leocai.bbscraw;

import com.leocai.bbscraw.services.CrawlerService;
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

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        CrawlerWriter crawlerWriter = applicationContext.getBean("crawlerWriter", CrawlerWriter.class);
        crawlerWriter.start();
        System.exit(0);
    }

    public void start() {
        crawlerService.crawByPage();
    }

}
