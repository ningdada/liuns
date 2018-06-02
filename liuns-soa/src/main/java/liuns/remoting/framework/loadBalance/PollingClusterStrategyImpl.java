package liuns.remoting.framework.loadBalance;

import liuns.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 轮询
 */
public class PollingClusterStrategyImpl implements ClusterStrastegy {

    // 计数器
    private int index = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        ProviderService service = null;
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            if (index >= providerServices.size()) {
                index = 0;
            }
            service = providerServices.get(index);
            index++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return service;
    }
}
