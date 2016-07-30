package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfoIndex;
import com.leocai.bbscraw.beans.JobInfo;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */
public class JobInfoExtractUtils {

    private static Logger logger = Logger.getLogger(JobInfoExtractUtils.class);

    public static JobInfo simpleExtract(WebElement we, JobInfoIndex jobInfoIndex, final SimpleDateFormat sdf) {
        JobDateParser jobDateParser = new JobDateParser() {

            public Date parse(String dateStr) {
                Date date = new Date();
                try {
                    date = sdf.parse(dateStr);
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
                return date;
            }
        };
        return simpleExtract(we, jobInfoIndex, jobDateParser);
    }

    /**
     * 抽取jobInfo
     * @param we
     * 粗糙定位节点
     * @param jobInfoIndex
     * 对应的索引
     * @param jobDateParser
     * 日期转换接口
     * @return
     */
    public static JobInfo simpleExtract(WebElement we, JobInfoIndex jobInfoIndex, JobDateParser jobDateParser) {
        JobInfo jobInfo = new JobInfo();
        String href = we.findElements(By.tagName("a")).get(jobInfoIndex.getHrefIdnex()).getAttribute("href");
        String timeStr = we.findElements(By.tagName("td")).get(jobInfoIndex.getTimeIndex()).getText();
        String title = we.findElements(By.tagName("td")).get(jobInfoIndex.getTitleIndex()).getText();
        Date time;
        time = jobDateParser.parse(timeStr);
        int hot = 0;
        try {
            hot = Integer.parseInt(we.findElements(By.tagName("td")).get(jobInfoIndex.getHotIndex()).getText());
        } catch (NumberFormatException e) {
            logger.info(e.getMessage(), e);
        }
        jobInfo.setHref(href);
        jobInfo.setJobDate(time);
        jobInfo.setTitle(title);
        jobInfo.setHot(hot);
        return jobInfo;
    }
}
