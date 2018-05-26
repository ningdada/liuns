package soa;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soa.netty.NettyChannelPoolFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RevokerServiceCallable implements Callable<SOAResponse> {

    private static final Logger log = LoggerFactory.getLogger(RevokerServiceCallable.class);

    private Channel channel;

    private InetSocketAddress inetSocketAddress;

    private SOARequest request;

    public static RevokerServiceCallable of(InetSocketAddress inetSocketAddress, SOARequest request) {
        return new RevokerServiceCallable(inetSocketAddress, request);
    }

    public RevokerServiceCallable(InetSocketAddress inetSocketAddress, SOARequest request) {
        this.inetSocketAddress = inetSocketAddress;
        this.request = request;
    }

    @Override
    public SOAResponse call() throws Exception {
        // 初始化返回结果容器，将本次调用的唯一标识作为key存入返回结果的Map
        RevokerResponseHolder.initResponseData(request.getUniqueKey());
        // 根据本地调用服务提供者地址获取对应的Netty通道channel队列
        ArrayBlockingQueue<Channel> blockingQueue = NettyChannelPoolFactory.channelPoolFactoryInstance().acquire(inetSocketAddress);

        try {
            if (channel == null) {
                // 从队列中获取本次调用的Netty通道channel
                channel = blockingQueue.poll(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
            }

            // 若获取的channel通道已经不可用，则重新获取一个
            while (!channel.isOpen() || !channel.isActive() || !channel.isWritable()) {
                log.warn("------------ retry get new Channel -------------");
                channel = blockingQueue.poll(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
                if (channel == null) {
                    channel = NettyChannelPoolFactory.channelPoolFactoryInstance().registerChannel(inetSocketAddress);
                }
            }

            // 将本次调用的信息写入Netty通道，发起异步调用
            ChannelFuture cf = channel.writeAndFlush(request);
            cf.syncUninterruptibly();

            // 从返回结果容器中获取结果，同时设置等待超时时间为invokerTimeout
            long invokeTimeout = request.getInvokeTimeout();
            return RevokerResponseHolder.getValue(request.getUniqueKey(), invokeTimeout);
        } catch (InterruptedException e) {
            log.error("service invoker error.", e);
        } finally {
            // 本次调用完毕后，将Netty的通道channel重新释放到队列中，以便下次调用复用
            NettyChannelPoolFactory.channelPoolFactoryInstance().release(blockingQueue, channel, inetSocketAddress);
        }
        return null;
    }
}
