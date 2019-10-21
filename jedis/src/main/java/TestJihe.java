import java.util.*;

public class TestJihe {
    public static void main(String[] args) {
//        List<String> l1=new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//                    new Thread(()->{
//                       l1.add("1");
////                        System.out.println(l1);
//                    },String.valueOf(i)).start();
//                }
//        System.out.println(l1);
        HashMap<String,String> m1=new HashMap<>();
        for (int i = 0; i < 100; i++) {
                    new Thread(()->{
                        m1.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,8));

                    },String.valueOf(i)).start();
                }
        System.out.println(m1);

    }
}
