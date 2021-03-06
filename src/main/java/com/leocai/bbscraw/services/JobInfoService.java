package com.leocai.bbscraw.services;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.impl.QueueProgress;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
     *
     * @param infoDTO
     */
    void produceJobInfo(JobInfo infoDTO);

    /**
     * 获取当前存在的公司
     *
     * @return
     */
    Set<String> getAvalibaleComanys();

    /**
     *
     * @param useCache
     * @return
     */
    List<JobInfo> getJobInfos(boolean useCache);

    /**
     * 重建jobinfo表
     */
    void createTableIfNotExits();

    /**
     * 从数据库中获取当前存在的公司
     *
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

    /**
     * 根据source获取数据库中最近的日期
     *
     * @param source
     * @return
     */
    Date getLatestDateBySource(String source);

    /**
     * 从某个日期开始到现在
     *
     * @param date
     * @return
     */
    List<JobInfo> getJobInfosSince(Date date);

    void dropTableIfExits();

    /**
     * 消费就业信息
     */
    void consumeJobInfo();

    /**
     * 获取消费队列的进度
     * @return
     */
    QueueProgress getQueueProgress();

    /**
     * 判断消息是否结束
     * @return
     */
    boolean isProducedEnd();

    /**
     * 判读消费是否结束
     * @return
     */
    boolean isConsumedEnd();

    /**
     * 等待消费队列为空，并设置consumedEnd
     */
    void waitConsumedEnd();
}
