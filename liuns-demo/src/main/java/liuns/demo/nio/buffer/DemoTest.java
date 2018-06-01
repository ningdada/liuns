package liuns.demo.nio.buffer;

import java.nio.ByteBuffer;

public class DemoTest {

    public static void main(String[] args) {

        String value = "abcdefg";
        System.out.println(value.getBytes());
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put(value.getBytes());
        System.out.println("capacity:"+buffer.capacity());
        System.out.println("limit:"+buffer.limit());
        System.out.println("position:"+buffer.position());
        System.out.println("mark:"+buffer.mark());

//        buffer.flip();

        System.out.println("---------------");
        System.out.println("capacity:"+buffer.capacity());
        System.out.println("limit:"+buffer.limit());
        System.out.println("position:"+buffer.position());
        System.out.println("mark:"+buffer.mark());


        System.out.println();
    }

}
