package liuns.remoting.framework.loadBalance;

import com.google.common.collect.Lists;
import liuns.remoting.framework.model.ProviderService;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * 加权随机
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrastegy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        // 存放加权后的服务列表
        List<ProviderService> providerServiceList = Lists.newArrayList();
        for (ProviderService providerService : providerServices) {
            Integer weight = providerService.getWeight();
            if (weight == null) weight = 1;
            for (int i=0; i<weight; i++) {
                providerServiceList.add(providerService);
            }
        }

        int MAX_LEN = providerServiceList.size();
        int index = RandomUtils.nextInt(0, MAX_LEN - 1);
        return providerServiceList.get(index);
    }
}
