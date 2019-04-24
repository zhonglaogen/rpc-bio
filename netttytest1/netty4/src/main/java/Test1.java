public class Test1 {
    public static void main(String[] args) {
        String a="abcdefghbij";
        String substring = a.substring(3,a.length()-1);
        String[] split=a.split("de");
        System.out.println(split[0]+"\t"+split[1]);
        System.out.println(a.indexOf("b"));
        System.out.println(substring);
    }
}
