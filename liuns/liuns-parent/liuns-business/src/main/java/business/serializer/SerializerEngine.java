package business.serializer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import model.business.po.UserPO;

import java.util.Map;

public class SerializerEngine {

    private static final Map<SerializeType, ISerializer> serializerMap = Maps.newConcurrentMap();

    static {
        serializerMap.put(SerializeType.DefaultJavaSerializer, new DefaultJavaSerializer());
        serializerMap.put(SerializeType.HessianSerialzer, new HessianSerializer());
        serializerMap.put(SerializeType.JSONSerializer, new JSONSerializer());
        serializerMap.put(SerializeType.XmlSerializer, new XmlSerializer());
        serializerMap.put(SerializeType.ProtostuffSerializer, new ProtostuffSerializer());
    }

    /**
     * 序列化
     *
     * @param obj
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj, SerializeType serializeType) {
        if (serializeType == null) {
            throw new RuntimeException("serialize is null");
        }
        ISerializer serializer = serializerMap.get(serializeType);
        if (serializer == null) {
            throw new RuntimeException("serialize error");
        }
        try {
            return serializer.serialize(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz, SerializeType serializeType) {
        if (serializeType == null) {
            throw new RuntimeException("serialize is null");
        }
        ISerializer serializer = serializerMap.get(serializeType);
        if (serializer == null) {
            throw new RuntimeException("serialize error");
        }
        try {
            return serializer.deserializer(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UserPO user = new UserPO();
        user.setUsername("宁大大");
        byte[] bytes = SerializerEngine.serialize(user, SerializeType.JSONSerializer);
        UserPO result = SerializerEngine.deserialize(bytes, UserPO.class, SerializeType.JSONSerializer);
        System.out.println(JSON.toJSONString(result));
    }
}
