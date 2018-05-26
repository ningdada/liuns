package nio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Iterator;

/**
 * Files
 */
public class FilesDemo {

    public static void main(String[] args) throws IOException {

        // 遍历Downloads下的子目录,但不会递归遍历子目录里的文件
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("/Users/ningdd/Downloads"));

        Iterator<Path> iterator = directoryStream.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            System.out.println(path);
        }

        // 递归创建文件
        Path directory = Files.createDirectories(Paths.get("/Users/ningdd/Downloads/filesDemo"));

        // 创建文件
        Path file = Files.createFile(Paths.get(directory.toAbsolutePath() + "/aa.txt"));

        // 使用缓冲字符流写入文件内容
        Charset charset = Charset.forName("utf-8");
        String content = "你好哇！";
        BufferedWriter writer = Files.newBufferedWriter(file, charset, StandardOpenOption.APPEND);
        writer.write(content);
        writer.close();

        // 使用缓冲字符流读取文件内容
        BufferedReader reader = Files.newBufferedReader(file, charset);
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}
