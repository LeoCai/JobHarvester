package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.mappers.JobInfoMapper;
import com.leocai.bbscraw.services.JobInfoService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by caiqingliang on 2016/7/29.
 */
@Service public class JobInfoServiceImpl implements JobInfoService {

    Logger logger = Logger.getLogger(getClass());
    @Autowired private JobInfoMapper jobInfoMapper;

    /**
     * 并发收集jobinfo
     */
    private Queue<JobInfo> jobInfos = new ConcurrentLinkedQueue<JobInfo>();

    /**
     * 实际包含的公司列表
     */
    //TODO 待优化，用set
    private List<String> avaliableComanys = new ArrayList<String>(20);
    public static boolean      DBEnabled        = true;

    public int insertJobInfo(JobInfo jobInfo) {
        return jobInfoMapper.insertJobInfo(jobInfo);
    }

    public List<JobInfo> getJobInfosFromMemory() {
        List<JobInfo> jiList = new ArrayList<JobInfo>(jobInfos.size());
        for (JobInfo ji : jobInfos) {
            jiList.add(ji);
        }
        Collections.sort(jiList, new Comparator<JobInfo>() {

            public int compare(JobInfo o1, JobInfo o2) {
                return o1.getJobDate().getTime() > o2.getJobDate().getTime() ? -1 : 1;
            }
        });
        return jiList;
    }

    public List<JobInfo> getJobInfosByDate(Date jobdate) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sd.format(jobdate);
        String endStr = sd.format(jobdate.getTime() + 24 * 60 * 60 * 1000);
        Date start = null;
        Date end = null;
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
        return i != 0;
    }

    public void produceJobInfo(JobInfo infoDTO) {
        if (isDBEnabled()) bufferAdd(infoDTO);
        else{
            jobInfos.add(infoDTO);
            if (!avaliableComanys.contains(infoDTO.getCompany())) {
                avaliableComanys.add(infoDTO.getCompany());
            }
        }
    }

    public List<String> getAvalibaleComanys() {
        if(isDBEnabled()) return getCompanys();
        return avaliableComanys;
    }

    public List<JobInfo> getJobInfos() {
        return jobInfoMapper.getJobInfos();
    }

    @PostConstruct
    public void createTableIfNotExits() {
        try {
            jobInfoMapper.createTableIfNotExits();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getCompanys() {
        return jobInfoMapper.getCompanys();
    }

    public void setJobInfos(Queue<JobInfo> jobInfos) {
        this.jobInfos = jobInfos;
    }

    public void setJobInfoMapper(JobInfoMapper jobInfoMapper) {
        this.jobInfoMapper = jobInfoMapper;
    }

    public boolean isDBEnabled() {
        return DBEnabled;
    }

    public void setDBEnabled(boolean DBEnabled) {
        this.DBEnabled = DBEnabled;
    }
}
