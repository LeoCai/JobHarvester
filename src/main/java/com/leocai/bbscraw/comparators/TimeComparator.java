package com.leocai.bbscraw.comparators;

import com.leocai.bbscraw.beans.JobInfo;

import java.util.Comparator;

/**
 * Created by caiqingliang on 2016/8/6.
 */
public class TimeComparator implements Comparator<JobInfo> {

    @Override public int compare(JobInfo o1, JobInfo o2) {
        long time1 = o1.getJobDate().getTime();
        long time2 = o2.getJobDate().getTime();
        if (time1 > time2) return -1;
        if (time1 < time2) return 1;
        return 0;
    }
}
