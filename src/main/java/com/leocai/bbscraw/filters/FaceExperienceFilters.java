package com.leocai.bbscraw.filters;

import com.leocai.bbscraw.beans.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * Created by caiqingliang on 2016/8/6.
 */
@Component public class FaceExperienceFilters implements AttentionFilters {

    @Autowired private Properties expAttentionProp;
    private            String[]   attentionCompanys;
    private            String[]   withOut;

    @PostConstruct public void init() {
        String list = ((String) expAttentionProp.get("list")).trim();
        attentionCompanys = list.split(",");
        withOut = ((String) expAttentionProp.get("without")).trim().split(",");
    }

    @Override public boolean isAttention(String content) {
        if(content.contains("百度")) return true;
        else return false;
//        if (content.length() > 300) return false;
//        for (String at : attentionCompanys) {
//            if (content.contains(at)) return true;
//        }
//        return false;
    }

    @Override public boolean isIgnored(String text) {
        for (String wt : withOut) {
            if (text.contains(wt)) return true;
        }
        return false;
    }

    @Override public boolean filted(JobInfo infoDTO) {
        return infoDTO.getHot() < 100;
    }
}
