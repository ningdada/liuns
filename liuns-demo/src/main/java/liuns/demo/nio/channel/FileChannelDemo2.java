package liuns.demo.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel read write
 */
public class FileChannelDemo2 {

    public static void main(String[] args) throws IOException {

        FileInputStream in = new FileInputStream("/Users/ningdd/Downloads/operate.txt");
        // 从输入流中获取源文件的通道
        FileChannel inChannel = in.getChannel();

        FileOutputStream out = new FileOutputStream("/Users/ningdd/Downloads/operateDemo.txt");
        // 从输出流中获取目标文件的通道
        FileChannel outChannel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = inChannel.read(buffer);
        // 一次读取不完，就需要循环读取
        while (bytesRead != -1) {
            // 翻转Buffer，为下面的读取做准备
            buffer.flip();
            while (buffer.hasRemaining()) {
                outChannel.write(buffer);
            }
            //  复位Buffer，以便再次复用Buffer
            buffer.clear();
            bytesRead = inChannel.read(buffer);
        }

        in.close();
        out.close();
        inChannel.close();
        outChannel.close();
    }
}
