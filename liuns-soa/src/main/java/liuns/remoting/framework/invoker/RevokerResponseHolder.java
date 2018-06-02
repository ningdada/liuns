package liuns.remoting.framework.invoker;

import com.google.common.collect.Maps;
import liuns.remoting.framework.model.SOAResponse;
import liuns.remoting.framework.model.SOAResponseWrapper;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RevokerResponseHolder {

    // 服务返回结果Map
    private static final Map<String, SOAResponseWrapper> responseMap = Maps.newConcurrentMap();

    // 清除过期的返回结果
    private static final ExecutorService removeExpireKeyExecutor = Executors.newSingleThreadExecutor();

    static {
        removeExpireKeyExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Map.Entry<String, SOAResponseWrapper> entry : responseMap.entrySet()) {
                            boolean isExpire = entry.getValue().isExpire();
                            if (isExpire) {
                                responseMap.remove(entry.getKey());
                            }
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 初始化返回结果容器，requestUniqueKey唯一标识本次调用
     *
     * @param requestUniqueKey
     */
    public static void initResponseData(String requestUniqueKey) {
        responseMap.put(requestUniqueKey, SOAResponseWrapper.of());
    }

    /**
     * 将Netty调用异步返回结果放入阻塞队列
     *
     * @param response
     */
    public static void putResultValue(SOAResponse response) {
        long currentTime = System.currentTimeMillis();
        SOAResponseWrapper responseWrapper = responseMap.get(response.getUniqueKey());
        responseWrapper.setResponseTime(currentTime);
        responseWrapper.getReponseQueue().add(response);
        responseMap.put(response.getUniqueKey(), responseWrapper);
    }

    /**
     * 从阻塞队列中获取Netty异步返回的结果值
     *
     * @param requestUniqueKey
     * @param timeout
     * @return
     */
    public static SOAResponse getValue(String requestUniqueKey, long timeout) {
        SOAResponseWrapper responseWrapper = responseMap.get(requestUniqueKey);

        try {
            return responseWrapper.getReponseQueue().poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseMap.remove(requestUniqueKey);
        }
    }
}
