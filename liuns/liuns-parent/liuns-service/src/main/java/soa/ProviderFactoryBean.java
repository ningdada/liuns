package soa;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 远程服务发布
 * <p />
 * 1. 启动Netty服务端 <br />
 * 2. 启动ZK服务端，将服务提供者属性信息注册到服务注册中心
 */
public class ProviderFactoryBean implements FactoryBean, InitializingBean {

    /**
     * 服务接口
     */
    private Class<?> serviceItf;

    /**
     * 服务实现
     */
    private Object serviceObject;

    /**
     * 服务端口
     */
    private String serverPort;

    /**
     * 服务超时时间
     */
    private long timeout;

    /**
     * 服务代理对象，暂时没用到
     */
    private Object serviceProxyObject;

    /**
     * 服务提供者唯一标识
     */
    private String appKey;

    /**
     * 服务分组组名
     */
    private String groupName;

    /**
     * 服务提供者权重，默认为1，范围为【1-100】
     */
    private int weight = 1;

    /**
     * 服务端线程数，默认10个线程，限制服务端该服务运行线程数，用于实现资源的隔离与服务端限流
     */
    private int workerThreads = 10;

    public Object getObject() throws Exception {
        return serviceProxyObject;
    }

    public Class<?> getObjectType() {
        return serviceItf;
    }

    /**
     * Spring Bean初始化的时候会自动执行一次
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        // 启动Netty服务端
        NettyServer.singletion().start(Integer.parseInt(serverPort));

        // 注册到zookeeper，元数据注册中心
        List<ProviderService> providerServiceList = buildProviderServiceInfos();
        IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
        registerCenter4Provider.registerProvider(providerServiceList);
    }

    private List<ProviderService> buildProviderServiceInfos() {
        List<ProviderService> providerList = Lists.newArrayList();
        Method[] methods = serviceObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ProviderService providerService = new ProviderService();
            providerService.setServiceItf(serviceItf);
            providerService.setServiceObject(serviceObject);
            providerService.setServerIp(IPHelper.localIp());
            providerService.setServerPort(Integer.parseInt(serverPort));
            providerService.setTimeout(timeout);
            providerService.setAppKey(appKey);
            providerService.setGroupName(groupName);
            providerService.setServiceMethod(method);
            providerService.setWeight(weight);
            providerService.setWorkerThreads(workerThreads);
            providerList.add(providerService);
        }
        return providerList;
    }

    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Object getServiceProxyObject() {
        return serviceProxyObject;
    }

    public void setServiceProxyObject(Object serviceProxyObject) {
        this.serviceProxyObject = serviceProxyObject;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
