package com.leocai.bbscraw;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.crawlers.MyCrawler;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.services.impl.JobInfoServiceImpl;
import com.leocai.bbscraw.utils.HtmlUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by caiqingliang on 2016/7/24.
 * 并行
 * StringBuffer
 */

@Component public class CrawlerStarter {

    Logger logger = Logger.getLogger(getClass());

    /**
     * 用于存放各个爬虫类
     */
    private HashMap<String, MyCrawler> map = new HashMap<String, MyCrawler>();

    @Autowired private JobInfoService jobInfoService;

    public static void main(String args[]) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        CrawlerStarter crawlerStarter = applicationContext.getBean("crawlerStarter", CrawlerStarter.class);
        long start = System.nanoTime();
        //        new com.leocai.bbscraw.CrawlerStarter().start();
        crawlerStarter.asynStart();
        System.out.println((System.nanoTime() - start) * 1.0 / 1000000000);
        System.exit(0);

    }

    /**
     * 使用类加载器加载各个爬虫类
     * 在school.properties中配置对于url
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    @PostConstruct public void init() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            properties.load(classLoader.getResourceAsStream("school.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            try {
                Class<MyCrawler> crawer = (Class<MyCrawler>) classLoader.loadClass(
                        "com.leocai.bbscraw.crawlers." + key + "Crawler");
                MyCrawler c = crawer.getConstructor(String.class).newInstance(properties.getProperty((String) key));
                c.setJobInfoService(jobInfoService);
                map.put((String) key, c);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            } catch (InstantiationException e) {
                logger.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
            properties.get(key);
        }
    }

    public void start() {
        Set<String> set = map.keySet();
        for (String key : set) {
            MyCrawler crawer = map.get(key);
            crawer.start();
        }
    }

    /**
     * 利用线程池并行收集各个高校的信息
     * 利用future进行收集
     * 若未启用mysql，将得到的数据写入到html中
     */
    public void asynStart() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<String> set = map.keySet();
        List<Future<String>> fts = new ArrayList<Future<String>>(5);
        for (final String key : set) {
            Future<String> ft = executorService.submit(new Callable<String>() {

                public String call() throws Exception {
                    String rs = map.get(key).start();
                    map.get(key).close();
                    return rs;
                }
            });
            fts.add(ft);
        }
        for (Future<String> f : fts) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (!isDBEnabled()) {
            List<JobInfo> jobInfoList = jobInfoService.getJobInfosFromMemory();
            HtmlUtils.writeHtml(jobInfoList, jobInfoService);
        }
    }

    public JobInfoService getJobInfoService() {
        return jobInfoService;
    }

    public void setJobInfoService(JobInfoService jobInfoService) {
        this.jobInfoService = jobInfoService;
    }

    //TODO需要调整
    public boolean isDBEnabled() {
        return JobInfoServiceImpl.DBEnabled;
    }

}
