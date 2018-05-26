package demo.netty.customer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class CustomClient {

    public static void main(String[] args) {
        new CustomClient().connetion("127.0.0.1", 8080);
    }

    public void connetion(String host, int port) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 解决粘包/半包，根据消息长度自动拆包
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0,2,0,2));
                            // 配置自定义序列化解码工具
                            sc.pipeline().addLast(new CustomV1Decoder());
                            // 解决粘包/半包问题附加消息长度在消息头部
                            sc.pipeline().addLast(new LengthFieldPrepender(2));
                            // 配置自定义序列化编码工具
                            sc.pipeline().addLast(new CustomV1Encoder());
                            sc.pipeline().addLast(new CustomClientHandler());
                        }
                    });

            ChannelFuture cf = b.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
