package liuns.remoting.framework.soa;

/**
 * SOA响应包装类
 */
public class SOAResponse {

    /**
     * 调用超时时间设置
     */
    private long invokeTimeout;

    /**
     * 本次调用唯一标识
     */
    private String uniqueKey;

    /**
     * 响应数据
     */
    private Object result;

    public long getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
