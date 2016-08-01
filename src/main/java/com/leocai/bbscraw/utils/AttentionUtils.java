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

    private static final String withOut[] = new String[] { "是不是", "咋", "?", "？", "呢", "为什么", "求", "实习", "请问" };
    private static String attentionCompanys[];

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("attention.properties");
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(in, "UTF-8"));
            String list = (String) properties.get("list");
            String[] l = list.split(",");
            attentionCompanys = l;
        } catch (IOException e) {
            attentionCompanys = new String[] {};
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
