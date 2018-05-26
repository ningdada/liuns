package nio2.completion;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * AsynchronousFileChannel Completion 异步读取文件
 */
public class CompletionReadDemo {

    public static void main(String[] args) throws IOException, InterruptedException {

        // 打开文件通道，获取异步Channel对象
        AsynchronousFileChannel ch = AsynchronousFileChannel.open(Paths.get("/Users/ningdd/Downloads/filesDemo/aa.txt"), StandardOpenOption.READ);

        // 读取文件临时ByteBuffer对象
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        long position = 0;
        final List<Integer> byteReadResultHolder = Lists.newArrayList();

        // 文件读取结果内容
        List<Byte> totalByteList = Lists.newArrayList();
        while (true) {
            // 每次开始读之前，先重置读取结果计数器
            byteReadResultHolder.clear();

            final CountDownLatch latch = new CountDownLatch(1);
            ch.read(buffer, position, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    // 保存本次读取的结果，如result=-1，则代表数据已经读完
                    byteReadResultHolder.add(result);
                    latch.countDown();
                }
                @Override
                public void failed(Throwable exc, Void attachment) {
                    // 读取文件失败，打印失败结果
                    System.out.println("read failure:" + exc.toString());
                    latch.countDown();
                }
            });

            //等待异步文件读取操作完成
            latch.await();

            // 如果读取完毕，则直接退出
            if (byteReadResultHolder.size() <= 0 || (byteReadResultHolder.size() >= 0 && byteReadResultHolder.get(0) == -1)) {
                break;
            }

            // 读取本次从文件读取到的Buffer里面的参数
            buffer.flip();
            // 计算下次文件读取的开始位置
            position = position + buffer.limit();
            while (buffer.hasRemaining()) {
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                // 将本次从文件里面读取到的数据保存到总的读取结果byte数组totalByteList中
                for (byte by : data) {
                    totalByteList.add(by);
                }
            }
            // 重置临时读取结果ByteBuffer对象，以便下次循环使用
            buffer.clear();
        }

        // 将读取结果byte数组totalByteList转换成字符串打印出来
        byte[] bytes = new byte[totalByteList.size()];
        for (int i=0; i<totalByteList.size(); i++) {
            bytes[i] = totalByteList.get(i);
        }
        String fileContent = new String(bytes);
        System.out.println("fileContent："+fileContent);

    }
}
