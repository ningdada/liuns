package nio2.future;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Future;

/**
 * AsynchronousFileChannel Future 异步读取文件
 */
public class FutureReadDemo {

    public static void main(String[] args) throws IOException {

        // 打开文件通道，获取异步Channel对象
        AsynchronousFileChannel fc = AsynchronousFileChannel.open(Paths.get("/Users/ningdd/Downloads/filesDemo/aa.txt"), StandardOpenOption.READ);
        // 读取文件临时ByteBuffer对象
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 文件开始读取位置
        long position = 0;
        // 文件读取结果
        List<Byte> totalByteList = Lists.newArrayList();

        while (true) {
            // 使用Future异步读取文件
            Future<Integer> operation = fc.read(buffer, position);
            // do noting 等待读取完成
            while (!operation.isDone());

            buffer.flip();
            // 如果本次没有读到数据，说明已经读取完毕，直接调出循环
            if (!buffer.hasRemaining()) {
                break;
            }

            // 计算下次文件读取的开始位置
            position = position + buffer.limit();
            while (buffer.hasRemaining()) {
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                // 将本次从文件中读取到的数据保存到总的读取结果byte数组totalByteList
                for (byte by : data) {
                    totalByteList.add(by);
                }
            }
            buffer.clear();
        }

        byte[] bytes = new byte[totalByteList.size()];
        for (int i=0; i<totalByteList.size(); i++) {
            bytes[i] = totalByteList.get(i);
        }
        String fileContent = new String(bytes);
        System.out.println(fileContent);
        fc.close();
    }
}
