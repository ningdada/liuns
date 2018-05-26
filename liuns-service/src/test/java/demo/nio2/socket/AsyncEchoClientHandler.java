package demo.nio2.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * AIO 客户端业务代码
 */
public class AsyncEchoClientHandler implements CompletionHandler<Void, AsyncEchoClientHandler>, Runnable {

    private AsynchronousSocketChannel client;

    private String host;

    private int port;

    private CountDownLatch latch;

    public AsyncEchoClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
            client.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void completed(Void result, AsyncEchoClientHandler attachment) {
        // 准备写入服务端的数据
        byte[] req = "哦哦，你好哇！".getBytes();
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(req);
        buffer.flip();

        // 将数据写入服务端
        client.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // 客户端数据持续写入服务端
                if (attachment.hasRemaining()) {
                    client.write(buffer, buffer, this);
                } else {
                    // 客户端数据写入完成后读取从服务端传回的数据
                    ByteBuffer readBuf = ByteBuffer.allocate(1024);
                    client.read(readBuf, readBuf, new CompletionHandler<Integer, ByteBuffer>() {

                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            // 服务端数据成功返回处理逻辑
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);

                            String body;
                            try {
                                // 将服务端返回的数据打印在控制台
                                body = new String(bytes, "utf-8");
                                System.out.println(body);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            // 服务端返回出错处理逻辑
                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    client.close();
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncEchoClientHandler attachment) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

}
