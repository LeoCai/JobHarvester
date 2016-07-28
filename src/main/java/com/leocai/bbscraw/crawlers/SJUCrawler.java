package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.JobInfo;
import com.leocai.bbscraw.JobInfoExtractUtils;
import com.leocai.bbscraw.JobInfoIndex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class SJUCrawler extends MyCrawler {

    public SJUCrawler(String url) {
        super(url);
    }

    @Override protected JobInfo getInfoDTO(WebElement we) {
        JobInfoIndex jobInfoIndex=new JobInfoIndex();
        jobInfoIndex.setHrefIdnex(1);
        jobInfoIndex.setTimeIndex(3);
        jobInfoIndex.setTitleIndex(4);
        SimpleDateFormat sdf=new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);
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
