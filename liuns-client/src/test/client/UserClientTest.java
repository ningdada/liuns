
import com.alibaba.fastjson.JSON;
import com.github.javafaker.Faker;
import liuns.interfacer.UserService;
import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Locale;


@ContextConfiguration(locations = {"classpath:spring-client.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserClientTest {

    @Resource(name="userClient")
    private UserService userService;

    @Test
    public void get() {
        for (long i=0L; i<100; i++) {
            RequestDTO<Long> request = new RequestDTO<>();
            request.setData(i);
            ResponseDTO<UserDTO> response = userService.get(request);
            System.out.println(JSON.toJSONString(response));
        }
    }

    @Test
    public void insert() {
        for (long i=2004; i<2005; i++) {
            RequestDTO<UserDTO> request = new RequestDTO<>();
            UserDTO user = new UserDTO();
            Locale locale = new Locale("zh", "CN");
            Faker faker = new Faker(locale);
            user.setId(i);
            user.setUsername(faker.name().username());
            user.setPwd(faker.business().creditCardNumber());
            request.setData(user);
            ResponseDTO<Long> response = userService.insert(request);
            System.out.println("resp:" + JSON.toJSONString(response));
        }
    }
}
