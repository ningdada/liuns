package liuns.demo.nio2.socket;

/**
 * AIO 客户端
 */
public class AIOEchoClient {

    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncEchoClientHandler("127.0.0.1", port)).start();
    }
}
