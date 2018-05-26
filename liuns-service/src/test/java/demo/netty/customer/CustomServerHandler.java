package demo.netty.customer;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import model.business.po.UserPO;

public class CustomServerHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 接收客户端发来的消息
        UserPO user = (UserPO) msg;
        System.out.println("received from client:" + JSON.toJSONString(user));

        // 消息回写给客户端
        ctx.writeAndFlush(user);
    }

}
