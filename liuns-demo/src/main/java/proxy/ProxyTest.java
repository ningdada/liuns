package proxy;

public class ProxyTest {

    public static void main(String[] args) {

        Interface i = new InterfaceImpl();
        JDKDynamicProxyDemo dynamicProxy = new JDKDynamicProxyDemo(i);
        Interface instance = (Interface) dynamicProxy.newProxyInstance();
        System.out.println(instance.sayHello("world"));

        InterfaceImpl2 i2 = new InterfaceImpl2();
        CglibDynamicProxyDemo dynamicProxy2 = new CglibDynamicProxyDemo();
        InterfaceImpl2 interfaceImpl2  = (InterfaceImpl2) dynamicProxy2.getProxy(i2.getClass());
        System.out.println(interfaceImpl2.sayGun("ndd"));

    }
}
