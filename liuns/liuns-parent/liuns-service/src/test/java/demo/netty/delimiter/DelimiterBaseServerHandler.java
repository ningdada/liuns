package demo.netty.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class DelimiterBaseServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String delimiter_tag = "@#";

    // 计数器
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String content) throws Exception {

        // 接收客户端的字符串
//        System.out.println("received from client:" + content + "counter:" + counter.addAndGet(1));
        System.out.println("received from client:" + content);

        // 加入分隔符，将数据返回给客户端
        content += delimiter_tag;
        ByteBuf buf = Unpooled.copiedBuffer(content.getBytes());
        ctx.writeAndFlush(buf);
    }
}
