import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.concurrent.TimeUnit;

public class TestTX {
    public static void main(String[] args) {
        boolean b = tranMethod();
        System.out.println(b);

    }

    private static boolean tranMethod() {
        Jedis jedis=new Jedis("localhost",6379);

        int balance;
        int debt;
        int amtToSubtract=10;//实际刷的金额

        jedis.watch("balance");

//        jedis.set("balance","155");//模拟出事，事物中断
        try{ TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){ e.printStackTrace();}
        balance=Integer.parseInt(jedis.get("balance"));
        if(balance<amtToSubtract){
            jedis.unwatch();
            System.out.println("balance is not enough!");
            return false;
        }else {
            System.out.println("***********************transaction");
            //watch中间被修改会取消失误
            Transaction multi = jedis.multi();
            multi.decrBy("balance",amtToSubtract);
            multi.incrBy("debt",amtToSubtract);
            multi.exec();
            balance=Integer.parseInt(jedis.get("balance"));
            debt=Integer.parseInt(jedis.get("debt"));

            System.out.println(balance);
            System.out.println(debt);
        }

        return true;
    }
}
