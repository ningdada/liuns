package liuns.remoting.framework.soa;

/**
 * 调用服务
 */
public class InvokerService {

    /**
     * 服务接口
     */
    private Class<?> serviceItf;

    /**
     * 服务提供者唯一标识
     */
    private String remoteAppKey;

    /**
     * 服务分组组名
     */
    private String groupName;

    /**
     * 服务端口
     */
    private int serverPort;

    public Class<?> getServiceItf() {
        return serviceItf;
    }

    public void setServiceItf(Class<?> serviceItf) {
        this.serviceItf = serviceItf;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
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
