package liuns.remoting.framework.loadBalance;

import com.google.common.collect.Maps;

import java.util.Map;

public class ClusterEngine {

    private static final Map<ClusterStrategyEnum, ClusterStrastegy> clusterStrategyMap = Maps.newConcurrentMap();

    static {
        clusterStrategyMap.put(ClusterStrategyEnum.Polling, new PollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightPolling, new WeightPollingClusterStrastegyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Random, new RandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightRandom, new WeightRandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Hash, new HashClusterStrategyImpl());
    }

    public static ClusterStrastegy selectClusterStrategy(String clusterStrategy) {
        ClusterStrategyEnum clusterStrategyEnum = ClusterStrategyEnum.valueOf(clusterStrategy);
        if (clusterStrategyEnum == null) {
            return clusterStrategyMap.get(ClusterStrategyEnum.Random);
        }
        return clusterStrategyMap.get(clusterStrategyEnum);
    }
}
