package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty Client
 */
public class EchoClient {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 8080;
        new EchoClient().connection(host, port);
    }

    public void connection(String host, int port) {
        // 创建客户端处理I/O读写的NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建客户端辅助启动类
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 设置NioSocketChannel，对应于JDK NIO类SocketChannel类
                    .channel(NioSocketChannel.class)
                    // 设置TCP参数TCP_NODELAY
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        protected void initChannel(NioSocketChannel sc) throws Exception {
                            // 配置客户端处理网络I/O事件的类
                            sc.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 构造客户端发送的数据ByteBuf对象
            byte[] req = "你好哇，有点热闹哇！".getBytes();
            ByteBuf msgBuf = Unpooled.buffer(req.length);
            msgBuf.writeBytes(req);

            // 向服务端发送数据
            ChannelFuture cf = f.channel().writeAndFlush(msgBuf);
            cf.syncUninterruptibly();

            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放NIO线程池资源
            group.shutdownGracefully();
        }

    }
}
