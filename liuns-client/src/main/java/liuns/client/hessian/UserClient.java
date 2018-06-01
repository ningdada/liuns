package liuns.client.hessian;

import liuns.client.serviceInterface.UserService;
import com.alibaba.fastjson.JSON;
import liuns.model.business.dto.UserDTO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;
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
