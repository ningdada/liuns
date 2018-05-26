package demo.nio2.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 服务端业务逻辑代码
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel sc;

    public ReadCompletionHandler(AsynchronousSocketChannel sc) {
        if (this.sc == null) {
            this.sc = sc;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        // 获取客户端传入的数据
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);

        try {
            // 将客户端的数据打印在控制台
            String req = new String(body, "utf-8");
            System.out.println("echo content:"+req);
            // 将接受的数据传给客户端
            doWrite(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void doWrite(String currentTime) {
        if (currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = currentTime.getBytes();
            final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();

            sc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 如果没有发送完成，继续发送
                    if (attachment.hasRemaining()) {
                        sc.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
//        exc.printStackTrace();
        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
