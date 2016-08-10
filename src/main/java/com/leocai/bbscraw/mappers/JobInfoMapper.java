package com.leocai.bbscraw.mappers;

import com.leocai.bbscraw.beans.JobInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
public interface JobInfoMapper {

    //TODO company 单独建表
    //TODO source 单独建表

    void createTableIfNotExits();

    void dropTableIfExists();

    void insertJobInfo(JobInfo jobInfo);

    List<JobInfo> getJobInfos();

    List<JobInfo> getJobInfosByDateRange(@Param("start") Date start, @Param("end") Date end);

    List<String> getCompanys();

    String getLatestMd5(@Param("source") String source);

    List<JobInfo> getJobInfosSince(@Param("date") Date date);

    Date getLatestDateBySource(@Param("source") String source);
}
