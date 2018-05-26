package demo;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ZkClientDemo {

    public static void main(String[] args) throws Exception {

        // 创建会话
        String zkServers = "127.0.0.1:2181";
        int connectionTimeout = 3000;
        ZkClient zkClient = new ZkClient(zkServers, connectionTimeout);

        // 创建ZK节点
        String path = "/zkClientDemo/8081";
        if (!zkClient.exists(path)) {
            zkClient.createEphemeral(path);
        }

        // 节点写入数据
        zkClient.writeData(path, "ndd");

        // 读取节点数据
        String data = zkClient.<String>readData(path, true);
        System.out.println(data);

        // 注册监听器，监听节点变化
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> childs) throws Exception {
                System.out.println("handlePathChange, path:" + parentPath + ",childs:" + JSON.toJSONString(childs));
            }
        });

        // 注册监听器，监听节点数据变化(测试发现只能监听节点删除事件，不能监听数据更新事件)
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println("handleDataChange, dataPath:" + path);
            }

            public void handleDataDeleted(String path) throws Exception {
                System.out.println("handleDataPathDeleted, dataPath:" + path);
            }
        });

        // 删除节点
//        zkClient.delete(path);

        Thread.sleep(Integer.MAX_VALUE);
    }

}
