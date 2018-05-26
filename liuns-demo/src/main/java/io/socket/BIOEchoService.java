package io.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Socket服务端
 * <p />
 * 1. 绑定一个特定的端口创建ServerSocket对象 <br />
 * 2. 使用ServerSocket的accept()监听这个端口的请求连接，accept()会一直阻塞知道通过某个请求与客户端建连接，
 *    此时accept()将返回客户端与服务端的连接的Socket对象 <br />
 * 3. 通过Socket对象的getInputStream()与getOutputStream()得到与客户端通信的输入流与输出流，进行通信交互 <br />
 * 4. 完成交互后关闭连接 <br />
 *
 */
public class BIOEchoService {

    // 服务端处理业务逻辑线程池
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        int port = 8081;
        ServerSocket serverSocket = null;

        try {
            // 绑定一个特定的端口创建ServerSocket对象
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                // 使用ServerSocket的accept()监听这个端口的请求连接
                socket = serverSocket.accept();
                executor.submit(new BIOEchoServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
