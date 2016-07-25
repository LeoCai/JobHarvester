import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class SJUCrawer extends MyCrawer {

    public SJUCrawer(String url) {
        super(url);
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
