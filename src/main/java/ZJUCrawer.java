import org.openqa.selenium.By;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class ZJUCrawer extends MyCrawer {

    public ZJUCrawer(String url) {
        super(url);
    }

    protected void nextPage() {
        driver.findElement(By.className("fenye")).findElements(By.tagName("li")).get(currentPage++).click();

    }
}
