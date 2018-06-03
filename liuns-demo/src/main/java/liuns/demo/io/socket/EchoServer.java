package liuns.demo.io.socket;

public class EchoServer {


    public static void main(String[] args) {
        EchoServerHandler handler = new EchoServerHandler(8088);
        new Thread(handler).start();
    }
}
