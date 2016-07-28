package com.leocai.bbscraw;

import com.leocai.bbscraw.crawlers.MyCrawler;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by caiqingliang on 2016/7/24.
 * 并行
 * StringBuffer
 */

public class CrawlerStarter {

    private static final String PATH = "com/leocai/bbscraw/crawlers/";

    HashMap<String, MyCrawler> map = new HashMap<String, MyCrawler>();

    /**
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    public CrawlerStarter() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            properties.load(classLoader.getResourceAsStream("school.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            try {
                Class<MyCrawler> crawer = (Class<MyCrawler>) classLoader.loadClass("com.leocai.bbscraw.crawlers."+key+"Crawler");
                MyCrawler c = crawer.getConstructor(String.class).newInstance(properties.getProperty((String) key));
                map.put((String) key, c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            properties.get(key);
        }

        //        map.put("NJU", new com.leocai.bbscraw.crawlers.NJUCrawler());
        //        map.put("ZJU", new com.leocai.bbscraw.crawlers.ZJUCrawler());
        //        map.put("SJU", new com.leocai.bbscraw.crawlers.SJUCrawler());
        //        map.put("NYU", new com.leocai.bbscraw.crawlers.NYUCrawler());
    }

    public void start() {
        Set<String> set = map.keySet();
        for (String key : set) {
            MyCrawler crawer = map.get(key);
            crawer.start();
        }
    }

    public void asynStart() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<String> set = map.keySet();
        List<Future<String>> fts = new ArrayList<Future<String>>(5);
        for (final String key : set) {
            Future<String> ft = executorService.submit(new Callable<String>() {

                public String call() throws Exception {
                    String rs = map.get(key).start();
                    map.get(key).close();
                    return rs;
                }
            });
            fts.add(ft);
        }
        StringBuilder sb = new StringBuilder("");
        for (Future<String> f : fts) {
            try {
                String content = f.get();
                sb.append(content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        writeRs(sb.toString());

    }

    private void writeRs(String s) {
        try {
            FileWriter fileWriter = new FileWriter("./jobInfo.html");
            fileWriter.write("<html >\n" + "<head>\n"
                             + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
                             + "</head>\n" + "<body>\n" + "    <div>\n" + "        <table>\n");
            fileWriter.write(s);
            fileWriter.write("</table>\n" + "\n" + "</div>\n" + "</body>\n" + "\n" + "</html>");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        long start = System.nanoTime();
        //        new com.leocai.bbscraw.CrawlerStarter().start();
        new CrawlerStarter().asynStart();

        System.out.println((System.nanoTime() - start) * 1.0 / 1000000000);

    }

}
