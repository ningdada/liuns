package liuns.demo.nio2.socket;

/**
 * AIO 服务端
 */
public class AIOEchoServer {

    public static void main(String[] args) {
        int port = 8080;
        AsyncEchoServerHandler timeServer = new AsyncEchoServerHandler(port);
        new Thread(timeServer).start();
    }
}
