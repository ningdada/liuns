package client.hessian;

import client.serviceInterface.UserService;
import com.alibaba.fastjson.JSON;
import model.business.dto.UserDTO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserClient {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("hessian-rpc-client.xml");
        UserService userService = (UserService) ctx.getBean("userServiceProxy");
        Long id = 1L;
        ResponseDTO<UserDTO> response = userService.get(RequestDTO.getInstance(id));
        System.out.println(JSON.toJSONString(response));
    }
}
