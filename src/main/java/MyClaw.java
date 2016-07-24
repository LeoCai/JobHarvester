import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public abstract class MyClaw {

    private String url;
    private   String    attention[] = new String[] { "内推", "阿里", "百度", "腾讯", "大疆", "华泰", "微软", "网易", "今日头条","去哪儿","优酷","360","小米" };
    WebDriver driver      = new FirefoxDriver();
    private   int       pageNum     = 4;

    public MyClaw(String url) {
        this.url = url;

    }

    public void start() {
        driver.get(url);
        for (int i = 0; i < pageNum; i++) {
            List<WebElement> wes = getCuCaoTarget();
            for (WebElement we : wes) {
                String text = we.getText();
                if (!isAttentioned(text)) continue;
                print(we);
            }
            nextPage();
        }
    }

    protected void print(WebElement we) {
        try {
            System.out.println(we.findElement(By.tagName("a")).getAttribute("href") + "\t" + we.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<WebElement> getCuCaoTarget(){
        return driver.findElements(By.tagName("tr"));
    }

    boolean isAttentioned(String content) {
        if(content.length()>300) return false;
        for (String at : attention) {
            if (content.contains(at)) return true;
        }
        return false;
    }

    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText("上一页"));
        el.click();
    }

}
