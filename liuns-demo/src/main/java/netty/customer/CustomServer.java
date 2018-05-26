package netty.customer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 自定义编解码器来解决粘包/半包的问题
 * <p />
 * 原理是使用int数据类型来记录整个消息的字节数组长度,将该int数据作为消息的消息头一起传输,
 * 在服务端接收消息的时候,先接收4个字节的int类型数据,这个数据即为整个消息字节数组的长度,
 * 在接收剩余字节,直到接收的字节数组长度等于最先接收的int数据类型数据大小
 *
 */
public class CustomServer {

    public static void main(String[] args) {
        new CustomServer().bind(8080);
    }

    public void bind(int port) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler((LogLevel.INFO)))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 解决粘包/半包，根据消息长度自动拆包
                            sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0,2,0,2));
                            // 配置自定义序列化解码工具
                            sc.pipeline().addLast(new CustomV1Decoder());
                            // 解决粘包/半包问题附加消息长度在消息头
                            sc.pipeline().addLast(new LengthFieldPrepender(2));
                            // 配置自定义序列化编码工具
                            sc.pipeline().addLast(new CustomV1Encoder());
                            sc.pipeline().addLast(new CustomServerHandler());
                        }
                    });
            ChannelFuture cf = b.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
