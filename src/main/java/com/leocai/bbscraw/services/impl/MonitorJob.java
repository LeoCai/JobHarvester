package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.services.JobInfoService;
import org.apache.log4j.Logger;

/**
 * Created by caiqingliang on 2016/8/24.
 */
public class MonitorJob implements Runnable {

    private Logger logger = Logger.getLogger(getClass());

    private final JobInfoService jobInfoService;

    public MonitorJob(JobInfoService jobInfoService) {
        this.jobInfoService = jobInfoService;
    }

    @Override public void run() {
        while (!jobInfoService.isConsumedEnd()) {
            QueueProgress queueProgress = jobInfoService.getQueueProgress();
            System.out.println(queueProgress);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
