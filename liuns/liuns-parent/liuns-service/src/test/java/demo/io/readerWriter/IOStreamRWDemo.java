package demo.io.readerWriter;

import java.io.*;

/**
 * InputStreamReader   OutputStreamWriter
 * <p />
 * 适合用来处理字符
 */
public class IOStreamRWDemo {

    public static void main(String[] args) throws IOException {

        // 创建文件字节输入流
        FileInputStream in = new FileInputStream("/Users/ningdd/Downloads/operate.txt");

        // 利用桥梁InputStreamReader将文件字节输入流FileInputStream转换成字符输入流
        InputStreamReader reader = new InputStreamReader(in, "utf-8");

        // 利用BufferedReader包装字符输入流InputStreamReader，提高性能
        BufferedReader bufferedReader = new BufferedReader(reader);

        // 创建文件字节输出流
        FileOutputStream out = new FileOutputStream("/Users/ningdd/Downloads/operateDemo.txt");

        // 利用桥梁OutputStreamWriter将文件字节输出流FileOutputStream转换成字符输出流
        OutputStreamWriter writer = new OutputStreamWriter(out);

        // 利用BufferedWriter包装字符输出流OutputStreamWriter，提高性能
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        // 写入文件
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();

        bufferedReader.close();
        bufferedWriter.close();
        in.close();
        out.close();

    }
}
