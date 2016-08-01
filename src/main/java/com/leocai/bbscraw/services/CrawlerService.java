package com.leocai.bbscraw.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by caiqingliang on 2016/8/1.
 */
public interface CrawlerService {

    @PostConstruct void loadCrawlers();

    void crawlerToday();

    void continueCraw();

    void crawByDate();

    void crawByPage();

    @PreDestroy void close();

}
