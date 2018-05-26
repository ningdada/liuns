package demo.io.stream;

import java.io.*;

/**
 * BufferedInputStream   BufferedFileOutputStream
 * <p />
 * 可提高IO性能
 * <p />
 * 适合用来处理字节流
 */
public class BufferedIOStreamDemo {

    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedInputStream bufferedIn = null;
        BufferedOutputStream bufferedOut = null;

        File srcFile = new File("/Users/ningdd/Downloads/operate.txt");
        File targetFile = new File("/Users/ningdd/Downloads/operateDemo.txt");

        // 实例化文件输入流和文件输出流
        in = new FileInputStream(srcFile);
        bufferedIn = new BufferedInputStream(in);

        out = new FileOutputStream(targetFile);
        bufferedOut = new BufferedOutputStream(out);

        byte[] buffer = new byte[1024];
        int byt;
        while ((byt = bufferedIn.read(buffer, 0, buffer.length)) != -1) {
            bufferedOut.write(buffer, 0, byt);
        }
        bufferedOut.flush();

        in.close();
        out.close();

    }
}
