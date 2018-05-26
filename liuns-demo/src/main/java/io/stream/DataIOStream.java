package io.stream;

import java.io.*;

/**
 * DataInputStream   DataOutputStream
 * <p />
 * 适合用来处理字节流
 */
public class DataIOStream {

    public static void main(String[] args) throws IOException {

        String fileName = "/Users/ningdd/Downloads/operate.txt";

        // 将java原生类型数据通过DataOutputStream写入文件
        FileOutputStream out = new FileOutputStream(fileName);
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(2017);
        dataOut.writeUTF("呵呵?");
        dataOut.writeBoolean(true);

        dataOut.close();
        out.close();

        // 使用DataInputStream从文件中按照顺序读取java原生类型数据
        FileInputStream in = new FileInputStream(fileName);
        DataInputStream dataIn = new DataInputStream(in);
        System.out.println(dataIn.readInt());
        System.out.println(dataIn.readUTF());
        System.out.println(dataIn.readBoolean());

        dataIn.close();
        in.close();
    }
}
