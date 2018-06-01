package liuns.demo.netty.customer;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import liuns.model.business.po.UserPO;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomClientHandler extends ChannelInboundHandlerAdapter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    // channel建立之后，向服务端发送消息
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      for (int i = 0; i < 1000; i++) {
          UserPO user = new UserPO();
          user.setPwd("ped");
          user.setId(Long.valueOf(counter.addAndGet(1)));
          user.setUsername("nnd");
          ctx.writeAndFlush(user);
      }
    }

    // 读取服务端发送的消息
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserPO user = (UserPO) msg;
        System.out.println("received from server:" + JSON.toJSONString(user));
    }

}
