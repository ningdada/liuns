package liuns.remoting.framework.service.impl;

import com.alibaba.fastjson.JSON;
import liuns.model.business.dto.AddressDTO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;
import liuns.service.AddressService;
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