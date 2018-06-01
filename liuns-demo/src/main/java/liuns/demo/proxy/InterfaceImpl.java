package liuns.demo.proxy;

public class InterfaceImpl implements Interface {
    @Override
    public String sayHello(String name) {
        return "hello " + name + "!";
    }
}
