package service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ArrayBlockingQueue;

@ContextConfiguration(locations = {"classpath:spring-service.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {

    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        for (int i = 0; i < 6; i++) {
            queue.offer(i);
        }

        while (queue.iterator().hasNext()) {
            System.out.println("next:"+queue.iterator().next());
        }
    }
}
