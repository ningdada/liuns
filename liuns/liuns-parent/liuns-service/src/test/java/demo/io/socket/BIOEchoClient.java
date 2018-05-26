package demo.io.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket客户端
 * <p />
 * 1. 创建Socket客户端，使用创建的Socket连接远程主机 <br />
 * 2. 建立连接后，从Socket得到输入流与输出流，Socket是全双工通道，可以使用这两个流与服务器之间相互发送数据 <br />
 */
public class BIOEchoClient {

    public static void main(String[] args) {

        int port = 8081;
        String serverIp = "127.0.0.1";
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // 创建Socket对象，并连接远程主机
            socket = new Socket(serverIp, port);
            // 建立连接后，从Socket得到输入流与输出流，可以使用这两个流与服务器之间相互发送数据
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("Hello");
            writer.flush();
            String echo = reader.readLine();
            System.out.println("echo:" + echo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
