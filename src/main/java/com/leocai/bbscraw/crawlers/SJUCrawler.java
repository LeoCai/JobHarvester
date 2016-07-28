package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.JobInfo;
import com.leocai.bbscraw.JobInfoExtractUtils;
import com.leocai.bbscraw.JobInfoIndex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class SJUCrawler extends MyCrawler {

    public SJUCrawler(String url) {
        super(url);
    }

    @Override protected JobInfo getInfoDTO(WebElement we) {
        JobInfoIndex jobInfoIndex=new JobInfoIndex();
        SimpleDateFormat sdf=new SimpleDateFormat();
        return JobInfoExtractUtils.simpleExtract(we, jobInfoIndex, sdf);
    }

    protected String getInfo(WebElement we) {
        try {
            return "<li><a href='" + we.findElements(By.tagName("a")).get(1).getAttribute("href") + "'><pre>" + we.getText() + "</pre></a></li>";
        } catch (Exception e) {
            //            e.printStackTrace();
        }
        return "";
    }

//    protected List<WebElement> getCuCaoTarget(){
//        return driver.findElements(By.tagName("td"));
//    }

}
