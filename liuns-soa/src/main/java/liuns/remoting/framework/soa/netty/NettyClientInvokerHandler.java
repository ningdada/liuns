package liuns.remoting.framework.soa.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import liuns.remoting.framework.soa.RevokerResponseHolder;
import liuns.remoting.framework.soa.SOAResponse;

public class NettyClientInvokerHandler extends SimpleChannelInboundHandler<SOAResponse> {


    public NettyClientInvokerHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SOAResponse response) throws Exception {
        // 将Netty异步返回的结果存入阻塞队列，以便调用端同步获取
        RevokerResponseHolder.putResultValue(response);
    }
}
