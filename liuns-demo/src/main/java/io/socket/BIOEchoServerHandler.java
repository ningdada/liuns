package io.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket服务端处理业务逻辑
 */
public class BIOEchoServerHandler implements Runnable {

    private Socket socket;

    public BIOEchoServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // 通过Socket对象的getInputStream()与getOutputStream()得到与客户端通信的输入流与输出流
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            while (true) {
                // 获取客户端的数据
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                writer.write(line + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
