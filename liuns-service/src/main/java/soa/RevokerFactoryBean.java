package soa;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import soa.netty.NettyChannelPoolFactory;

import java.util.List;
import java.util.Map;

/**
 * 远程服务的引入
 * <p />
 * 1. 通过注册中心,将服务提供者信息获取到本地缓存列表 <br />
 * 2. 初始化Netty连接池 <br />
 * 3. 获取服务提供者代理对象 <br />
 * 4. 将服务消费者信息注册到注册中心 <br />
 */
public class RevokerFactoryBean implements FactoryBean, InitializingBean {

    /**
     * 服务接口
     */
    private Class<?> tartgetInterface;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 服务实现
     */
    private Object serviceObject;

    /**
     * 负载均衡策略
     */
    private String clusterStrategy;

    /**
     * 服务提供者唯一标识
     */
    private String remoteAppKey;

    /**
     * 服务分组组名
     */
    private String groupName;

    public Object getObject() throws Exception {
        return serviceObject;
    }

    public Class<?> getObjectType() {
        return tartgetInterface;
    }

    /**
     * Spring Bean初始化的时候会自动执行一次
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        // 获取服务注册中心
        IRegisterCenter4Invoker registerCenter4Consumer = RegisterCenter.singleton();
        // 初始化服务提供者列表到本地缓存
//        registerCenter4Consumer.initProviderMap(remoteAppKey, groupName);
        registerCenter4Consumer.initProviderMap();

        // 初始化Netty Channel
        Map<String, List<ProviderService>> providerMap = registerCenter4Consumer.getServiceMetaDataMap4Consume();
        if (MapUtils.isEmpty(providerMap)) {
            throw new RuntimeException("service provider list is empty.");
        }

        // 初始化Netty Channel连接队列
        NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPoolFactory(providerMap);

        // 获取服务提供者代理对象
        RevokerProxyBeanFactory proxyFactory = RevokerProxyBeanFactory.singleton(tartgetInterface, timeout, clusterStrategy);
        this.serviceObject = proxyFactory.getProxy();

        // 将消费者信息注册到注册中心
        InvokerService invoker = new InvokerService();
        invoker.setServiceItf(tartgetInterface);
        invoker.setRemoteAppKey(remoteAppKey);
        invoker.setGroupName(groupName);
        registerCenter4Consumer.registerInvoker(invoker);

    }

    public Class<?> getTartgetInterface() {
        return tartgetInterface;
    }

    public void setTartgetInterface(Class<?> tartgetInterface) {
        this.tartgetInterface = tartgetInterface;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(String clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public String getRemoteAppKey() {
        return remoteAppKey;
    }

    public void setRemoteAppKey(String remoteAppKey) {
        this.remoteAppKey = remoteAppKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
