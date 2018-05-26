package io.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileInputStream   FileOutputStream
 * <p />
 * 适合用来处理字节流
 */
public class FileIOStreamDemo {

    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;

        File srcFile = new File("/Users/ningdd/Downloads/operate.txt");
        File targetFile = new File("/Users/ningdd/Downloads/operateDemo.txt");

        // 实例化文件输入流与文件输出流
        in = new FileInputStream(srcFile);
        out = new FileOutputStream(targetFile);

        int byt;
        while ((byt = in.read()) != -1) {
            out.write(byt);
        }

        in.close();
        out.close();
    }
}
