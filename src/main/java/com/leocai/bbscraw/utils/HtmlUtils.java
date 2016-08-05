package com.leocai.bbscraw.utils;

import com.leocai.bbscraw.beans.JobInfo;
import com.leocai.bbscraw.services.JobInfoService;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by caiqingliang on 2016/7/30.
 * HTML生成器
 */
public class HtmlUtils {
    //TODO 高亮

    public static String getRows(List<JobInfo> jobInfoList) {
        StringBuffer sb = new StringBuffer();
        for (JobInfo jb : jobInfoList) {
            sb.append(getTableRow(jb));
        }
        return sb.toString();
    }

    //TODO 设计待优化，需要改多个地方
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

    public static String getRealComanyListInfo(Set<String> avalibaleComanys) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select>");
        for (String company : avalibaleComanys) {
            sb.append(getOption(company, company));
        }
        sb.append("</select>\n");
        return sb.toString();
    }

    //TODO 加入value
    private static String getOption(String company, String value) {
        return "<option value='" + value + "'/>" + company + "</option>";
    }

    //TODO 待重构
    public static void writeHtml(List<JobInfo> jobInfoList, JobInfoService jobInfoService) {
        writeRs(HtmlUtils.getRows(jobInfoList), jobInfoService);
    }

    private static void writeRs(String content, JobInfoService jobInfoService) {
        try {
            FileWriter fileWriter = new FileWriter("./jobInfo.html");
            fileWriter.write("<html >\n" + "<head>\n"
                             + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
                             + "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">"
                             + "<script type=\"text/javascript\" src=\"jquery-3.1.0.min.js\"></script>\n"
                             + "<script type=\"text/javascript\" src=\"core.js\"></script>\n"
                             + "</head>\n" + "<body>\n"
                             + "    <div>\n" + "        <table>\n");
            fileWriter.write(HtmlUtils.getRealComanyListInfo(jobInfoService.getAvalibaleComanys()));
            fileWriter.write(HtmlUtils.getTableHead());
            fileWriter.write(content);
            fileWriter.write("</table>\n" + "\n" + "</div>\n" + "</body>\n" + "\n" + "</html>");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
