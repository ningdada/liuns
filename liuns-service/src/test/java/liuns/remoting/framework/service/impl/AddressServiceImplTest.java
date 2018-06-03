package liuns.remoting.framework.service.impl;

import com.alibaba.fastjson.JSON;
import liuns.interfacer.AddressService;
import liuns.interfacer.model.business.AddressDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import org.junit.Test;
import liuns.remoting.framework.service.BaseTest;

import javax.annotation.Resource;

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