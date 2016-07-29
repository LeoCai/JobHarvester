package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.utils.JobInfoExtractUtils;
import com.leocai.bbscraw.beans.JobInfoIndex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NYUCrawler extends MyCrawler {

    public NYUCrawler(String url) {
        super(url);
    }

    //    public void start() {
//        WebElement ls = driver.findElement(By.className("board-list"));
//        List<WebElement> els = ls.findElements(By.tagName("tr"));
//        for (WebElement we : els) {
//            try {
//                String text = we.findElement(By.tagName("a")).getAttribute("href") + "\t" + we.getText();
//                if (isAttentioned(we.getText())) {
//                    System.out.println(text);
//                }
//            } catch (Exception e) {
//                //                e.printStackTrace();
//            }
//        }
//    }

//    protected void getInfo(WebElement we) {
//        try {
//            System.out.println(we.findElement(By.tagName("a")).getAttribute("href") + "\t" + we.getText());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override protected JobInfo getInfoDTO(WebElement we) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        JobInfoIndex jobInfoIndex = new JobInfoIndex();
        jobInfoIndex.setHrefIdnex(1);
        jobInfoIndex.setTimeIndex(2);
        jobInfoIndex.setTitleIndex(1);
        jobInfoIndex.setHotIndex(4);
        return JobInfoExtractUtils.simpleExtract(we, jobInfoIndex, sdf, sdf2);
    }

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText(">>"));
        el.click();
    }

//    public static void main(String args[]) {
////                new com.leocai.bbscraw.crawlers.NYUCrawler("http://bbs.cloud.icybee.cn/board/Job").start();
////        new com.leocai.bbscraw.crawlers.NJUCrawler("http://bbs.nju.edu.cn/board?board=JobExpress").start();
//        new com.leocai.bbscraw.crawlers.SJUCrawler("https://bbs.sjtu.edu.cn/bbsdoc?board=JobInfo").start();
//
//
//    }

}
