package com.leocai.bbscraw.filters;

import com.leocai.bbscraw.beans.JobInfo;

/**
 * Created by caiqingliang on 2016/8/6.
 */
public interface AttentionFilters {

    boolean isAttention(String text);

    boolean isIgnored(String text);

    boolean filted(JobInfo infoDTO);
}
