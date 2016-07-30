package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by caiqingliang on 2016/7/30.
 */
public class HtmlUtils {

    public static String getRows(List<JobInfo> jobInfoList) {
        StringBuffer sb = new StringBuffer();
        for (JobInfo jb : jobInfoList) {
            sb.append(getTableRow(jb));
        }
        return sb.toString();
    }

    //TODO设计待优化，需要改多个地方
    public static String getTableHead() {
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append(getTag("th", "公司"));
        sb.append(getTag("th", "标题"));
        sb.append(getTag("th", "热度"));
        sb.append(getTag("th", "日期"));
        sb.append(getTag("th", "来源"));
        sb.append("</tr>\n");
        return sb.toString();
    }

    public static String getTableRow(JobInfo infoDTO) {
        StringBuffer tableRow = new StringBuffer("");
        tableRow.append("<tr>");
        tableRow.append(getTag("td", infoDTO.getCompany()));
        tableRow.append(getTdWithHref(infoDTO.getTitle(), infoDTO.getHref()));
        tableRow.append(getTag("td", infoDTO.getHot() + ""));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableRow.append(getTag("td", sd.format(infoDTO.getJobDate())));
        tableRow.append(getTag("td", infoDTO.getSource()));
        tableRow.append("</tr>\n");
        return tableRow.toString();
    }

    public static String getTag(String tag, String title) {
        return "<" + tag + ">" + title + "</" + tag + ">";
    }

    public static String getTdWithHref(String info, String href) {
        return "<td><a href='" + href + "'>" + info + "</a></td>";
    }

    public static String getRealComanyListInfo(List<String> avalibaleComanys) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select>");
        for (String company : avalibaleComanys) {
            sb.append(getOption(company, ""));
        }
        sb.append("</select>\n");
        return sb.toString();
    }

    //TODO 加入value
    private static String getOption(String company, String value) {
        return "<option value='" + value + "'/>" + company + "</option>";
    }
}
