package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AttentionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;

/**
 * Created by caiqingliang on 2016/7/24.
 * 核心爬虫类，若要扩展必须继承它
 */
public abstract class MyCrawler {

    protected Logger logger      = Logger.getLogger(getClass());
    protected int    currentPage = 1;
    WebDriver driver;
    private JobInfoService jobInfoService;
    /**
     * 页面url
     */
    private String url;
    //TODO 爬的总页数
    private int pageNum = 50;
    /**
     * 爬虫来源
     */
    private String source;

    public MyCrawler(String url) {
        this.url = url;
    }

    /**
     * 初始化，禁用图片css
     */
    private void init() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.stylesheet", 2);
        profile.setPreference("permissions.default.image", 2);
        driver = new FirefoxDriver(profile);
        driver.manage().window().setPosition(new Point(-2000, 0));
    }

    /**
     * 模板模式算法
     * 粗糙定位目标位置:比如table
     *      过滤想要和不想要的信息
     *      定位行位置
     *      抽取封装JobInfo信息
     *      调用服务进行处理（此处可异步）
     * 下一页
     *
     * @return
     */
    public String start() {
        init();
        driver.get(url);
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < pageNum; i++) {
            List<WebElement> wes = getCuCaoTarget();
            for (WebElement we : wes) {
                String text = we.getText();
                if (!AttentionUtils.isAttentioned(text) || AttentionUtils.isWithOut(text)) continue;
                JobInfo infoDTO = getInfoDTO(we);
                infoDTO.setSource(source);
                jobInfoService.produceJobInfo(infoDTO);
            }
            nextPage();
        }
        return sb.toString();
    }

    /**
     * 根据行抽取JobInfo，需要重写
     *
     * @param we
     * @return
     */
    protected abstract JobInfo getInfoDTO(WebElement we);

    /**
     * 获取粗糙的目标，可能需要重写
     *
     * @return
     */
    protected List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    /**
     * 下一页，可能需要重写
     */
    protected void nextPage() {
        WebElement el = driver.findElement(By.linkText("上一页"));
        el.click();
    }

    public void close() {
        driver.close();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public JobInfoService getJobInfoService() {
        return jobInfoService;
    }

    public void setJobInfoService(JobInfoService jobInfoService) {
        this.jobInfoService = jobInfoService;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
