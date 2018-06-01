package liuns.remoting.framework.serializer;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        if (data == null) {
            throw new NullPointerException();
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput hi = new HessianInput(is);
            return (T) hi.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
