package business.impl;

import business.AddressManage;
import business.BaseTest;
import com.alibaba.fastjson.JSON;
import model.business.po.AddressPO;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class AddressManageImplTest extends BaseTest {

    @Resource
    private AddressManage addressManage;

    @Test
    public void insert() {
        AddressPO address = new AddressPO();
        address.setId(4L);
        address.setParentId(0L);
        address.setAddressCode("001");
        address.setAddressName("中国");
        long addressId = addressManage.insert(address);
        System.out.println(addressId);
    }

    @Test
    public void update() {
    }

    @Test
    public void get() {
        AddressPO address = addressManage.get(3L);
        System.out.println(JSON.toJSONString(address));
    }
}