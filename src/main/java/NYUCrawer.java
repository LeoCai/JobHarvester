import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NYUCrawer extends MyCrawer {

    public NYUCrawer(String url) {
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

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText(">>"));
        el.click();
    }

//    public static void main(String args[]) {
////                new NYUCrawer("http://bbs.cloud.icybee.cn/board/Job").start();
////        new NJUCrawer("http://bbs.nju.edu.cn/board?board=JobExpress").start();
//        new SJUCrawer("https://bbs.sjtu.edu.cn/bbsdoc?board=JobInfo").start();
//
//
//    }

}
