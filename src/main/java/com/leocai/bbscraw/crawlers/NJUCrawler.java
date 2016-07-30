package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.utils.JobDateParser;
import com.leocai.bbscraw.utils.JobDateUtils;
import com.leocai.bbscraw.utils.JobInfoExtractUtils;
import com.leocai.bbscraw.beans.JobInfoIndex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class NJUCrawler extends MyCrawler {

    public NJUCrawler(String url) {
        super(url);
        setSource("南大bbs");
    }

    protected JobInfo getInfoDTO(WebElement we) {
        JobInfoIndex jobInfoIndex = new JobInfoIndex();
        jobInfoIndex.setHotIndex(6);
        jobInfoIndex.setHrefIdnex(1);
        jobInfoIndex.setTimeIndex(4);
        jobInfoIndex.setTitleIndex(5);
        JobDateParser jobDateParser = new JobDateParser() {

            public Date parse(String dateStr) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);
                Date date = new Date();
                String yearStr = JobDateUtils.getYearStr();
                try {
                    date = sdf.parse(yearStr + " " + dateStr);
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
                return date;
            }
        };
        return JobInfoExtractUtils.simpleExtract(we, jobInfoIndex, jobDateParser);
    }

}
