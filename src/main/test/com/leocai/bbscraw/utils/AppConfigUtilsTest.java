package com.leocai.bbscraw.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by caiqingliang on 2016/8/5.
 */
@RunWith(SpringJUnit4ClassRunner.class) @ContextConfiguration(locations = "classpath:config/spring/spring-config.xml") public class AppConfigUtilsTest
        extends AbstractJUnit4SpringContextTests {

    @Autowired AppConfigUtils appConfigUtils;

    @Test public void isDebug() throws Exception {
        assertEquals(true, AppConfigUtils.isMysqlEnabled());
    }

    @Test public void isMySQLEnabled() throws Exception {
        assertEquals(true, AppConfigUtils.isMysqlEnabled());
    }

    @Test public void isRedisEnabled() throws Exception {
        assertEquals(true, AppConfigUtils.isMysqlEnabled());
    }

}
