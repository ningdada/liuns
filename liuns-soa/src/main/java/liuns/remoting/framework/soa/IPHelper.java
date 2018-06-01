package liuns.remoting.framework.soa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPHelper {

    private static final Logger log = LoggerFactory.getLogger(IPHelper.class);

    /**
     * 获取本地ip
     *
     * @return
     */
    public static String localIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本地ip出错", e);
            return null;
        }
    }
}
