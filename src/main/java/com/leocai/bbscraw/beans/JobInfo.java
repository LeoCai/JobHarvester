package com.leocai.bbscraw.beans;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by caiqingliang on 2016/7/28.
 */

@Data @ToString
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
    private String contentMD5;

}
