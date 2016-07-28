package com.leocai.bbscraw;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */
public class JobInfoExtractUtils {

    public static JobInfo simpleExtract(WebElement we, JobInfoIndex jobInfoIndex, SimpleDateFormat sdf) {
        JobInfo jobInfo = new JobInfo();
        String href = we.findElements(By.tagName("a")).get(jobInfoIndex.getHrefIdnex()).getAttribute("href");
        String timeStr = we.findElements(By.tagName("td")).get(jobInfoIndex.getTimeIndex()).getText();
        String title = we.findElements(By.tagName("td")).get(jobInfoIndex.getTitleIndex()).getText();
        Date time = new Date();
        try {
            time = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hot = 0;
        try {
            hot = Integer.parseInt(we.findElements(By.tagName("td")).get(jobInfoIndex.getHotIndex()).getText());
        } catch (NumberFormatException e) {
        }
        jobInfo.setHref(href);
        jobInfo.setTime(time);
        jobInfo.setTitle(title);
        jobInfo.setHot(hot);
        return jobInfo;
    }

}
