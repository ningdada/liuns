package soa;

import java.util.List;
import java.util.Map;

/**
 * 服务端注册中心接口
 */
public interface IRegisterCenter4Provider {

    /**
     * 服务端将服务提供者信息注册到ZK对应的节点下
     */
    void registerProvider(final List<ProviderService> serviceMetaData);

    /**
     * 服务端获取服务提供者信息
     * <p />
     * 注：返回对象：key：服务端提供者接口  value：服务端提供者服务方法列表
     *
     * @return
     */
    Map<String, List<ProviderService>> getProviderServiceMap();
}
