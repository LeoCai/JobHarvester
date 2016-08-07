package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class ZJUCrawler extends MyCrawler {

    public ZJUCrawler(String url) {
        super(url);
    }

    public JobInfo getInfoDTO(WebElement we) {
        return null;
    }

    public void nextPage() {
        driver.findElement(By.className("fenye")).findElements(By.tagName("li")).get(currentPage++).click();

    }
}
