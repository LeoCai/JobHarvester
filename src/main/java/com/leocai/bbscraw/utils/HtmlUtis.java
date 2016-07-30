package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/30.
 */
public class HtmlUtis {

    public static String getRows(List<JobInfo> jobInfoList) {
        StringBuffer sb = new StringBuffer();
        for (JobInfo jb : jobInfoList) {
            sb.append(getTableRow(jb));
        }
        return sb.toString();
    }

    public static String getTableRow(JobInfo infoDTO) {
        StringBuffer tableRow = new StringBuffer("");
        tableRow.append("<tr>");
        tableRow.append(getTdWithHref(infoDTO.getTitle(), infoDTO.getHref()));
        tableRow.append(getTag("td", infoDTO.getHot() + ""));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableRow.append(getTag("td", sd.format(infoDTO.getJobDate())));
        tableRow.append("</tr>\n");
        return tableRow.toString();
    }

    public static String getTag(String tag, String title) {
        return "<" + tag + ">" + title + "</" + tag + ">";
    }

    public static String getTdWithHref(String info, String href) {
        return "<td><a href='" + href + "'>" + info + "</a></td>";
    }
}
