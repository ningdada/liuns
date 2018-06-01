package liuns.demo.proxy;

import com.google.common.reflect.AbstractInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 * <p />
 * 主要就是要实现java.lang.reflect.InvocationHandler接口以及利用通过Proxy.newProxyInstance()来创建动态代理 <br />
 * 从通过Proxy.newProxyInstance()参数中我们可以看到，能被JDK动态代理的对象必须要实现一个接口 <br />
 */
public class JDKDynamicProxyDemo extends AbstractInvocationHandler {

    private Object proxyObject;

    public Object newProxyInstance() {
        // 通过Proxy.newProxyInstance()可以来创建动态代理
        return Proxy.newProxyInstance(this.proxyObject.getClass().getClassLoader(), this.proxyObject.getClass().getInterfaces(), this);
    }

    @Override
    protected Object handleInvocation(Object obj, Method method, Object[] args) throws Throwable {
        return method.invoke(this.proxyObject, args);
    }

    public JDKDynamicProxyDemo(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

}
