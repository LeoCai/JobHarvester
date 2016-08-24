package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.crawlers.MyCrawler;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AttentionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by caiqingliang on 2016/8/23.
 */
public class CrawlerCallableJob implements Callable<String> {

    private final MyCrawler crawler;
    private final JobInfoService jobInfoService;
    private Logger logger = Logger.getLogger(getClass());

    public CrawlerCallableJob(MyCrawler myCrawler, JobInfoService jobInfoService) {
        this.crawler = myCrawler;
        this.jobInfoService = jobInfoService;
    }

    @Override
    public String call() throws Exception {
        Date date = jobInfoService.getLatestDateBySource(crawler.getSource());
        crawler.init();
        for (int i = 0; i < crawler.getPageNum(); i++) {
            List<WebElement> wes = crawler.getCuCaoTarget();
            for (WebElement we : wes) {
                String text = we.getText();
                if (!crawler.getAttentionFilters().isAttention(text) || crawler.getAttentionFilters().isIgnored(text))
                    continue;
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
            crawler.nextPage();
        }
        crawler.close();
        return null;
    }
}
