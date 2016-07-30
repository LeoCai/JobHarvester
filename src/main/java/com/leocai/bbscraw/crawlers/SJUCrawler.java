package com.leocai.bbscraw.crawlers;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.utils.JobDateParser;
import com.leocai.bbscraw.utils.JobDateUtils;
import com.leocai.bbscraw.utils.JobInfoExtractUtils;
import com.leocai.bbscraw.beans.JobInfoIndex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.logging.impl.Jdk14Logger;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by caiqingliang on 2016/7/24.
 */
public class SJUCrawler extends MyCrawler {

    public SJUCrawler(String url) {
        super(url);
        setSource("上交bbs");
    }

    @Override protected JobInfo getInfoDTO(WebElement we) {
        JobInfoIndex jobInfoIndex = new JobInfoIndex();
        jobInfoIndex.setHrefIdnex(1);
        jobInfoIndex.setTimeIndex(3);
        jobInfoIndex.setTitleIndex(4);
        JobDateParser jobDateParser = new JobDateParser() {

            public Date parse(String dateStr) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm", Locale.ENGLISH);
                Date date = new Date();
                if(org.apache.commons.lang3.StringUtils.isEmpty(dateStr)) return date;
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
