package liuns.client.rmi;

import com.alibaba.fastjson.JSON;
import liuns.interfacer.UserService;
import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserClient {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("rmi-rpc-client.xml");
        UserService userService = (UserService) ctx.getBean("userRmiServiceProxy");
        Long id = 1L;
        ResponseDTO<UserDTO> response = userService.get(RequestDTO.getInstance(id));
        System.out.println(JSON.toJSONString(response));
    }
}
