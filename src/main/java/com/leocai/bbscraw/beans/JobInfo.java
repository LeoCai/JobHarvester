package com.leocai.bbscraw.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */

public class JobInfo implements Serializable {

    private static final long serialVersionUID = 5823691433703420311L;

    /**
     * 数据库生成id
     */
    private int     id;
    /**
     * job日期
     */
    private Date    jobDate;
    /**
     * 标题
     */
    private String  title;
    /**
     * 公司
     */
    private String  company;
    /**
     * 链接
     */
    private String  href;
    /**
     * 热度
     */
    private int     hot;
    /**
     * 信息来源
     */
    private String  source;
    /**
     * 是否已读
     */
    private boolean isRead;//TODO 暂时未做

    /**
     * 根据href和title的md5码
     */
    //TODO md5暂时未做
    private String contentMD5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getJobDate() {
        return jobDate;
    }

    public void setJobDate(Date jobDate) {
        this.jobDate = jobDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    @Override public String toString() {
        return "JobInfo{" +
               "id=" + id +
               ", jobDate=" + jobDate +
               ", title='" + title + '\'' +
               ", company='" + company + '\'' +
               ", href='" + href + '\'' +
               ", hot=" + hot +
               '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
