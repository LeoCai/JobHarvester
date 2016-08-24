package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.services.JobInfoService;

/**
 * Created by caiqingliang on 2016/8/23.
 */
public class ConsumeJob implements Runnable {

    private final JobInfoService jobInfoService;

    public ConsumeJob(JobInfoService jobInfoService) {
        this.jobInfoService = jobInfoService;
    }

    @Override public void run() {
        while (!jobInfoService.isProducedEnd()) {
            jobInfoService.consumeJobInfo();
        }
    }
}
