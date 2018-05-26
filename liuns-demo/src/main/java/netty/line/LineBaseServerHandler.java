package netty.line;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LineBaseServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String tag = "\n";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String content) throws Exception {

        // 接收客户端发送的数据
        System.out.println("received from client:" + content);

        // 将数据回写给客户端
        content += tag;
        ByteBuf buf = Unpooled.copiedBuffer(content.getBytes());
        ctx.writeAndFlush(buf);
    }
}
