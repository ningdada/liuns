package demo.nio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannel ServerSocketChannel 阻塞式网络通信
 */
public class EchoHandler implements Runnable {

    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

    public EchoHandler (SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            // 读取客户端传输的数据，并原样写入返回给客户端
            while (socketChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                if (byteBuffer.hasRemaining()) {
                    byteBuffer.compact();
                } else {
                    byteBuffer.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
