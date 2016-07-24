import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NJUClaw extends MyClaw {

    public NJUClaw(String url) {
        super(url);
    }

    protected void print(WebElement we) {
        WebElement link = we.findElement(By.tagName("a"));
        System.out.println(link.getAttribute("href") + "\t" + we.getText());
    }

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText("上一页"));
        el.click();
    }

}
