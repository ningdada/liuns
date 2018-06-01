package liuns.remoting.framework.loadBalance;

import liuns.remoting.framework.soa.ProviderService;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * 随机
 */
public class RandomClusterStrategyImpl implements ClusterStrastegy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        int MAX_LEN = providerServices.size();
        int index = RandomUtils.nextInt(0, MAX_LEN - 1);
        return providerServices.get(index);
    }
}
