import redis.clients.jedis.Jedis;

/**
 * Created by caiqingliang on 2016/7/28.
 */
public class RedisTest {

    public static void main(String args[]){

        Jedis jedis = new Jedis("172.24.36.12",6379);
        System.out.println("Running" + jedis.ping());

    }

}
