package liuns.demo.netty.fixed;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class FixedLengthClient {

    /**
     * 19是"我就用7个字符"这几个字符串的字节数组长度
     */
    private static final int fix_length = 19;

    public static void main(String[] args) {
        new FixedLengthClient().connection("127.0.0.1", 8080);
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
                            sc.pipeline().addLast(new FixedLengthFrameDecoder(fix_length));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new FixLengthClientHandler());
                        }
                    });

            ChannelFuture cf = b.connect(host, port).sync();
            // 向服务端发送数据
            for (int i = 0; i < 1000; i++) {
                String content = "我就用7个字符";
                byte[] req = content.getBytes();
                ByteBuf buffer = Unpooled.buffer(req.length);
                buffer.writeBytes(req);
                ChannelFuture channelFuture = cf.channel().writeAndFlush(buffer);
                channelFuture.syncUninterruptibly();
            }
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
