package com.leocai.bbscraw.services;

import com.leocai.bbscraw.beans.JobInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
public interface JobInfoService {
    int insertJobInfo(JobInfo jobInfo);
    List<JobInfo> getJobInfos();
    List<JobInfo> getJobInfosByDate(Date jobdate);
    boolean bufferAdd(JobInfo infoDTO);
}
