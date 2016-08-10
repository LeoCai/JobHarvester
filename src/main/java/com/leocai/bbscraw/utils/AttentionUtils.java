package com.leocai.bbscraw.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by caiqingliang on 2016/7/30.
 * 关注过滤工具类
 */
public class AttentionUtils {

    /**
     * 过滤的字符串
     */
    private static String withOut[];
    /**
     * 关注的公司
     */
    private static String attentionCompanys[];

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "config/attention/job-attention.properties");
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(in, "UTF-8"));
            String list = ((String) properties.get("list")).trim();
            attentionCompanys = list.split(",");
            withOut = ((String) properties.get("without")).trim().split(",");
        } catch (IOException e) {
            attentionCompanys = new String[] {};
            withOut = new String[] {};
            e.printStackTrace();
        }
    }

    /**
     * 是否为关注的代码
     *
     * @param content
     * @return
     */
    public static boolean isAttentioned(String content) {
        if (content.length() > 300) return false;
        for (String at : attentionCompanys) {
            if (content.contains(at)) return true;
        }
        return false;
    }

    /**
     * 过滤不想要的//TODO带抽取
     *
     * @param text
     * @return
     */
    public static boolean isWithOut(String text) {
        for (String wt : withOut) {
            if (text.contains(wt)) return true;
        }
        return false;
    }

    //TODO 此处需要优化
    public static String findComany(String title) {
        for (String comany : attentionCompanys) {
            if (title.contains(comany)) return comany;
        }
        return "其它";
    }
}
