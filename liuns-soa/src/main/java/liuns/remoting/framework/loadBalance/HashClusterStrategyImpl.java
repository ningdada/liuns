package liuns.remoting.framework.loadBalance;

import liuns.remoting.framework.helper.IPHelper;
import liuns.remoting.framework.model.ProviderService;

import java.util.List;

/**
 * 源地址Hash
 */
public class HashClusterStrategyImpl implements ClusterStrastegy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        String localIp = IPHelper.localIp();
        int hashCode = localIp.hashCode();
        int size = providerServices.size();
        return providerServices.get(hashCode % size);
    }
}
