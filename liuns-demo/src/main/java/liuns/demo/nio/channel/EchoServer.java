package liuns.demo.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SocketChannel ServerSocketChannel 阻塞式网络通信
 */
public class EchoServer {


    // 执行服务端业务逻辑线程池
    private static final ExecutorService executor = Executors.newCachedThreadPool();


    public static void main(String[] args) throws IOException {

        // 新建服务端ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        if (serverSocketChannel.isOpen()) {

            // 设置为阻塞模式
            serverSocketChannel.configureBlocking(true);
            // 设置网络传输参数
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            // 绑定服务端Channel端口与本地IP
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8085));
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                executor.submit(new EchoHandler(socketChannel));
            }
        }
    }
}
