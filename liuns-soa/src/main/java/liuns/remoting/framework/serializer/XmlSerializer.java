package liuns.remoting.framework.serializer;


import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlSerializer implements ISerializer {

    private static final XStream xStream = new XStream(new DomDriver());

    @Override
    public <T> byte[] serialize(T obj) {
        return xStream.toXML(obj).getBytes();
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        return (T) xStream.fromXML(new String(data));
    }

}
