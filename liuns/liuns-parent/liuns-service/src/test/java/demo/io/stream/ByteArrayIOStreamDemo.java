package demo.io.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * ByteArrayInputStream   ByteArrayOutputStream
 * <p />
 * 适合用来处理字节流
 */
public class ByteArrayIOStreamDemo {

    public static void main(String[] args) throws IOException {
        String content = "我就是宁大大！";
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));

        // 将字节数组转化成字节输入流
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        byte[] buffer = new byte[1024];
        int size = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((size = in.read(buffer)) != -1) {
            out.write(buffer, 0, size);
        }

        // 将字节输出流转化成字符串
        System.out.println(out.toString());
    }
}
