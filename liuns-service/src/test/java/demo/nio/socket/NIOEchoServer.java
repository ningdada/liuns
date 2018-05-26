package demo.nio.socket;

public class NIOEchoServer {

    public static void main(String[] args) {
        int port = 8080;
        NIOEchoServerHandler server = new NIOEchoServerHandler(port);
        new Thread(server).start();
    }
}
