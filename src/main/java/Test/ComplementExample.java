package Test;

public class ComplementExample {
    public static void main(String[] args) {
        int number = 2;

        // 只保留低 8 位
        int mask8Bit = 0xFF; // 8位掩码
        int complement8 = number & mask8Bit;

        // 显示 8 位补码表示
        String binaryString = Integer.toBinaryString(complement8);
        System.out.println("原数: " + number);
        System.out.println("8位补码表示: " + binaryString);
    }
}
