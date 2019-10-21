import javax.swing.tree.TreeNode;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

public class TestMap {
    public static void main(String[] args) {
        int cap=6;
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n);

        int a=10;
        int b=3;
        int c=a&b;
        System.out.println(c);
        float DEFAULT_LOAD_FACTOR = 0.75f;
        int DEFAULT_INITIAL_CAPACITY = 1 << 4;
        int newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        System.out.println(newThr);


    }




}
