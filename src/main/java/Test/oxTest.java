package Test;

public class oxTest {
    public static void main(String[] args) {
        String str = "0x11";
        String str2 = str.substring(2);
        System.out.println(str.substring(2, str.length()));
        int i = Integer.parseInt(str2, 16);
        System.out.println(i);
    }
}
