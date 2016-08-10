package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.filters.AttentionFilters;
import com.leocai.bbscraw.services.JobInfoService;
import com.leocai.bbscraw.utils.AppConfigUtils;
import com.leocai.bbscraw.utils.AttentionUtils;
import com.leocai.bbscraw.utils.ProfileUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by caiqingliang on 2016/7/24.
 * 核心爬虫类，若要扩展必须继承它
 */
public abstract class MyCrawler {

    protected Logger logger      = Logger.getLogger(getClass());
    /**
     * 当前页
     */
    protected int    currentPage = 1;
    protected                          WebDriver        driver;
    @Autowired @Getter @Setter private JobInfoService   jobInfoService;
    @Getter @Setter private            AttentionFilters attentionFilters;
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    /**
     * 页面url
     */
    private                            String           url;
    @Getter @Setter private int pageNum = 3;
    /**
     * 爬虫来源
     */
    @Getter @Setter private String source;

    public MyCrawler(String url) {
        this.url = url;
    }

    /**
     * 初始化，禁用图片css
     */
    public void init() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.stylesheet", 2);
        profile.setPreference("permissions.default.image", 2);
        driver = new FirefoxDriver(profile);
        driver.manage().window().setPosition(new Point(-2000, 0));
        pageNum = AppConfigUtils.getCrawMaxNum();
        driver.get(url);
    }

    /**
     * 模板模式算法
     * 粗糙定位目标位置:比如table
     * 过滤想要和不想要的信息
     * 定位行位置
     * 抽取封装JobInfo信息
     * 调用服务进行处理（此处可异步）
     * 下一页
     *
     * @return
     */
    public String start() {
        crawSince(null);
        return null;
    }

    /**
     * 爬到某个日期位置
     *
     * @param date
     */
    public void crawSince(final Date date) {
        ProfileUtils.start(getClass().getSimpleName() + ".crawSince");
        init();
        driver.get(url);
        for (int i = 0; i < pageNum; i++) {
            ProfileUtils.start(getClass().getSimpleName() + ".crawOnePage");
            List<WebElement> wes = getCuCaoTarget();
            for (final WebElement we : wes) {

                executorService.execute(new Runnable() {

                    @Override public void run() {
                        String text = we.getText();
                        if (!attentionFilters.isAttention(text) || attentionFilters.isIgnored(text)) return;
                        JobInfo infoDTO = getInfoDTO(we);
                        infoDTO.setCompany(AttentionUtils.findComany(infoDTO.getTitle()));
                        if (date != null && dateEarly(infoDTO, date)) {
                            logger.info("find date");
                            return;
                        }
                        infoDTO.setSource(source);
                        if (attentionFilters.filted(infoDTO)) return;
                        jobInfoService.produceJobInfo(infoDTO);
                    }
                });
            }
            ProfileUtils.end(getClass().getSimpleName() + ".crawOnePage");
            ProfileUtils.start(getClass().getSimpleName() + ".nextPage");
            nextPage();
            ProfileUtils.end(getClass().getSimpleName() + ".nextPage");
        }
        ProfileUtils.end(getClass().getSimpleName() + ".crawSince");
    }

    public boolean dateEarly(JobInfo infoDTO, Date date) {
        return infoDTO.getJobDate().getTime() < date.getTime();
    }

    /**
     * 根据行抽取JobInfo，需要重写
     *
     * @param we
     * @return
     */
    public abstract JobInfo getInfoDTO(WebElement we);

    /**
     * 获取粗糙的目标，可能需要重写
     *
     * @return
     */
    public List<WebElement> getCuCaoTarget() {
        return driver.findElements(By.tagName("tr"));
    }

    /**
     * 下一页，可能需要重写
     */
    public void nextPage() {
        WebElement el = driver.findElement(By.linkText("上一页"));
        el.click();
    }

    public void close() {
        driver.close();
    }

//    public static void main(String[] args) {
//        List<Integer> k = new ArrayList<>();
//        k.add(1);
//        k.add(2);
//        k.add(3);
//        for(final Integer i:k){
//            System.out.println(i);
//
//        }
//    }

}
