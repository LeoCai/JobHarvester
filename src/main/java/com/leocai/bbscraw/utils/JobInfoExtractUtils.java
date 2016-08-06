package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.beans.JobInfoIndex;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 * 抽取JobInfo工具类
 */
public class JobInfoExtractUtils {

    private static Logger logger = Logger.getLogger(JobInfoExtractUtils.class);

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
