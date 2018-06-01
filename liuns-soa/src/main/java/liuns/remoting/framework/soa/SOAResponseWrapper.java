package liuns.remoting.framework.soa;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Netty异步调用返回结果包装类
 */
public class SOAResponseWrapper {

    // 存储返回结果的阻塞队列
    private BlockingQueue<SOAResponse> reponseQueue = new ArrayBlockingQueue<>(1);

    // 结果返回时间
    private long responseTime;

    /**
     * 计算该返回结果是否以过期
     *
     * @return
     */
    public boolean isExpire() {
        SOAResponse response = reponseQueue.peek();
        if (response == null) {
            return false;
        }

        long timeout = response.getInvokeTimeout();
        if ((System.currentTimeMillis() - responseTime) > timeout) {
            return true;
        }
        return false;
    }


    public static SOAResponseWrapper of() {
        return new SOAResponseWrapper();
    }

    public BlockingQueue<SOAResponse> getReponseQueue() {
        return reponseQueue;
    }

    public void setReponseQueue(BlockingQueue<SOAResponse> reponseQueue) {
        this.reponseQueue = reponseQueue;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
