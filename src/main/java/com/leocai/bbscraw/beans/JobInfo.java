package com.leocai.bbscraw.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */

public class JobInfo implements Serializable {

    private static final long serialVersionUID = 5823691433703420311L;
    private int id;
    private Date   jobDate;
    private String title;
    private String type;
    private String href;
    private int    hot;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
               ", type='" + type + '\'' +
               ", href='" + href + '\'' +
               ", hot=" + hot +
               '}';
    }
}
