package liuns.demo.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOEchoServerHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public NIOEchoServerHandler(int port) {
        try {
            int backLog = 1024;
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), backLog);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();

                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动注册并关闭，不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }

            // 处理数据的读取
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println("body:" + body);

                    doWrite(sc, body);
                }
                // 返回值为-1，链路已经关闭，需要关闭SocketChannel，释放资源
                else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
                // 返回值为0，没有读到字节，正常情况，忽略
                else {

                }
            }

        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            sc.write(buffer);
        }
    }
}
