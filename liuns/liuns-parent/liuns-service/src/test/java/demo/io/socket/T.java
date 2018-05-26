package demo.io.socket;

import java.io.InputStream;
import java.net.Socket;

public class T {

    public static void main(String[] args) {
        try (Socket socket = new Socket("time.nist.gov", 13)) {
            InputStream in = socket.getInputStream();
            StringBuilder content = new StringBuilder();
            for (int i = in.read(); i != -1; i = in.read()) {
                content.append((char)i);
            }
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
