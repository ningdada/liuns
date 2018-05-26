package demo.netty;

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
 * 客户端多次循环发送消息，你会发现结果和预期的不一样，结果杂乱无章
 * <p />
 * 原因：造成这样的结果就是TCP传输过程中出现的粘包/半包的问题的导致的
 * <p />
 * 解决方案，可利用一下几种工具栏组合解决（仅只用于字符串类型）：
 * 1. DelimiterBasedFrameDecoder + StringDecoder 利用特殊分隔符作为消息的结束标识
 * 2. LineBasedFrameDecoder + StringDecoder 已换行符作为消息的结束标识
 * 3. FixedLengthFrameDecoder + StringDecoder 按照固定长度获取消息
 */
public class EchoClients {


    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 8080;
        new EchoClients().connection(host, port);
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

            // 注意：这儿和EchoClient不一样，这儿是多次循环调用
            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            for (int i =0; i < 1000; i++) {
                // 构造客户端发送的数据ByteBuf对象
                byte[] req = "你好哇，有点热闹哇！".getBytes();
                ByteBuf msgBuf = Unpooled.buffer(req.length);
                msgBuf.writeBytes(req);

                // 向服务端发送数据
                ChannelFuture cf = f.channel().writeAndFlush(msgBuf);
                cf.syncUninterruptibly();
            }

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
