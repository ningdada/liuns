package liuns.remoting.framework.soa;

import java.lang.reflect.Method;

public class ProviderService {

    // 服务接口
    private Class<?> serviceItf;

    // 服务ip
    private String serverIp;

    // 服务端口
    private int serverPort;

    // 服务超时时间
    private Long timeout;

    // 服务提供者唯一标识
    private String appKey;

    // 服务分组组名
    private String groupName;

    // 服务实现
    private Object serviceObject;

    // 服务方法
    private Method serviceMethod;

    // 服务提供者权重
    private Integer weight;

    // 服务端线程数
    private Integer workerThreads;

    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public Method getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(Integer workerThreads) {
        this.workerThreads = workerThreads;
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

    public ProviderService copy() {
        ProviderService target = new ProviderService();
        target.setServerIp(this.getServerIp());
        target.setServerPort(this.getServerPort());
        target.setServiceItf(this.getServiceItf());
        target.setServiceMethod(this.getServiceMethod());
        target.setAppKey(this.getAppKey());
        target.setGroupName(this.getGroupName());
        target.setTimeout(this.getTimeout());
        target.setWeight(this.getWeight());
        target.setWorkerThreads(this.getWorkerThreads());
        target.setServiceObject(this.getServiceObject());
        return target;
    }
}
