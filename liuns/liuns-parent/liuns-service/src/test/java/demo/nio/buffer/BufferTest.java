package demo.nio.buffer;

import java.nio.ByteBuffer;

/**
 * Buffer四个重要的概念：<br />
 * 1. Capacity：容量 <br />
 * 2. Limit：上界 <br />
 * 3. Position：位置 <br />
 * 4. Mark：标记 <br />
 * 0 <= mark <= position <= limit <= capacity
 * <p />
 * 关键动作：<br />
 * 1. allocate()：创建Buffer并初始化容量 <br />
 * 2. flip()：翻转，将Buffer从读模式切换成写模式，在读模式下可以读取之前写入的所有数据 <br />
 * 3. clear()：清空整个Buffer缓存区，以便可以再次被写入 <br />
 * 4. compact()：只会清除已经的数据，未读过的数据被移动至Buffer的起始处，新写入的数据放入未读数据的后面 <br />
 * 5. rewind()：将Position设置成0，可以让你重读Buffer中的所有数据，Limit保持不变 <br />
 * 6. hasRemaining()：是否已达到Buffer的上界 <br />
 * 7. get()：读一个单位数据 <br />
 * 8. put()：写一个单位数据 <br />
 * <p />
 * 基本步骤：<br />
 * 1. 写入数据到Buffer中 <br />
 * 2. 调用flip()方法 <br />
 * 3. 从Buffer中读取数据 <br />
 * 4. 调用clear()方法或者compact()方法 <br />
 */
public class BufferTest {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(50);
        String content = "mynameis:ndd";
        // 将数据存入Buffer
        for (int i=0; i<content.length(); i++) {
            buffer.put((byte)content.charAt(i));
        }
        // 反转Buffer，准备读取Buffer内容

        buffer.flip();

        // 读取Buffer中的数据
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        // 倒回读取之前，准备再次读取
        buffer.rewind();
        System.out.println();

        // 再次读取Buffer中的数据
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
        System.out.println();

        // 清空Buffer，复位position，Buffer可以再次服用
        buffer.clear();
        buffer.put((byte)'哈').put((byte)'j');
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

    }
}
