package com.leocai.bbscraw.services;

import com.leocai.bbscraw.beans.JobInfo;

import java.util.List;

/**
 * Created by caiqingliang on 2016/8/4.
 */
public interface JobInfoCacheService {

    void init();

    List<JobInfo> getFromCache();

    void addCache(List<JobInfo> mysqlInfo);

    void flush();

    void close();
}
