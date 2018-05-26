package netty.delimiter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class DelimiterBaseClientHandler extends SimpleChannelInboundHandler<String> {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String content) throws Exception {
//        System.out.println("received from server:" + content + " counter:" + counter.addAndGet(1));
        System.out.println("received from server:" + content);
    }
}
