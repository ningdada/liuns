package demo.netty.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedLengthServerHandler extends SimpleChannelInboundHandler<String> {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String content) throws Exception {

        // 接收客户端的数据
        System.out.println("received from client:" + content + " counter:" + counter.addAndGet(1));

        // 将数据回写给客户端
        ByteBuf buf = Unpooled.copiedBuffer(content.getBytes());
        ctx.writeAndFlush(buf);
    }
}
