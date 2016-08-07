package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.crawlers.MyCrawler;
import com.leocai.bbscraw.filters.AttentionFilters;
import com.leocai.bbscraw.filters.FaceExperienceFilters;
import com.leocai.bbscraw.filters.JobInfoFilters;
import com.leocai.bbscraw.services.CrawlerService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.leocai.bbscraw.utils.HtmlUtils;
import com.leocai.bbscraw.utils.ProfileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by caiqingliang on 2016/7/24.
 * 并行
 * StringBuffer
 */
@Service public class CrawlerServiceImpl implements CrawlerService {

    public static final String PACAGE         = "com.leocai.bbscraw.crawlers.";
    public static final String CRAWLER_SUFFIX = "Crawler";

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 爬虫配置
     */
    @Autowired private Properties jobCrawlerConfig;
    @Autowired private Properties expCrawlerConfig;

    /**
     * 用于存放各个爬虫类
     */
    private HashMap<String, MyCrawler> jobCrawlerMap = new HashMap<String, MyCrawler>();
    private HashMap<String, MyCrawler> expCrawlerMap = new HashMap<String, MyCrawler>();

    @Autowired private JobInfoService        jobInfoService;
    private            ExecutorService       executorService;
    @Autowired private FaceExperienceFilters faceExperienceFilters;
    @Autowired private JobInfoFilters        jobInfoFilters;

    /**
     * 使用类加载器加载各个爬虫类
     * 在school.properties中配置对于url
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    @PostConstruct public void init() {
        ProfileUtils.start("init");
        loadCrawlers();
        executorService = Executors.newFixedThreadPool(AppConfigUtils.getThreadNum());
        ProfileUtils.end("init");
    }

    /**
     * 类加载器加载爬虫类放到map中
     */
    //TODO properties 使用工厂模式
    public void loadCrawlers() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        loadCrawlers(classLoader, jobCrawlerConfig, jobCrawlerMap, jobInfoFilters);
        //        loadCrawlers(classLoader, expCrawlerConfig, expCrawlerMap, faceExperienceFilters);
    }

    /**
     * 类加载器加载爬虫类
     *
     * @param classLoader      类加载器
     * @param crawlerConfig    爬虫配置
     * @param crawlerMap       爬虫Map
     * @param attentionFilters 爬虫过滤器
     */
    private void loadCrawlers(ClassLoader classLoader, Properties crawlerConfig, HashMap<String, MyCrawler> crawlerMap,
                              AttentionFilters attentionFilters) {
        Set<Object> keys = crawlerConfig.keySet();
        for (Object key : keys) {
            try {
                Class<?> mc = classLoader.loadClass(PACAGE + key + CRAWLER_SUFFIX);
                Class<MyCrawler> crawer = (Class<MyCrawler>) mc;
                MyCrawler c = crawer.getConstructor(String.class).newInstance(crawlerConfig.getProperty((String) key));
                c.setJobInfoService(jobInfoService);
                c.setAttentionFilters(attentionFilters);
                crawlerMap.put((String) key, c);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 从上次时间继续爬虫
     */
    public void continueCraw() {
        Set<String> keys = jobCrawlerMap.keySet();
        List<Future<String>> fts = new ArrayList<>(keys.size());
        for (final String key : keys) {
            Future<String> ft = executorService.submit(new Callable<String>() {

                public String call() throws Exception {
                    MyCrawler crawler = jobCrawlerMap.get(key);
                    Date date = jobInfoService.getLatestDateBySource(crawler.getSource());
                    crawler.crawSince(date);
                    jobCrawlerMap.get(key).close();
                    return null;
                }
            });
            fts.add(ft);
        }
        for (Future<String> f : fts) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (!isDBEnabled()) {
            List<JobInfo> jobInfoList = jobInfoService.getJobInfosFromMemory();
            HtmlUtils.writeHtml(jobInfoList, jobInfoService);
        }
    }

    /**
     * 利用线程池并行收集各个高校的信息
     * 利用future进行收集
     * 若未启用mysql，将得到的数据写入到html中
     */
    //TODO 是否可以多个标签页并行
    public void asynStart() {
        Set<String> set = jobCrawlerMap.keySet();
        List<Future<String>> fts = new ArrayList<>(set.size());
        for (final String key : set) {
            Future<String> ft = executorService.submit(new Callable<String>() {

                public String call() throws Exception {
                    String rs = jobCrawlerMap.get(key).start();
                    jobCrawlerMap.get(key).close();
                    return rs;
                }
            });
            fts.add(ft);
        }
        for (Future<String> f : fts) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (!isDBEnabled()) {
            List<JobInfo> jobInfoList = jobInfoService.getJobInfosFromMemory();
            HtmlUtils.writeHtml(jobInfoList, jobInfoService);
        }
    }

    public void crawlerToday() {

    }

    public void crawByPage() {
        asynStart();
    }

    public void close() {

    }

    public void crawByDate() {

    }

    public void start() {
        Set<String> set = jobCrawlerMap.keySet();
        for (String key : set) {
            MyCrawler crawer = jobCrawlerMap.get(key);
            crawer.start();
        }
    }

    //TODO 需要调整
    public boolean isDBEnabled() {
        return AppConfigUtils.isMysqlEnabled();
    }
}
