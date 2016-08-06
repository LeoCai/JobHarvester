package com.leocai.bbscraw.comparators;

import com.leocai.bbscraw.beans.JobInfo;

/**
 * Created by caiqingliang on 2016/8/6.
 */
public class HotComparator implements java.util.Comparator<JobInfo> {

    @Override public int compare(JobInfo o1, JobInfo o2) {
        return o2.getHot() - o1.getHot();
    }
}
