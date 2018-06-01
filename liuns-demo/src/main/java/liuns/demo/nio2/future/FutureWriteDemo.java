package liuns.demo.nio2.future;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * AsynchronousFileChannel Future 异步写文件
 */
public class FutureWriteDemo {

    public static void main(String[] args) throws IOException {

        Path file = Paths.get("/Users/ningdd/Downloads/filesDemo/aa.txt");
        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        // 打开文件通道，获取异步Channel对象
        AsynchronousFileChannel fc = AsynchronousFileChannel.open(file, StandardOpenOption.WRITE);
        // 文件开始读取位置
        int position = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("我敲了半天代码了！".getBytes());
        buffer.flip();
        Future<Integer> operation = fc.write(buffer, position);
        buffer.clear();

        // 等待文件写入操作完毕，关闭文件通道
        while (!operation.isDone()) ;
        fc.close();

        System.out.println("Async Write File donw.");
    }
}