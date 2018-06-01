package liuns.demo.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * SocketChannel ServerSocketChannel 阻塞式网络通信
 */
public class EchoClient {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap("呵呵哒，我就是宁大大！".getBytes());

        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        try {
            // 创建客户端SocketChannel
            SocketChannel socketChannel = SocketChannel.open();
            // 如果客户端SocketChannel创建成功
            if (socketChannel.isOpen()) {
                // 设置为阻塞模式
                socketChannel.configureBlocking(true);
                // 设置网络参数
                socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);
                // 连接服务器
                socketChannel.connect(new InetSocketAddress("127.0.0.1", 8085));
                // 连接成功
                if (socketChannel.isConnected()) {
                    // 向服务端发送数据
                    socketChannel.write(buffer);
                    // 创建接受服务端返回的数据ByteBuffer
                    ByteBuffer buffer1 = ByteBuffer.allocateDirect(1024);
                    while (socketChannel.read(buffer1) != -1) {
                        buffer1.flip();
                        charBuffer = decoder.decode(buffer1);
                        System.out.println(charBuffer.toString());
                        if (buffer1.hasRemaining()) {
                            buffer1.compact();
                        } else {
                            buffer1.clear();
                        }
                    }
                } else {
                    throw new RuntimeException("socket channel cannot be opened!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}
