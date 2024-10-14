package Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteArrayInputStreamExample {
    public static void main(String[] args) {
        // 创建一个字节数组
        byte[] byteArray = "Hello, World!".getBytes();
        for (byte b : byteArray){
            System.out.print(b+" ");
        }
        System.out.println();
        // 用字节数组创建一个ByteArrayInputStream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        byte[] readByte = new byte[10];
        long skip = byteArrayInputStream.skip(2);
        if(skip == 2){

        }
        int read = byteArrayInputStream.read(readByte, 0, 10);
        for (byte b : readByte){
           System.out.print(b+" ");
       }
        System.out.println();
        int data;
        // 逐字节读取数据
        while ((data = byteArrayInputStream.read()) != -1) {
            // 将读取到的字节转换为字符并打印
            System.out.print((char) data);
        }

        // 关闭流（实际上 ByteArrayInputStream 不需要关闭，但可以这样做）
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
