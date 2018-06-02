package liuns.remoting.framework;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import liuns.remoting.framework.helper.IPHelper;
import liuns.remoting.framework.helper.PropertyConfigHelper;
import liuns.remoting.framework.invoker.IRegisterCenter4Invoker;
import liuns.remoting.framework.model.InvokerService;
import liuns.remoting.framework.provider.IRegisterCenter4Provider;
import liuns.remoting.framework.model.ProviderService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 服务注册中心
 * <p />
 * 客户端和服务端通过这个类将服务注册到注册中心上 <br />
 * 客户端通过ZK监听机制即使更新注册中心上的服务节点信息 <br />
 */
public class RegisterCenter implements IRegisterCenter4Provider, IRegisterCenter4Invoker {

    private static RegisterCenter registerCenter = new RegisterCenter();

    /**
     * 服务提供者列表，key：服务提供者接口  value：服务提供者服务方法列表
     */
    private static final Map<String, List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    /**
     * 服务端ZK服务元信息，选择服务（第一次直接从ZK拉取，后续由ZK监听机制主动更新）
     */
    private static final Map<String, List<ProviderService>> serviceMetaDataMap4Consume = Maps.newConcurrentMap();

    /**
     * 从配置文件中获取ZK的服务地址列表
     */
    private static String ZK_SERVICE = PropertyConfigHelper.getZkService();

    /**
     * 从配置文件中获取ZK会话超时时间配置
     */
    private static int ZK_SESSION_TIME_OUT = PropertyConfigHelper.getZkSessionTimeout();

    /**
     * 从配置文件中获取ZK连接超时时间配置
     */
    private static int ZK_CONNECTION_TIME_OUT = PropertyConfigHelper.getZkConnectionTimeout();

    /**
     * 组装ZK根路径/APPKEY路径
     */
    private static String ROOT_PATH = "/liuns";

    private static String PROVIDER_TYPE = "/provider";

    private static String INVOKER_TYPE = "/consumer";

    private static volatile ZkClient zkClient = null;

    private RegisterCenter() {}

    public static RegisterCenter singleton() {
        return registerCenter;
    }


    public void initProviderMap() {
        if (MapUtils.isEmpty(serviceMetaDataMap4Consume)) {
            serviceMetaDataMap4Consume.putAll(fetchOrUpdateServiceMetaData());
        }
    }

    public Map<String, List<ProviderService>> getServiceMetaDataMap4Consume() {
        return serviceMetaDataMap4Consume;
    }

