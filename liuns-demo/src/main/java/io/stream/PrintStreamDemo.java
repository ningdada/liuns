package io.stream;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * PrintStream
 * <p />
 * 适合用来处理字节流
 */
public class PrintStreamDemo {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/ningdd/Downloads/operate.txt");
        PrintStream print = new PrintStream(file);
        print.println("嗯，我知道是你！");
        print.close();
    }
}
