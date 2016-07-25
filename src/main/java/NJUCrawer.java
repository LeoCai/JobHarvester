import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NJUCrawer extends MyCrawer {

    public NJUCrawer(String url) {
        super(url);
    }

    //    protected void getInfo(WebElement we) {
//        WebElement link = we.findElement(By.tagName("a"));
//        System.out.println(link.getAttribute("href") + "\t" + we.getText());
//    }

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    protected String getInfo(WebElement we) {
        try {
            return "<li><a href='"+we.findElements(By.tagName("a")).get(1).getAttribute("href") + "'><pre>" + we.getText()+"</pre></a></li>";
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "";
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText("上一页"));
        el.click();
        currentPage++;
    }

}
