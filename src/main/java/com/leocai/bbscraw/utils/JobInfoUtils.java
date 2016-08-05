package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfo;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caiqingliang on 2016/8/4.
 */
public class JobInfoUtils {

    private static final String           ID           = "id";
    private static final String           TITLE        = "title";
    private static final String           HREF         = "href";
    private static final String           COMPANY      = "company";
    private static final String           JOB_DATE     = "jobDate";
    private static final String           HOT          = "hot";
    private static final String           SOURCE       = "source";
    private static final String           IS_READ      = "isRead";
    private static final String           CONTENT_MD_5 = "contentMD5";
    private static       SimpleDateFormat sdf          = new SimpleDateFormat("yyyyMMddHHmmss");
    private static       Logger           logger       = Logger.getLogger(JobInfoUtils.class);

    /**
     * 从map转换JobInfo
     *
     * @param jobInfo
     * @return
     */
    public static Map<String, String> getMapByJobInfo(JobInfo jobInfo) {
        Map<String, String> map = new HashMap<>();
        map.put(ID, String.valueOf(jobInfo.getId()));
        if (jobInfo.getCompany() != null) map.put(COMPANY, jobInfo.getCompany());
        try {
            map.put(JOB_DATE, sdf.format(jobInfo.getJobDate()));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        if (jobInfo.getTitle() != null) map.put(TITLE, jobInfo.getTitle());
        if (jobInfo.getContentMD5() != null) map.put(CONTENT_MD_5, jobInfo.getContentMD5());
        map.put(HOT, String.valueOf(jobInfo.getHot()));
        if (jobInfo.getSource() != null) map.put(SOURCE, jobInfo.getSource());
        if (jobInfo.getHref() != null) map.put(HREF, jobInfo.getHref());
        map.put(IS_READ, String.valueOf(jobInfo.isRead()));
        return map;
    }

    /**
     * 从JobInfo转换成map
     *
     * @param map
     * @return
     */
    public static JobInfo getJobInfoByMap(Map<String, String> map) {
        JobInfo jobInfo = new JobInfo();
        String title = map.get(TITLE);
        Date jobDate = null;
        try {
            jobDate = sdf.parse(map.get(JOB_DATE));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        String href = map.get(HREF);
        String contentMd5 = map.get(CONTENT_MD_5);
        boolean isRead = Boolean.valueOf(map.get(IS_READ));
        String source = map.get(SOURCE);
        int id = Integer.valueOf(map.get(ID));
        int hot = Integer.valueOf(map.get(HOT));
        String company = map.get(COMPANY);
        jobInfo.setId(id);
        jobInfo.setSource(source);
        jobInfo.setTitle(title);
        jobInfo.setContentMD5(contentMd5);
        jobInfo.setCompany(company);
        jobInfo.setRead(isRead);
        jobInfo.setJobDate(jobDate);
        jobInfo.setHref(href);
        jobInfo.setHot(hot);
        return jobInfo;
    }
}
