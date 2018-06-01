package liuns.demo.netty.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty Client
 */
public class DelimiterBaseClient {

    // 字符串分隔符
    private static final String delimiter_tag = "@#";

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        new DelimiterBaseClient().connetion(host, port);

    }

    public void connetion(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel sc) throws Exception {
                            // 设置DelimiterDecoder处理器
                            ByteBuf delimiter = Unpooled.copiedBuffer(delimiter_tag.getBytes());
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            // 设置StringDecoder处理器
                            sc.pipeline().addLast(new StringDecoder());
                            // 配置客户端处理网络I/O事件的类
                            sc.pipeline().addLast(new DelimiterBaseClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture cf = b.connect(host, port).sync();
            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < 1000; i++) {
                String content = "我就再试试？看看数据怎么样了！" + counter.addAndGet(1) + delimiter_tag;
                byte[] req = content.getBytes();
                ByteBuf buf = Unpooled.buffer(req.length);
                buf.writeBytes(req);

                // 向服务端发送数据
                ChannelFuture channelFuture = cf.channel().writeAndFlush(buf);
                channelFuture.syncUninterruptibly();
            }
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程池
            group.shutdownGracefully();
        }

    }
}
