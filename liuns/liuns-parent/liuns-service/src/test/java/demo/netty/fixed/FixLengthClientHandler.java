package demo.netty.fixed;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class FixLengthClientHandler extends SimpleChannelInboundHandler<String> {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String content) throws Exception {

        // 接收服务端发送的数据
        System.out.println("received from server:" + content + " counter:" + counter.addAndGet(1));

    }
}
