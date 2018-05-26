package business.serializer;


import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.business.po.UserPO;

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

    public static void main(String[] args) {
        UserPO user = new UserPO();
        user.setUsername("宁大大");
        ISerializer serializer = new XmlSerializer();
        byte[] bytes = serializer.serialize(user);
        System.out.println(JSON.toJSONString(serializer.deserializer(bytes, UserPO.class)));
    }
}
