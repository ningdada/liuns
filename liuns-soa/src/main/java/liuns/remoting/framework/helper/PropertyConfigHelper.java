package liuns.remoting.framework.helper;

import liuns.remoting.framework.serializer.SerializeType;

public class PropertyConfigHelper {

    public static String getZkService() {
        return "127.0.0.1:2181";
    }

    public static int getZkConnectionTimeout() {
        return 1000;
    }

    public static int getZkSessionTimeout() {
        return 1000;
    }

    public static int getChannelConnectSize() {
        return 15;
    }

    public static SerializeType getSerializeType() {
        return SerializeType.HessianSerialzer;
    }
}
