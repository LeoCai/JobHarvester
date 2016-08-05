package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.beans.JobInfoIndex;
import com.leocai.bbscraw.utils.JobDateParser;
import com.leocai.bbscraw.utils.JobDateUtils;
import com.leocai.bbscraw.utils.JobInfoExtractUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NYUCrawler extends MyCrawler {

    public NYUCrawler(String url) {
        super(url);
        setSource("南邮bbs");
    }

    @Override protected JobInfo getInfoDTO(WebElement we) {

        JobInfoIndex jobInfoIndex = new JobInfoIndex();
        jobInfoIndex.setHrefIdnex(1);
        jobInfoIndex.setTimeIndex(2);
        jobInfoIndex.setTitleIndex(1);
        jobInfoIndex.setHotIndex(4);
        JobDateParser jobDateParser = new JobDateParser() {

            public Date parse(String dateStr) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                if (dateStr.contains("-")) {
                    try {
                        date = sdf.parse(dateStr);
                    } catch (ParseException e) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    try {
                        date = sdf2.parse(JobDateUtils.getTodayDateStr() + " " + dateStr);
                    } catch (ParseException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                return date;
            }
        };
        return JobInfoExtractUtils.simpleExtract(we, jobInfoIndex, jobDateParser);
    }

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText(">>"));
        el.click();
    }

}
