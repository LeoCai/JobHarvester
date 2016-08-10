package com.leocai.bbscraw.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by caiqingliang on 2016/8/1.
 */
public interface CrawlerService {

    void continueCraw();

    @PreDestroy void close();

}
