package liuns.remoting.framework.loadBalance;

import com.google.common.collect.Lists;
import liuns.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 加权轮询
 */
public class WeightPollingClusterStrastegyImpl implements ClusterStrastegy {

    // 计数器
    private int index = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService select(List<ProviderService> providerServices) {

        ProviderService service = null;
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            List<ProviderService> services = Lists.newArrayList();
            for (ProviderService provider : providerServices) {
                int weight = provider.getWeight();
                for (int i = 0; i < weight; i++) {
                    services.add(provider);
                }
            }
            if (index > services.size()) {
                index = 0;
            }
            service = services.get(index);
            index++;
            return service;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
