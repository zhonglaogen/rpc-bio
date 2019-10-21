import java.util.concurrent.TimeUnit;

public class Test {
    static  boolean flag=true;
    public static void main(String[] args) {
        new Thread(()->{
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){ e.printStackTrace();}
            flag=false;
        },"").start();
        while (flag){
            //sout可以代替volitile
            System.out.println("1");
        }
        


    }
}
