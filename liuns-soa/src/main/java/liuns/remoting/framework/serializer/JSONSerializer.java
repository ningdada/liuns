package liuns.remoting.framework.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONSerializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }
}
