package com.leocai.bbscraw.crawlers;

import org.openqa.selenium.By;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class ZJUCrawler extends MyCrawler {

    public ZJUCrawler(String url) {
        super(url);
    }

    protected void nextPage() {
        driver.findElement(By.className("fenye")).findElements(By.tagName("li")).get(currentPage++).click();

    }
}
