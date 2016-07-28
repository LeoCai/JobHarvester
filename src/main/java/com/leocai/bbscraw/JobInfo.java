package com.leocai.bbscraw;

import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */
public class JobInfo {

    private int id;
    private Date time;
    private String title;
    private String type;
    private String href;
    private int hot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
