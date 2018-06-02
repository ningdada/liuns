package liuns.remoting.framework.invoker;

import liuns.remoting.framework.model.InvokerService;
import liuns.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * 消费端注册中心接口
 */
public interface IRegisterCenter4Invoker {

    /**
     * 消费端初始化服务提供者信息本地缓存
     */
    void initProviderMap();

    /**
     * 消费端获取服务提供者信息
     *
     * @return
     */
    Map<String, List<ProviderService>> getServiceMetaDataMap4Consume();

    /**
     * 消费端将消费者信息注册到ZK对应的节点下
     *
     * @param invokerService
     */
    void registerInvoker(final InvokerService invokerService);
}
