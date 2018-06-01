package liuns.demo.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * FileChannel transferTo
 * <p />
 * 不能切换到非阻塞模式，所以不能与Selector一起使用
 */
public class FileChannelDemo {

    public static void main(String[] args) throws IOException {

        FileInputStream in = new FileInputStream("/Users/ningdd/Downloads/operate.txt");
        // 从输入流中获取源文件的通道
        FileChannel inChannel = in.getChannel();

        FileOutputStream out = new FileOutputStream("/Users/ningdd/Downloads/operateDemo.txt");
        // 从输出流中获取目标文件的通道
        FileChannel outChannel = out.getChannel();

        // 使用transferTo()将源文件内容写入目标文件
        inChannel.transferTo(0, inChannel.size(), outChannel);

        in.close();
        out.close();
        inChannel.close();
        outChannel.close();

    }
}
