package demo.netty.fixed;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * FixedLengthFrameDecoder + StringDecoder 方式解决TCP粘包/半包的问题。
 * <p />
 * 但是感觉固定传输数据大小的方式不实用啊
 */
public class FixedLengthServer {

    /**
     * 19是"我就用7个字符"这几个字符串的字节数组长度
     */
    private static final int fix_length = 19;

    public static void main(String[] args) {
        int port = 8080;
        new FixedLengthServer().bind(port);
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
                            sc.pipeline().addLast(new FixedLengthFrameDecoder(fix_length));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new FixedLengthServerHandler());
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
