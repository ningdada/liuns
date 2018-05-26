package soa.netty;

import business.serializer.SerializeType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soa.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class NettyChannelPoolFactory {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelPoolFactory.class);

    private static final NettyChannelPoolFactory nettyChannelPoolFactory = new NettyChannelPoolFactory();

    // Key为服务提供者地址，value为Netty Channel阻塞队列
    private static final Map<InetSocketAddress, ArrayBlockingQueue<Channel>> channelPoolMap = Maps.newConcurrentMap();

    // 初始化Netty Channel阻塞队列的长度
    private static final int channelConnetSize = PropertyConfigHelper.getChannelConnectSize();

    // 初始化序列化协议类型
    private static final SerializeType serializeType = PropertyConfigHelper.getSerializeType();

    // 服务提供者列表
    private List<ProviderService> serviceMetaDataList = Lists.newArrayList();

    private NettyChannelPoolFactory() {

    }

    public static NettyChannelPoolFactory channelPoolFactoryInstance() {
        return nettyChannelPoolFactory;
    }

    /**
     * 初始化Netty Channel连接队列
     *
     * @param providerMap
     */
    public void initChannelPoolFactory(Map<String, List<ProviderService>> providerMap) {
        // 将服务提供者信息存入serviceMetaDataList列表中
        Collection<List<ProviderService>> collectionServiceMetaDataList = providerMap.values();
        for (List<ProviderService> serviceMetaDataModels : collectionServiceMetaDataList) {
            if (CollectionUtils.isEmpty(serviceMetaDataModels)) {
                continue;
            }
            serviceMetaDataList.addAll(serviceMetaDataModels);
        }

        // 获取服务提供者地址列表
        Set<InetSocketAddress> socketAddressSet = Sets.newHashSet();
        for (ProviderService serviceMetaData : serviceMetaDataList) {
            String serverIp = serviceMetaData.getServerIp();
            int serverPort = serviceMetaData.getServerPort();
            InetSocketAddress socketAddress = new InetSocketAddress(serverIp, serverPort);
            socketAddressSet.add(socketAddress);
        }

        // 根据服务提供者列表地址初始化Channel阻塞队列，并以地址为Key，地址对应的Channel阻塞队列为value，存入channelPoolMap
        for (InetSocketAddress socketAddress : socketAddressSet) {
            try {
                int realChannelConnectSize = 0;
                while (realChannelConnectSize < channelConnetSize) {
                    Channel channel = null;
                    while (channel == null) {
                        // 若channel不存在，则注册新的Netty Channel
                        channel = registerChannel(socketAddress);
                    }
                    // 计数器，初始化的时候存入阻塞队列的Netty Channel个数不超过channelConnectSize
                    realChannelConnectSize++;

                    // 将新注册的Netty channelArrayBlockingQueue
                    // 并将阻塞队列channelArrayBlockingQueue作为value存入channelPoolMap
                    ArrayBlockingQueue<Channel> channelArrayBlockingQueue = channelPoolMap.get(socketAddress);
                    if (channelArrayBlockingQueue == null) {
                        channelArrayBlockingQueue = new ArrayBlockingQueue<Channel>(channelConnetSize);
                        channelPoolMap.put(socketAddress, channelArrayBlockingQueue);
                    }
                    // TODO 下面这行代码是否需要放在if里面？
                    channelArrayBlockingQueue.offer(channel);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 根据服务提供者地址获取对应的Netty Channel阻塞队列
     *
     * @param socketAddress
     * @return
     */
    public ArrayBlockingQueue<Channel> acquire(InetSocketAddress socketAddress) {
        return channelPoolMap.get(socketAddress);
    }

    /**
     * channel使用完毕之后，回收到阻塞队列arrayBlockingQueue
     *
     * @param arrayBlockingQueue
     * @param channel
     * @param socketAddress
     */
    public void release(ArrayBlockingQueue<Channel> arrayBlockingQueue, Channel channel, InetSocketAddress socketAddress) {
        if (arrayBlockingQueue == null) {
            return;
        }

        // 回收之前检查channel是否可用，不可用的话，重新注册一个，放入阻塞队列
        if (channel == null || !channel.isActive() || !channel.isOpen() || !channel.isWritable()) {
            if (channel != null) {
                channel.deregister().syncUninterruptibly().awaitUninterruptibly();
                channel.closeFuture().syncUninterruptibly().awaitUninterruptibly();
            }
            Channel newChannel = null;
            while (newChannel == null) {
                log.debug("-------------------register new Channel -----------------");
                newChannel = registerChannel(socketAddress);
            }
            arrayBlockingQueue.offer(newChannel);
            return;
        }
        arrayBlockingQueue.offer(channel);
    }

    /**
     * 为服务提供者地址socketAddress注册新的channel
     *
     * @param socketAddress
     * @return
     */
    public Channel registerChannel(SocketAddress socketAddress) {
        try {
            EventLoopGroup group = new NioEventLoopGroup(10);
            Bootstrap b = new Bootstrap();
            b.remoteAddress(socketAddress);

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            // 注册Netty编码器
                            sc.pipeline().addLast(new NettyEncoderHandler(serializeType));
                            // 注册Netty解码器
                            sc.pipeline().addLast(new NettyDecoderHandler(SOAResponse.class, serializeType));
                            // 注册客户端业务逻辑处理handler
                            sc.pipeline().addLast(new NettyServerInvokeHandler());
                        }
                    });

            ChannelFuture cf = b.connect().sync();
            final Channel newChannel = cf.channel();
            final CountDownLatch latch = new CountDownLatch(1);
            final List<Boolean> isSuccessHolder = Lists.newArrayListWithCapacity(1);
            // 监听Channel是否建立成功
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    // 若Channel建立成功，保存建立成功的标记
                    if (future.isSuccess()) {
                        isSuccessHolder.add(Boolean.TRUE);
                    } else {
                        // 若Channel建立失败，保存建立失败的标记
                        future.cause().printStackTrace();;
                        isSuccessHolder.add(Boolean.FALSE);
                    }
                    latch.countDown();
                }
            });

            latch.await();
            // 如果Channel建立成功，返回新建的Channel
            if (isSuccessHolder.get(0)) {
                return newChannel;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
