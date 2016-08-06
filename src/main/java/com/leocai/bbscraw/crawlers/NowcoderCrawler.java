package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by caiqingliang on 2016/8/6.
 */
public class NOWCODERCrawler extends MyCrawler {

    public NOWCODERCrawler(String url) {
        super(url);
        setSource("牛客网");
    }

    @Override protected JobInfo getInfoDTO(WebElement we) {
        String title = we.findElement(By.className("discuss-detail")).findElement(By.tagName("a")).getText();
        String time = we.findElement(By.className("discuss-detail")).findElement(By.tagName("p")).getText().split(
                "  ")[1];
        String hot = we.findElement(By.className("discuss-detail")).findElements(By.className("feed-legend-num")).get(
                2).getText();
        String href = we.findElement(By.className("discuss-detail")).findElements(By.tagName("a")).get(0).getAttribute(
                "href");
        JobInfo jobInfo = new JobInfo();
        jobInfo.setTitle(title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date jobdate = null;
        if(time.contains("今天")){
            jobdate = new Date();
        }else{
            try {
                jobdate = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        jobInfo.setJobDate(jobdate);
        jobInfo.setHot(Integer.parseInt(hot));
        jobInfo.setHref(href);
        return jobInfo;
    }

    @Override protected List<WebElement> getCuCaoTarget() {
        return driver.findElement(By.className("common-list")).findElements(By.tagName("li"));
    }

    @Override protected void nextPage() {
        driver.get(driver.findElement(By.className("js-next-pager")).findElement(By.tagName("a")).getAttribute("href"));
    }
}
