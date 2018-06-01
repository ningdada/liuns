package liuns.demo.nio2.socket;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 服务端接入客户端请求代码
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncEchoServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncEchoServerHandler attachment) {
        // 循环接入客户端连接
        attachment.ssc.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncEchoServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
