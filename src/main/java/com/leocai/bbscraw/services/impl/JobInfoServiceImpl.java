package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.comparators.TimeComparator;
import com.leocai.bbscraw.mappers.JobInfoMapper;
import com.leocai.bbscraw.services.JobInfoCacheService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by caiqingliang on 2016/7/29.
 */

@Service public class JobInfoServiceImpl implements JobInfoService {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired @Getter @Setter private JobInfoMapper       jobInfoMapper;
    @Autowired @Getter @Setter private JobInfoCacheService jobInfoCacheService;

    @Getter @Setter private BlockingQueue<JobInfo> jobInfoBlockingQueue = new LinkedBlockingQueue<>();

    /**
     * 并发收集jobinfo
     */
    private Queue<JobInfo> jobInfos         = new ConcurrentLinkedQueue<>();
    /**
     * 实际包含的公司列表
     */
    //TODO 待优化，用set
    private Set<String>    avaliableComanys = new HashSet<>(20);
    /**
     * 总共产生的信息
     */
    private int totalInfoProduced;
    /**
     * 总共消耗的信息
     */
    private int totalInfoConsumed;

    /**
     * 爬取结束
     */
    @Getter @Setter private volatile boolean producedEnd;

    /**
     * 消费结束
     */
    @Getter @Setter private volatile boolean consumedEnd;

    public int insertJobInfo(JobInfo jobInfo) {
        int rs = 1;
        try {
            jobInfoMapper.insertJobInfo(jobInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
        return rs;
    }

    public List<JobInfo> getJobInfosFromMemory() {
        List<JobInfo> jiList = new ArrayList<>(jobInfos.size());
        for (JobInfo ji : jobInfos) {
            jiList.add(ji);
        }
        Collections.sort(jiList, new TimeComparator());
        return jiList;
    }

    public List<JobInfo> getJobInfosByDate(Date jobdate) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sd.format(jobdate);
        String endStr = sd.format(jobdate.getTime() + 24 * 60 * 60 * 1000);
        Date start;
        Date end;
        try {
            start = sd.parse(startStr);
            end = sd.parse(endStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return jobInfoMapper.getJobInfosByDateRange(start, end);
    }

    public boolean bufferAdd(JobInfo infoDTO) {
        int i = insertJobInfo(infoDTO);
        return i != -1;
    }

    public void produceJobInfo(JobInfo infoDTO) {
        try {
            jobInfoBlockingQueue.put(infoDTO);
            totalInfoProduced++;
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override public void consumeJobInfo() {
        try {
            JobInfo infoDTO = jobInfoBlockingQueue.take();
            totalInfoConsumed++;
            infoDTO.setContentMD5(DigestUtils.md5Hex(infoDTO.getTitle()));//进行MD5
            if (isDBEnabled()) bufferAdd(infoDTO);
            else {
                jobInfos.add(infoDTO);
                avaliableComanys.add(infoDTO.getCompany());
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public Set<String> getAvalibaleComanys() {
        if (isDBEnabled()) {
            List<String> list = getCompanys();
            avaliableComanys.clear();
            avaliableComanys.addAll(list);
        }
        return avaliableComanys;
    }

    public List<JobInfo> getJobInfos(boolean useCache) {
        List<JobInfo> cachedInfoList = new ArrayList<>();
        int cacheSize = 0;
        Date maxCacheDate = null;
        if (useCache) {
            cachedInfoList = jobInfoCacheService.getFromCache();
            if (!CollectionUtils.isEmpty(cachedInfoList)) {
                maxCacheDate = cachedInfoList.get(0).getJobDate();
                cacheSize = cachedInfoList.size();
            }
        }
        List<JobInfo> mysqlInfo;
        if (maxCacheDate == null) mysqlInfo = jobInfoMapper.getJobInfos();
        else mysqlInfo = jobInfoMapper.getJobInfosSince(maxCacheDate);
        List<JobInfo> list = new ArrayList<>(cacheSize + mysqlInfo.size());
        list.addAll(cachedInfoList);
        list.addAll(mysqlInfo);
        jobInfoCacheService.addCache(mysqlInfo);
        return list;
    }

    @PostConstruct public void createTableIfNotExits() {
        if (!AppConfigUtils.isMysqlEnabled()) return;
        if (AppConfigUtils.isMysqlDropTable()) dropTableIfExits();
        try {
            jobInfoMapper.createTableIfNotExits();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<String> getCompanys() {
        return jobInfoMapper.getCompanys();
    }

    public String getLatestMd5(String source) {
        return jobInfoMapper.getLatestMd5(source);
    }

    public Date getLatestDateBySource(String source) {
        if (source == null) return null;
        if (!AppConfigUtils.isMysqlEnabled()) return null;
        return jobInfoMapper.getLatestDateBySource(source);
    }

    public List<JobInfo> getJobInfosSince(Date date) {
        return jobInfoMapper.getJobInfosSince(date);
    }

    @Override public void dropTableIfExits() {
        jobInfoMapper.dropTableIfExists();
    }

    @Override public QueueProgress getQueueProgress() {
        return new QueueProgress(jobInfoBlockingQueue.size(), totalInfoProduced, totalInfoConsumed);
    }

    @Override public void waitConsumedEnd() {
        while (!consumedEnd && !jobInfoBlockingQueue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        consumedEnd = true;
    }

    public boolean isDBEnabled() {
        return AppConfigUtils.isMysqlEnabled();
    }

}
