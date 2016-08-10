package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.crawlers.MyCrawler;
import com.leocai.bbscraw.filters.AttentionFilters;
import com.leocai.bbscraw.filters.FaceExperienceFilters;
import com.leocai.bbscraw.filters.JobInfoFilters;
import com.leocai.bbscraw.services.CrawlerService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.leocai.bbscraw.utils.AttentionUtils;
import com.leocai.bbscraw.utils.HtmlUtils;
import com.leocai.bbscraw.utils.ProfileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
    private                                   Map<String, MyCrawler> cuCrawlerMap;

    @Autowired private JobInfoService  jobInfoService;
    private            ExecutorService executorService;

    /**
     * 使用类加载器加载各个爬虫类
     * 在school.properties中配置对于url
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    @PostConstruct public void init() {
        ProfileUtils.start("init");
        executorService = Executors.newFixedThreadPool(AppConfigUtils.getThreadNum());
        ProfileUtils.end("init");
    }

    /**
     * 从上次时间继续爬虫
     */
    public void continueCraw() {
        if (!AppConfigUtils.isExpCrawler()) cuCrawlerMap = jobCrawlerMap;
        else cuCrawlerMap = expCrawlerMap;
        Set<String> keys = cuCrawlerMap.keySet();
        List<Future<String>> fts = new ArrayList<>(keys.size());
        for (final String key : keys) {
            Future<String> ft = executorService.submit(new Callable<String>() {

                public String call() throws Exception {
                    MyCrawler crawler = cuCrawlerMap.get(key);
                    Date date = jobInfoService.getLatestDateBySource(crawler.getSource());

                    crawler.init();
                    for (int i = 0; i < crawler.getPageNum(); i++) {
                        ProfileUtils.start(getClass().getSimpleName() + ".crawOnePage");
                        List<WebElement> wes = crawler.getCuCaoTarget();
                        for (WebElement we : wes) {
                            String text = we.getText();
                            if (!crawler.getAttentionFilters().isAttention(text)
                                || crawler.getAttentionFilters().isIgnored(text)) continue;
                            JobInfo infoDTO = crawler.getInfoDTO(we);
                            infoDTO.setCompany(AttentionUtils.findComany(infoDTO.getTitle()));
                            if (date != null && crawler.dateEarly(infoDTO, date)) {
                                logger.info("find date");
                                return null;
                            }
                            infoDTO.setSource(crawler.getSource());
                            if (crawler.getAttentionFilters().filted(infoDTO)) continue;
                            jobInfoService.produceJobInfo(infoDTO);
                        }
                        ProfileUtils.end(getClass().getSimpleName() + ".crawOnePage");
                        ProfileUtils.start(getClass().getSimpleName() + ".nextPage");
                        crawler.nextPage();
                        ProfileUtils.end(getClass().getSimpleName() + ".nextPage");
                    }

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
