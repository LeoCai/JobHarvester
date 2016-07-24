/**
 * Created by caiqingliang on 2016/7/24.
 */
public class ClawStarter {

    String urls[] = new String[] {
            "https://bbs.sjtu.edu.cn/bbsdoc?board=JobInfo",
            "http://bbs.nju.edu.cn/board?board=JobExpress",
            "http://bbs.cloud.icybee.cn/board/Job"
    };

    public static void main(String args[]) {
        new ShangJiaoClaw("https://bbs.sjtu.edu.cn/bbsdoc?board=JobInfo").start();
    }

}
