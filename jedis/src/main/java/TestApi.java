import redis.clients.jedis.Jedis;

import java.util.Set;

public class TestApi {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost",6379);
        jedis.set("k1","v1");
        jedis.set("k2","v2");
        jedis.set("k3","v3");

        System.out.println(jedis.get("k1"));

        Set<String> keys = jedis.keys("*");
        for(String key:keys){
            System.out.println(jedis.get(key));
        }
    }
}
