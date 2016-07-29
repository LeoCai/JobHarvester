package com.leocai.bbscraw.services.impl;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.mappers.JobInfoMapper;
import com.leocai.bbscraw.services.JobInfoService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/29.
 */
@Service public class JobInfoServiceImpl implements JobInfoService {

    Logger logger = Logger.getLogger(getClass());

    @Autowired private JobInfoMapper jobInfoMapper;

    public int insertJobInfo(JobInfo jobInfo) {
        return jobInfoMapper.insertJobInfo(jobInfo);
    }

    public List<JobInfo> getJobInfos() {
        return jobInfoMapper.getJobInfos();
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

    public void setJobInfoMapper(JobInfoMapper jobInfoMapper) {
        this.jobInfoMapper = jobInfoMapper;
    }
}
