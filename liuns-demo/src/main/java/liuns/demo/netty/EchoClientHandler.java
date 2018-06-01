package liuns.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 客户端业务处理逻辑
 */
public class EchoClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        // 获取服务端返回的数据buf
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        // 打印数据
        String body = new String(req, CharsetUtil.UTF_8);
        System.out.println("receive data from server:"+body);
    }
}