    public void registerInvoker(InvokerService invoker) {
        if (invoker == null) {
            return;
        }

        // 连接ZK，注册服务
        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
            // 创建ZK命名空间/当前部署应用APP命名空间
            boolean exists = zkClient.exists(ROOT_PATH);
            if (!exists) {
                zkClient.createPersistent(ROOT_PATH, true);
            }
            // 创建服务提供者节点
            exists = zkClient.exists((ROOT_PATH));
            if (!exists) {
                zkClient.createPersistent(ROOT_PATH);
            }

            // 创建服务消费者节点
            String serviceNode = invoker.getServiceItf().getName();
            String servicePath = ROOT_PATH + "/" + serviceNode + INVOKER_TYPE;
            exists = zkClient.exists(servicePath);
            if (!exists) {
                zkClient.createPersistent(servicePath);
            }

            // 创建当前服务器节点
            String localIp = IPHelper.localIp();
            String currentServiceIpNode = servicePath + "/" + localIp;
            exists = zkClient.exists(currentServiceIpNode);
            if (!exists) {
                // 这里是临时节点
                zkClient.createEphemeral(currentServiceIpNode);
            }
        }
    }

    public void registerProvider(final List<ProviderService> serviceMetaData) {
        if (CollectionUtils.isEmpty(serviceMetaData)) {
            return;
        }

        // 连接ZK，注册服务
        synchronized (RegisterCenter.class) {
            for (ProviderService provider : serviceMetaData) {
                String serviceItfKey = provider.getServiceItf().getName();
                List<ProviderService> providers = providerServiceMap.get(serviceItfKey);
                if (providers == null) {
                    providers = Lists.newArrayList();
                }
                providers.add(provider);
                providerServiceMap.put(serviceItfKey, providers);
            }
            if (zkClient == null) {
                // 这里可以自定义序列化
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }
            // 创建ZK命名空间/当前部署应用APP命名空间
            boolean exists = zkClient.exists(ROOT_PATH);
            if (!exists) {
                zkClient.createPersistent(ROOT_PATH, true);
            }
            // 创建服务提供者节点
            exists = zkClient.exists((ROOT_PATH));
            if (!exists) {
                zkClient.createPersistent(ROOT_PATH);
            }
            for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
                // 创建服务提供者节点
                String serviceNode = entry.getKey();
                String servicePath = ROOT_PATH + "/" + serviceNode + PROVIDER_TYPE;
                exists = zkClient.exists(servicePath);
                if (!exists) {
                    zkClient.createPersistent(servicePath, true);
                }

                // 创建当前服务器节点
                int serverPort = entry.getValue().get(0).getServerPort();
                String localIp = IPHelper.localIp();
                String currentServiceIpNode = servicePath + "/" + localIp + "|" + serverPort;
                exists = zkClient.exists(currentServiceIpNode);
                if (!exists) {
                    // 这里是临时节点
                    zkClient.createEphemeral(currentServiceIpNode);
                }
                // 监听注册服务的变化，同时更新数据到本地缓存
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        if (currentChilds == null) {
                            currentChilds = Lists.newArrayList();
                        }

                        // 存活的服务IP列表
                        List<String> activityServiceIpList = Lists.newArrayList(Lists.transform(currentChilds, new Function<String, String>() {
                            public String apply(String s) {
                                return StringUtils.split(s, "|")[0];
                            }
                        }));
                        refreshActivityService(activityServiceIpList);
                    }
                });
            }
        }
    }

    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return providerServiceMap;
    }

    /**
     * 利用ZK自动刷新当前存活的服务提供者列表数据
     *
     * @param serviceIpList
     */
    private void refreshActivityService(List<String> serviceIpList) {
        if (serviceIpList == null) {
            serviceIpList = Lists.newArrayList();
        }

        Map<String, List<ProviderService>> currentServiceMetaDataMap = Maps.newHashMap();
        for (Map.Entry<String, List<ProviderService>> entry : providerServiceMap.entrySet()) {
            String key = entry.getKey();
            List<ProviderService> providerServices = entry.getValue();
            List<ProviderService> serviceMetaDataModelList = currentServiceMetaDataMap.get(key);
            if (serviceMetaDataModelList == null) {
                serviceMetaDataModelList = Lists.newArrayList();
            }

            for (ProviderService serviceMetaData : providerServices) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    serviceMetaDataModelList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(key, serviceMetaDataModelList);
        }
        providerServiceMap.putAll(currentServiceMetaDataMap);
    }

    private Map<String, List<ProviderService>> fetchOrUpdateServiceMetaData() {
        final Map<String, List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();
        // 连接ZK
        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIME_OUT, ZK_CONNECTION_TIME_OUT, new SerializableSerializer());
            }

            // 从ZK获取服务提供者列表
            String providerPath = ROOT_PATH;
            List<String> providerServices = zkClient.getChildren(providerPath);
            for (String serviceName : providerServices) {
                String servicePath = providerPath + "/" + serviceName + PROVIDER_TYPE;
                List<String> ipPathList = zkClient.getChildren(servicePath);
                for (String ipPath : ipPathList) {
                    String serverIp = StringUtils.split(ipPath, "|")[0];
                    String serverPort = StringUtils.split(ipPath, "|")[1];

                    List<ProviderService> providerServiceList = providerServiceMap.get(serviceName);
                    if (providerServiceList == null) {
                        providerServiceList = Lists.newArrayList();
                    }
                    ProviderService providerService = new ProviderService();
                    try {
                        providerService.setServiceItf(ClassUtils.getClass(serviceName));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    providerService.setServerIp(serverIp);
                    providerService.setServerPort(Integer.parseInt(serverPort));
                    providerServiceList.add(providerService);
                    providerServiceMap.put(serviceName, providerServiceList);
                }

                // ZK监听机制
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        if (currentChilds == null) {
                            currentChilds = Lists.newArrayList();
                        }
                        currentChilds = Lists.newArrayList(Lists.transform(currentChilds, new Function<String, String>() {
                            public String apply(String s) {
                                return StringUtils.split(s, "|")[0];
                            }
                        }));
                        refreshServiceMetaDataMap(currentChilds);
                    }
                });
            }
            return providerServiceMap;
        }
    }

    private void refreshServiceMetaDataMap(List<String> serviceIpList){
        if (serviceIpList == null) {
            serviceIpList = Lists.newArrayList();
        }

        Map<String, List<ProviderService>> currentServiceMetaDataMap = Maps.newHashMap();
        for (Map.Entry<String, List<ProviderService>> entry : serviceMetaDataMap4Consume.entrySet()) {
            String serviceItfKey = entry.getKey();
            List<ProviderService> serviceList = entry.getValue();
            List<ProviderService> providerServiceList = currentServiceMetaDataMap.get(serviceItfKey);
            if (providerServiceList == null) {
                providerServiceList = Lists.newArrayList();
            }
            for (ProviderService serviceMetaData : serviceList) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    providerServiceList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(serviceItfKey, providerServiceList);
        }
        serviceMetaDataMap4Consume.putAll(currentServiceMetaDataMap);
        System.out.println("serviceMetaDataMap4Consume:" + JSON.toJSONString(serviceMetaDataMap4Consume));
    }
}


