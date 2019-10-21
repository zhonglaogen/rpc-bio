import redis.clients.jedis.Jedis;

public class TestMs {
    public static void main(String[] args) {
        Jedis jedis79=new Jedis("localhost",6379);
        Jedis jedis80=new Jedis("localhost",6380);

        jedis80.slaveof("localhost",6379);

        jedis79.set("aaaaa","2222222as222");
        //第一次会获得为空，在执行一次就好了
        String aClass = jedis80.get("aaaaa");
        System.out.println(aClass);
    }
}
