package service.impl;

import com.alibaba.fastjson.JSON;
import model.business.dto.AddressDTO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;
import org.junit.Test;
import service.AddressService;
import service.BaseTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class AddressServiceImplTest extends BaseTest {

    @Resource
    private AddressService addressService;

    @Test
    public void insert() {
        AddressDTO address = new AddressDTO();
        address.setId(5L);
        address.setParentId(1L);
        address.setAddressCode("123");
        address.setAddressName("北京市");
        ResponseDTO<Long> response = addressService.insert(RequestDTO.getInstance(address));
        System.out.println(JSON.toJSONString(response));
    }
}