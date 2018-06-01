package liuns.demo.netty.line;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty Client
 *
 */
public class LineBaseClient {

    private static final String tag = "\n";

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        new LineBaseClient().connection(host, port);
    }

    public void connection(String host, int port) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new LineBaseClientHandler());
                        }
                    });

            ChannelFuture cf = b.connect(host, port).sync();

            // 向客户端发送数据
            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < 1000; i++) {
                String content = "我就再试试咯！" + counter.addAndGet(1) + tag;
                byte[] req = content.getBytes();
                ByteBuf buffer = Unpooled.buffer(req.length);
                buffer.writeBytes(req);
                cf.channel().writeAndFlush(buffer);
                cf.syncUninterruptibly();
            }
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
