package liuns.remoting.framework.loadBalance;

import liuns.remoting.framework.invoker.IRegisterCenter4Invoker;
import liuns.remoting.framework.model.SOARequest;
import liuns.remoting.framework.model.SOAResponse;
import liuns.remoting.framework.model.ProviderService;
import liuns.remoting.framework.RegisterCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 提供服务软负载
 */
public class RevokerProxyBeanFactory implements InvocationHandler {

    private ExecutorService fixedThreadPool = null;

    // 服务接口
    private Class<?> targetInterface;

    // 超时时间
    private int consumeTimeout;

    // 调用者线程数
    private static int threadWorkerNumber = 110;

    // 负载均衡策略
    private String clusterStrategy;

    private static RevokerProxyBeanFactory singleton;

    public RevokerProxyBeanFactory(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        this.targetInterface = targetInterface;
        this.consumeTimeout = consumeTimeout;
        this.clusterStrategy = clusterStrategy;
    }

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        if (singleton == null) {
            synchronized (RevokerProxyBeanFactory.class) {
                if (singleton == null) {
                    singleton = new RevokerProxyBeanFactory(targetInterface, consumeTimeout, clusterStrategy);
                }
            }
        }
        return singleton;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{targetInterface}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 服务接口名称
        String serviceKey = targetInterface.getName();
        // 获取某个接口的服务提供者列表
        IRegisterCenter4Invoker registerCenter4Consumer = RegisterCenter.singleton();
        List<ProviderService> providerServices = registerCenter4Consumer.getServiceMetaDataMap4Consume().get(serviceKey);

        // 根据客户端配置信息选择软负载均衡具体策略
        ClusterStrastegy clusterStrastegyService = ClusterEngine.selectClusterStrategy(clusterStrategy);
        ProviderService providerService = clusterStrastegyService.select(providerServices);

        // 复制一份服务提供者信息
        ProviderService newService = providerService.copy();
        // 设置本次调用服务的方法及接口
        newService.setServiceItf(targetInterface);
        newService.setServiceMethod(method);

        // 声明调用SOARequest对象，SOARequest表示发起一次调用所包含的信息
        final SOARequest request = new SOARequest();
        request.setUniqueKey(UUID.randomUUID().toString() + "-" + Thread.currentThread().getId());
        request.setProviderService(newService);
        request.setInvokeTimeout(consumeTimeout);
        request.setInvokeMethodName(method.getName());
        request.setArgs(args);

        try {
            // 构建用来发起调用的线程池
            if (fixedThreadPool == null) {
                synchronized (RevokerProxyBeanFactory.class) {
                    if (fixedThreadPool == null) {
                        fixedThreadPool = Executors.newFixedThreadPool(threadWorkerNumber);
                    }
                }
            }
            // 根据服务提供者的IP、Port，构建InetSocketAddress对象，标识服务提供者地址
            String serverIp = request.getProviderService().getServerIp();
            int serverPort = request.getProviderService().getServerPort();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIp, serverPort);

            // 提交本次调用信息到线程池fixedThreadPool，发起调用
            Future<SOAResponse> responseFuture = fixedThreadPool.submit(RevokerServiceCallable.of(inetSocketAddress, request));

            // 获取调用返回的结果
            SOAResponse response = responseFuture.get(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
            if (response != null) {
                return response.getResult();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
