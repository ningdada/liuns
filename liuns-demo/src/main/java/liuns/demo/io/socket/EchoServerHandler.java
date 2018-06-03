package liuns.demo.io.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerHandler implements Runnable {

    private int port;

    public EchoServerHandler(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            in = socket.getInputStream();;
            out = socket.getOutputStream();
            StringBuilder content = new StringBuilder("send: ");
            while (in.read() != -1) {
                if (content.length() > 10) break;
                content.append((char)in.read());
            }
            System.out.println("send");

            out.write(content.toString().getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
