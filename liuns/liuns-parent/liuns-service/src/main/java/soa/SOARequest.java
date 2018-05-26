package soa;

/**
 * SOA请求包装类
 */
public class SOARequest {

    /**
     * 服务提供者信息
     */
    private ProviderService providerService;

    /**
     * 调用超时时间设置
     */
    private long invokeTimeout;

    /**
     * 调用方法名
     */
    private String invokeMethodName;

    /**
     * 请求参数
     */
    private Object[] args;

    /**
     * 本次调用唯一标识
     */
    private String uniqueKey;

    public ProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public String getInvokeMethodName() {
        return invokeMethodName;
    }

    public void setInvokeMethodName(String invokeMethodName) {
        this.invokeMethodName = invokeMethodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}
