package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.crawlers.MyCrawler;
import com.leocai.bbscraw.services.CrawlerService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.leocai.bbscraw.utils.HtmlUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by caiqingliang on 2016/7/24.
 * 并行
 * StringBuffer
 */
@Service public class CrawlerServiceImpl implements CrawlerService {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 用于存放各个爬虫类
     */
    @Resource(name = "jobCrawlerMap") private Map<String, MyCrawler> jobCrawlerMap;

    @Resource(name = "expCrawlerMap") private Map<String, MyCrawler> expCrawlerMap;
    /**
     * 当前使用的爬虫种类
     */
    private                                   Map<String, MyCrawler> cuCrawlerMap;

    @Autowired private JobInfoService  jobInfoService;
    private            ExecutorService jobInfoProduceService;
    private            ExecutorService jobInfoConsumeService;

    /**
     * 使用类加载器加载各个爬虫类
     * 在school.properties中配置对于url
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    @PostConstruct public void init() {
        jobInfoProduceService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        jobInfoConsumeService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        if (!AppConfigUtils.isExpCrawler()) cuCrawlerMap = jobCrawlerMap;
        else cuCrawlerMap = expCrawlerMap;

    }

    /**
     * 从上次时间继续爬虫
     */
    public void continueCraw() {
        List<Future<String>> fts = asyncProduceJobInfo();
        asyncConsumeJobInfo();
        asyncMoniterInfoQueue();
        for(Future<String> ft:fts){
            try {
                ft.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        jobInfoService.waitConsumedEnd();
        //        writeHtml();
    }

    /**
     * 异步产生就业信息
     */
    private List<Future<String>> asyncProduceJobInfo() {
        Set<String> keys = cuCrawlerMap.keySet();
        List<Future<String>> fts = new ArrayList<>(keys.size());
        for (String key : keys) {
            Future<String> ft = jobInfoProduceService.submit(
                    new CrawlerCallableJob(cuCrawlerMap.get(key), jobInfoService));
            fts.add(ft);
        }
        return fts;
    }

    /**
     * 异步消费信息
     */
    private void asyncConsumeJobInfo() {
        for (int i = 0; i < AppConfigUtils.getConsumeThreadNum(); i++) {
            jobInfoConsumeService.execute(new ConsumeJob(jobInfoService));
        }
    }

    private void asyncMoniterInfoQueue() {
        new Thread(new MonitorJob(jobInfoService)).start();
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
            Future<String> ft = jobInfoProduceService.submit(new Callable<String>() {

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

    private void writeHtml() {
        if (!isDBEnabled()) {
            List<JobInfo> jobInfoList = jobInfoService.getJobInfosFromMemory();
            HtmlUtils.writeHtml(jobInfoList, jobInfoService);
        }
    }

    public void crawByPage() {
        asynStart();
    }

    public void close() {

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
