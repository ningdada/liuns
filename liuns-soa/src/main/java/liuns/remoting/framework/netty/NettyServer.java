package liuns.remoting.framework.netty;

import io.netty.channel.ChannelFuture;
import liuns.remoting.framework.helper.PropertyConfigHelper;
import liuns.remoting.framework.serializer.SerializeType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import liuns.remoting.framework.model.SOAResponse;

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
    private SerializeType serializeType = PropertyConfigHelper.getSerializeType();

    public void start(final int port) {
        synchronized (NettyServer.class) {
            if (bossGroup != null || workerGroup != null) {
                return ;
            }

            bossGroup = new NioEventLoopGroup(); // 用于服务器端接受客户端的连接
            workerGroup = new NioEventLoopGroup(); // 用于网络事件的处理
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024) // 用来初始化服务端可连接队列大小
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

            try {
                b.bind(port).sync().channel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public NettyServer() {

    }

    public static NettyServer singletion() {
        return nettyServer;
    }
}
