package demo.io.readerWriter;

import java.io.*;

/**
 * BufferedReader   BufferedWriter
 * <p />
 * 适合用来处理字符
 */
public class BufferedRWDemo {

    public static void main(String[] args) throws IOException{

        FileReader reader = new FileReader("/Users/ningdd/Downloads/operate.txt");
        FileWriter writer = new FileWriter("/Users/ningdd/Downloads/operateDemo.txt");

        // 将字符输入/输出流用BufferedReader/BufferedWriter包装起来
        BufferedReader bufferedReader = new BufferedReader(reader);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        // 利用BufferedReader/BufferedWriter实现逐行读写，提高I/O性能
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();

    }
}
