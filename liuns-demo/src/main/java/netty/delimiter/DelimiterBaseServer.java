package netty.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Netty Server
 * <p />
 * DelimiterBasedFrameDecoder + StringDecoder的方式解决TCP粘包/半包的问题 <br />
 */
public class DelimiterBaseServer {

    private static final String delimiter_tag = "@#";

    public static void main(String[] args) {
        int port = 8080;
        // 绑定端口，启动Netty服务
        new DelimiterBaseServer().bind(port);
    }

    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel sc) throws Exception {
                            // 设置DelimiterBasedFrameDecoder处理器
                            ByteBuf delimiter = Unpooled.copiedBuffer(delimiter_tag.getBytes());
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            // 设置StringDecoder处理器
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new DelimiterBaseServerHandler());
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
