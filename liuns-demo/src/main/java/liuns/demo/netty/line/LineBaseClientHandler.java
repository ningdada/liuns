package liuns.demo.netty.line;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LineBaseClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String content) throws Exception {

        // 获取服务端数据
        System.out.println("received from server:" + content);
    }
}
