package com.leocai.bbscraw.services;

import com.leocai.bbscraw.beans.JobInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
public interface JobInfoService {

    int insertJobInfo(JobInfo jobInfo);

    List<JobInfo> getJobInfosFromMemory();

    List<JobInfo> getJobInfosByDate(Date jobdate);

    boolean bufferAdd(JobInfo infoDTO);

    void produceJobInfo(JobInfo infoDTO);

    List<String> getAvalibaleComanys();

    List<JobInfo> getJobInfos();

    void createTableIfNotExits();

    List<String> getCompanys();


}
