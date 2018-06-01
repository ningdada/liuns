package liuns.demo.nio2.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncEchoServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;

    AsynchronousServerSocketChannel ssc;

    public AsyncEchoServerHandler(int port) {
        this.port = port;
        try {
            // 获取AsynchronousServerSocketChannel对象
            ssc = AsynchronousServerSocketChannel.open();
            // 绑定服务端口
            ssc.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept() {
        // 接受客户端连接
        ssc.accept(this, new AcceptCompletionHandler());
    }
}
