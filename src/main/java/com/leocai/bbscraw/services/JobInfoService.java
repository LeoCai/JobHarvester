package com.leocai.bbscraw.services;

import com.leocai.bbscraw.beans.JobInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 * JobInfo服务类
 */
public interface JobInfoService {

    /**
     * 插入新的jobinfo
     *
     * @param jobInfo
     * @return 不成功返回-1
     */
    int insertJobInfo(JobInfo jobInfo);

    /**
     * 从内存中读取JobInfo
     *
     * @return
     */
    List<JobInfo> getJobInfosFromMemory();

    /**
     * 根据日期获取JobInfo
     *
     * @param jobdate
     * @return
     */
    List<JobInfo> getJobInfosByDate(Date jobdate);

    //TODO 增加缓冲
    boolean bufferAdd(JobInfo infoDTO);

    /**
     * 产生一个JobInfo，进行处理
     * @param infoDTO
     */
    void produceJobInfo(JobInfo infoDTO);

    /**
     * 获取当前存在的公司
     * @return
     */
    List<String> getAvalibaleComanys();

    /**
     * 从数据库中获取jobInfo
     * @return
     */
    List<JobInfo> getJobInfos();

    /**
     * 重建jobinfo表
     */
    void createTableIfNotExits();

    /**
     * 从数据库中获取当前存在的公司
     * @return
     */
    List<String> getCompanys();

    /**
     * 根据source获取最近的MD5
     *
     * @param source
     * @return
     */
    String getLatestMd5(String source);




}
