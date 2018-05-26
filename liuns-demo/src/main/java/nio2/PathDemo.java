package nio2;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path
 */
public class PathDemo {

    public static void main(String[] args) {

        // Paths.get()可通过相对路径获取，也可通过绝对路径获取

        Path path = Paths.get("/Users/ningdd/Downloads/demo.demo.txt");
        System.out.println("path1:" + path);

        path = Paths.get("Users", "ningdd", "Downloads", "demo.demo.txt");
        System.out.println("path2:" + path);


        // Pahts.get()还可以通过URI获取
        path = Paths.get("http://ifeve.com/wp-content/uploads/2018/05/WechatIMG1.jpeg");
        System.out.println("path3:" + path);

        // 还可以通过FileSystem获取

        // Path转File
        File file = path.toFile();
        System.out.println("fileName:" + file.getName());

        // Path转URI
        URI uri = path.toUri();
        System.out.println("uri:" + uri);

        // 获取绝对路径
        path = path.toAbsolutePath();
        System.out.println("absolutePath:" + path.toString());
    }
}
