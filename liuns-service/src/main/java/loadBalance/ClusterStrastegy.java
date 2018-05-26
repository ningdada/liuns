package loadBalance;

import soa.ProviderService;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface ClusterStrastegy {

    public ProviderService select(List<ProviderService> providerServices);
}
