package liuns.remoting.framework.service.impl;

import com.alibaba.fastjson.JSON;
import liuns.interfacer.UserService;
import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import org.junit.Test;
import liuns.remoting.framework.service.BaseTest;

import javax.annotation.Resource;

public class UserServiceImplTest extends BaseTest{

    @Resource
    private UserService userService;

    @Test
    public void get() {
        long id = 1L;
        RequestDTO<Long> request = RequestDTO.getInstance(id);
        ResponseDTO<UserDTO> response = userService.get(request);
        System.out.println(JSON.toJSONString(response));
    }
}