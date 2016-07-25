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

public class ClawerStarter {

    HashMap<String, MyCrawer> map = new HashMap<String, MyCrawer>();

    /**
     * 读取学校链接匹配地址，命名规范[School]+[Crawer]
     */
    public ClawerStarter() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("school.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            try {
                Class<MyCrawer> crawer = (Class<MyCrawer>) getClass().getClassLoader().loadClass("" + key + "Crawer");
                MyCrawer c = crawer.getConstructor(String.class).newInstance(properties.getProperty((String) key));
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

        //        map.put("NJU", new NJUCrawer());
        //        map.put("ZJU", new ZJUCrawer());
        //        map.put("SJU", new SJUCrawer());
        //        map.put("NYU", new NYUCrawer());
    }

    public void start() {
        Set<String> set = map.keySet();
        for (String key : set) {
            MyCrawer crawer = map.get(key);
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
                    return map.get(key).start();
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
            FileWriter fileWriter = new FileWriter("E:/learn/cql/jobInfo.html");
            fileWriter.write("<html >\n" + "<head>\n"
                             + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
                             + "</head>\n" + "<body>\n" + "    <div>\n" + "        <ul>\n");
            fileWriter.write(s);
            fileWriter.write("</ul>\n" + "\n" + "</div>\n" + "</body>\n" + "\n" + "</html>");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        long start = System.nanoTime();
        //        new ClawerStarter().start();
        new ClawerStarter().asynStart();

        System.out.println((System.nanoTime() - start) * 1.0 / 1000000000);

    }

}
