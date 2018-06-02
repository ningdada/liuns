package liuns.remoting.framework.netty;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import liuns.remoting.framework.RegisterCenter;
import liuns.remoting.framework.model.SOARequest;
import liuns.remoting.framework.model.SOAResponse;
import liuns.remoting.framework.provider.IRegisterCenter4Provider;
import liuns.remoting.framework.model.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class NettyServerInvokeHandler extends SimpleChannelInboundHandler<SOARequest> {

    private static final Logger log = LoggerFactory.getLogger(NettyServerInvokeHandler.class);

    // 服务端限流
    private static final Map<String, Semaphore> serviceKeySermaphoreMap = Maps.newConcurrentMap();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SOARequest req) throws Exception {
        if (ctx.channel().isWritable()) {
            // 从服务调用对象里获取服务提供者信息
            ProviderService metaDataModel = req.getProviderService();
            long consumeTimeout = req.getInvokeTimeout();
            final String methodName = req.getInvokeMethodName();

            // 根据方法名称定位到具体某一个服务提供者
            String serviceKey = metaDataModel.getServiceItf().getName();
            // 获取限流工具
            int workerThread = metaDataModel.getWorkerThreads();
            Semaphore semaphore = serviceKeySermaphoreMap.get(serviceKey);
            // 初始化流控基础设施semaphore
            if (semaphore == null) {
                synchronized (serviceKeySermaphoreMap) {
                    semaphore = serviceKeySermaphoreMap.get(serviceKey);
                    if (semaphore == null) {
                        semaphore = new Semaphore(workerThread);
                        serviceKeySermaphoreMap.put(serviceKey, semaphore);
                    }
                }
            }

            // 获取服务注册中心
            IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
            List<ProviderService> localProviderCaches = registerCenter4Provider.getProviderServiceMap().get(serviceKey);
            ProviderService localProviderCache = Collections2.filter(localProviderCaches, new Predicate<ProviderService>() {
                @Override
                public boolean apply(ProviderService input) {
                    return StringUtils.equals(input.getServiceMethod().getName(), methodName);
                }

                @Override
                public boolean test(ProviderService input) {
                    return apply(input);
                }
            }).iterator().next();
            Object serviceObject = localProviderCache.getServiceObject();

            // 利用反射发起服务调用
            Method method = localProviderCache.getServiceMethod();
            Object result = null;
            boolean acquire = false;
            try {
                // 利用semaphore限流
                acquire = semaphore.tryAcquire(consumeTimeout, TimeUnit.MILLISECONDS);
                if (acquire) {
                    // 利用反射发起服务调用
                    result = method.invoke(serviceObject, req.getArgs());
                }
            } catch (InterruptedException e) {
                result = e;
            } finally {
                if (acquire) {
                    semaphore.release();
                }
            }

            SOAResponse resp = new SOAResponse();
            resp.setInvokeTimeout(consumeTimeout);
            resp.setUniqueKey(req.getUniqueKey());
            resp.setResult(result);

            // 响应客户端
            ctx.writeAndFlush(resp);
        } else {
            log.error("--------------------- channel close! ---------------------");
        }
    }
}
