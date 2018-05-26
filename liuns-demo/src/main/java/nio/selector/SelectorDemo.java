package nio.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector 选择器
 * <p />
 * open()：创建一个Selector
 * 通道触发了一个事件意思是该事件已经就绪。所以，
 * 一个channel成功连接到另一个服务器称为“连接就绪”。
 * 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
 * 一个有数据可读的通道可以说是“读就绪”。
 * 一个等待写数据的通道可以说是“写就绪”
 * 可监听Channel的四种事件：
 * 1. SelectionKey.OP_CONNECT
 * 2. SelectionKey.OP_ACCEPT
 * 3. SelectionKey.OP_READ
 * 4. SelectionKey.OP_WRITE
 */
public class SelectorDemo {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        while (true) {
            int readChannels = selector.select();
            if (readChannels == 0) continue;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    // connetion accepted 事件
                } else if (next.isConnectable()) {
                    // connetion established 事件
                } else if (next.isReadable()) {
                    // channel 准备好读数据事件
                } else if (next.isWritable()) {
                    // channel 准备好写数据事件
                }
                iterator.remove();
            }
        }
    }
}
