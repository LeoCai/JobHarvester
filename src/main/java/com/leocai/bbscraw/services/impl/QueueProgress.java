package com.leocai.bbscraw.services.impl;

import lombok.Data;
import lombok.ToString;

/**
 * Created by caiqingliang on 2016/8/24.
 * 信息队列进度
 */
@Data public class QueueProgress {

    private int queueSize;
    private int totalInfoProduced;
    private int totalInfoConsumed;

    public QueueProgress(int queueSize, int totalInfoProduced, int totalInfoConsumed) {
        this.queueSize = queueSize;
        this.totalInfoProduced = totalInfoProduced;
        this.totalInfoConsumed = totalInfoConsumed;
    }

    @Override public String toString() {
        return "QueueProgress:\t" +
               "queueSize=" + queueSize +
               "\t totalInfoProduced=" + totalInfoProduced +
               "\t totalInfoConsumed=" + totalInfoConsumed;
    }
}
