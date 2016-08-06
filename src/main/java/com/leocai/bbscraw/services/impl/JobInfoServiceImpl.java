package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.comparators.HotComparator;
import com.leocai.bbscraw.mappers.JobInfoMapper;
import com.leocai.bbscraw.services.JobInfoCacheService;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by caiqingliang on 2016/7/29.
 */

@Service public class JobInfoServiceImpl implements JobInfoService {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired @Getter @Setter private JobInfoMapper       jobInfoMapper;
    @Autowired @Getter @Setter private JobInfoCacheService jobInfoCacheService;
    /**
     * 并发收集jobinfo
     */
    private Queue<JobInfo> jobInfos         = new ConcurrentLinkedQueue<>();
    /**
     * 实际包含的公司列表
     */
    //TODO 待优化，用set
    private Set<String>    avaliableComanys = new HashSet<>(20);

    public int insertJobInfo(JobInfo jobInfo) {
        int rs = -1;
        try {
            rs = jobInfoMapper.insertJobInfo(jobInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rs;
    }

    public List<JobInfo> getJobInfosFromMemory() {
        List<JobInfo> jiList = new ArrayList<>(jobInfos.size());
        for (JobInfo ji : jobInfos) {
            jiList.add(ji);
        }
        Collections.sort(jiList, new HotComparator());
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
        infoDTO.setContentMD5(DigestUtils.md5Hex(infoDTO.getTitle()));//进行MD5
        if (isDBEnabled()) bufferAdd(infoDTO);
        else {
            jobInfos.add(infoDTO);
            avaliableComanys.add(infoDTO.getCompany());
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
        return jobInfoMapper.getLatestDateBySource(source);
    }

    public List<JobInfo> getJobInfosSince(Date date) {
        return jobInfoMapper.getJobInfosSince(date);
    }

    @Override public void dropTableIfExits() {
        jobInfoMapper.dropTableIfExists();
    }

    public boolean isDBEnabled() {
        return AppConfigUtils.isMysqlEnabled();
    }

}
