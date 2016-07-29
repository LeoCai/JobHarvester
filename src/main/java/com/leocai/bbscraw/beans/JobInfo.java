package com.leocai.bbscraw.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */

public class JobInfo implements Serializable {

    private static final long serialVersionUID = 5823691433703420311L;

    private int     id;
    private Date    jobDate;
    private String  title;
    private String  company;
    private String  href;
    private int     hot;
    private String  source;
    private boolean isRead;

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

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getHot() {
        return hot;
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
