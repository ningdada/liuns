package demo.io.readerWriter;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * CharArrayReader   CharArrayWriter
 * <p />
 * 适合用来处理字符
 */
public class CharArrayRWDemo {

    public static void main(String[] args) throws IOException {

        String content = "我知道的，就是你！";

        // 将字符数据转换成字符输入流
        CharArrayReader charReader = new CharArrayReader(content.toCharArray());

        // 将字符输入流数据写入字符输出流
        char[] chars = new char[1024];
        int size = 0;
        CharArrayWriter charWriter = new CharArrayWriter();
        while ((size = charReader.read(chars)) != -1) {
            charWriter.write(chars, 0, size);
        }

        System.out.println(charWriter.toString());

        char[] charArray = charWriter.toCharArray();
        for (char c : charArray) {
            System.out.println(c);
        }
    }
}
