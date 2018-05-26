package nio2.completion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

/**
 * AsynchronousFileChannel Completion 异步写入文件
 */
public class CompletionWriteDemo {

    public static void main(String[] args) throws IOException, InterruptedException {

        Path path = Paths.get("/Users/ningdd/Downloads/filesDemo/aa.txt");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        // 获得异步写入文件的fileChannel
        AsynchronousFileChannel ch = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        // 指定文件写入的位置
        long position = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("你好哇！哈哈哈！".getBytes());
        buffer.flip();

        // 异步写入操作
        final CountDownLatch latch = new CountDownLatch(1);
        ch.write(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // 写入操作成功完成
                latch.countDown();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                // 写入操作出错
                latch.countDown();
                exc.printStackTrace();
            }
        });

        // 等待异步写入完成
        latch.await();
        System.out.println("Async Write File done.");

    }
}
