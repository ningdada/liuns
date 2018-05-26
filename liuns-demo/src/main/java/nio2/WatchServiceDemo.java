package nio2;

import java.io.IOException;
import java.nio.file.*;

/**
 * WatchService
 * <p />
 * 提供流通过应用程序监听操作系统文件变更事件的能力（文件创建，修改，删除），
 * 通过在Path上注册所需要的监听事件，应用程序能实时感知到这种变化
 */
public class WatchServiceDemo {

    public void watchDic(Path path) throws IOException, InterruptedException {

        // 创建WatchService实例
        WatchService watchService = FileSystems.getDefault().newWatchService();

        // 注册WatchService所监听的事件，目录的创建，修改，删除
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        while (true) {
            final WatchKey key = watchService.take();
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                WatchEvent.Kind<?> kind = watchEvent.kind();
                // 忽略OVERFLOW事件
                if (kind == StandardWatchEventKinds.OVERFLOW) {

                }
                final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                final Path fileName = watchEventPath.context();
                // 打印事件及发生事件的文件
                System.out.println(kind + ":" + fileName);
            }

            // 重置key
            boolean valid = key.reset();

            // 如果key无效（比如监听的文件被删除或目录被重命名等），则退出
            if (!valid) {
                break;
            }
        }

    }

    public static void main(String[] args) {

        Path path = Paths.get("/Users/ningdd/Downloads/filesDemo");
        WatchServiceDemo watchService = new WatchServiceDemo();
        try {
            watchService.watchDic(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
