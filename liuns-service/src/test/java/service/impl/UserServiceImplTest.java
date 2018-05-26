package service.impl;

import com.alibaba.fastjson.JSON;
import model.business.dto.UserDTO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;
import org.junit.Test;
import service.BaseTest;
import service.UserService;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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