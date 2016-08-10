package com.leocai.bbscraw.crawlers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

/**
 * Created by caiqingliang on 2016/8/6.
 */
@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration(locations = "classpath:config/spring/*.xml") public class NOWCODERCrawlerTest
        extends AbstractJUnit4SpringContextTests {

    MyCrawler nowCoder;

    @Autowired Properties jobCrawlerConfig;

    @Before public void setUp() throws Exception {
        String url = jobCrawlerConfig.getProperty("NOWCODER");
        nowCoder = new NOWCODERCrawler(url);
    }

    @Test public void getInfoDTO() throws Exception {

    }

    @Test public void getCuCaoTarget() throws Exception {
        nowCoder.start();
    }

    @Test public void nextPage() throws Exception {

    }

}
