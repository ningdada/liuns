package soa.netty;

import business.serializer.SerializeType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import soa.SOAResponse;

/**
 * Netty Server
 */
public class NettyServer {

    private static NettyServer nettyServer = new NettyServer();

    // 服务端boss线程组
    private EventLoopGroup bossGroup;

    // 服务端worker线程组
    private EventLoopGroup workerGroup;

    // 序列化类型配置信息
    private SerializeType serializeType;

    public void start(final int port) {
        if (bossGroup != null || workerGroup != null) {
            return ;
        }

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) {
                            // 注册解码器
                            sc.pipeline().addLast(new NettyDecoderHandler(SOAResponse.class, serializeType));
                            // 注册编码器
                            sc.pipeline().addLast(new NettyEncoderHandler(serializeType));
                            // 服务端业务处理器
                            sc.pipeline().addLast(new NettyServerInvokeHandler());
                        }
                    });

            b.bind(port).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public NettyServer() {

    }

    public static NettyServer singletion() {
        return nettyServer;
    }
}
