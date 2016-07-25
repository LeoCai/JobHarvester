import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public abstract class MyCrawer {

    private String url;
    private String attention[] = new String[] { "内推", "阿里", "百度", "腾讯", "大疆", "华泰", "微软", "网易", "今日头条", "去哪儿", "优酷",
                                                "360", "小米","银行","好未来","摩根","谷歌" };
    private String withOut[] = new String[]{"是不是","咋","?","？","呢","为什么","求","实习","请问"};
    WebDriver driver;
    private int pageNum = 10;

    int currentPage = 1;

    public MyCrawer(String url) {
        this.url = url;
    }

    private void init() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.stylesheet", 2);
        profile.setPreference("permissions.default.image", 2);
        driver = new FirefoxDriver(profile);
    }

    public String start() {
        init();
        driver.get(url);
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < pageNum; i++) {
            List<WebElement> wes = getCuCaoTarget();
            for (WebElement we : wes) {
                String text = we.getText();
                if (!isAttentioned(text)||isWithOut(text)) continue;
                sb.append(getInfo(we) + "\r\n");
                //                getInfo(we);
            }
            nextPage();
        }
        return sb.toString();
    }

    private boolean isWithOut(String text) {
        for(String wt:withOut){
            if(text.contains(wt)) return true;
        }
        return false;
    }

    protected String getInfo(WebElement we) {
        try {
            return "<li><a href='"+we.findElement(By.tagName("a")).getAttribute("href") + "'>" + we.getText()+"</a></li>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    boolean isAttentioned(String content) {
        if (content.length() > 300) return false;
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
