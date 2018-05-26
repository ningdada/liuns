package io.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单文件服务器
 */
public class SimpleFileHttpServer {

    private final static Logger log = LoggerFactory.getLogger(SimpleFileHttpServer.class);

    private final byte[] content;

    private final byte[] header;

    private final int port;

    private final String encoding;

    public SimpleFileHttpServer(String data, String encoding, String mimeType, int port) throws UnsupportedEncodingException {
        this(data.getBytes(encoding), encoding, mimeType, port);
    }

    public SimpleFileHttpServer(byte[] data, String encoding, String mimeType, int port) {
        this.content = data;
        this.port = port;
        this.encoding = encoding;
        String header = "HTTP/1.0 200 OK\r\n" +
                "Server: OneFile 2.0\r\n" +
                "Content-length: " + this.content.length + "\r\n" +
                "Content-type: " + mimeType + "; charset=" + encoding + "\r\n\r\n";
        this.header = header.getBytes(Charset.forName("US-ASCII"));
    }

    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(1000);
        try (ServerSocket server = new ServerSocket(this.port)) {
            log.info("Accepting connetions on port " + server.getLocalPort());
            log.info("data to send: ");
            log.info(new String(this.content, encoding));

            for (;;) {
                try {
                    Socket connetion = server.accept();
                    pool.submit(new HttpHandler(connetion));
                } catch (IOException e) {
                    log.warn("Exception accepting connetion", e);
                } catch (RuntimeException e) {
                    log.error("UnException error", e);
                }
            }
        } catch (IOException e) {
            log.error("Could not start server", e);
        }
    }

    /**
     * Socket数据处理类
     */
    private class HttpHandler implements Callable<Void> {

        private final Socket connetion;

        public HttpHandler(Socket connetion) {
            this.connetion = connetion;
        }

        @Override
        public Void call() throws Exception {
            try {
                BufferedOutputStream out = new BufferedOutputStream(connetion.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(connetion.getInputStream());
                StringBuilder request = new StringBuilder(80);
                // 只读取第一行
                for (int i = in.read(); i != -1; i = in.read()) {
                    if (i == '\r' || i == '\n') break;
                    request.append((char)i);
                }
                // 如果是HTTP1.1或以后的版本，则发送一个MIME首部
                if (request.toString().indexOf("HTTP/") != -1) {
                    out.write(header);
                }
                out.write(content);
                out.flush();
            } catch (IOException e) {
                log.warn("error writing to client", e);
            } finally {
                connetion.close();
            }
            return null;
        }
    }


    public static void main(String[] args) {

        int port;
        try {
            port = Integer.parseInt(args[1]);
            if (port < 1 || port > 65535) port = 80;
        } catch (NumberFormatException e) {
            port = 80;
        }
        String encoding = "UTF-8";
        if (args.length > 2) encoding = args[2];

        try {
            Path path = Paths.get(args[0]);
            byte[] data = Files.readAllBytes(path);

            String contentType = URLConnection.getFileNameMap().getContentTypeFor(args[0]);
            SimpleFileHttpServer server = new SimpleFileHttpServer(data, encoding, contentType, port);
            server.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java SingleFileHttpServer filename port encoding");
        } catch (IOException e) {
            log.error("serve start error", e);
        }
    }

}
