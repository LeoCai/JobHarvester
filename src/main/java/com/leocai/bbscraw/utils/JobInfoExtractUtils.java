package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfoIndex;
import com.leocai.bbscraw.beans.JobInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */
public class JobInfoExtractUtils {

    public static String getTag(String tag, String title) {
        return "<" + tag + ">" + title + "</" + tag + ">";
    }

    public static String getTableRow(JobInfo infoDTO) {
        StringBuffer tableRow = new StringBuffer("");
        tableRow.append("<tr>");
        tableRow.append(getTdWithHref(infoDTO.getTitle(),infoDTO.getHref()));
        tableRow.append(getTag("td", infoDTO.getHot() + ""));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableRow.append(getTag("td", sd.format(infoDTO.getJobDate())));
        tableRow.append("</tr>\n");
        return tableRow.toString();
    }

    public static String getTdWithHref(String info, String href) {
        return "<td><a href='" + href + "'>" + info + "</a></td>";
    }

    public static JobInfo simpleExtract(WebElement we, JobInfoIndex jobInfoIndex, SimpleDateFormat sdf) {
        return simpleExtract(we, jobInfoIndex, sdf, null);
    }

    public static JobInfo simpleExtract(WebElement we, JobInfoIndex jobInfoIndex, SimpleDateFormat sdf,
                                        SimpleDateFormat sdf2) {
        JobInfo jobInfo = new JobInfo();
        String href = we.findElements(By.tagName("a")).get(jobInfoIndex.getHrefIdnex()).getAttribute("href");
        String timeStr = we.findElements(By.tagName("td")).get(jobInfoIndex.getTimeIndex()).getText();
        String title = we.findElements(By.tagName("td")).get(jobInfoIndex.getTitleIndex()).getText();
        Date time = new Date();
        try {
            time = sdf.parse(timeStr);
        } catch (ParseException e) {
            try {
                if (sdf2 != null) time = sdf2.parse(timeStr);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        int hot = 0;
        try {
            hot = Integer.parseInt(we.findElements(By.tagName("td")).get(jobInfoIndex.getHotIndex()).getText());
        } catch (NumberFormatException e) {
        }
        jobInfo.setHref(href);
        jobInfo.setJobDate(time);
        jobInfo.setTitle(title);
        jobInfo.setHot(hot);
        return jobInfo;
    }
}
