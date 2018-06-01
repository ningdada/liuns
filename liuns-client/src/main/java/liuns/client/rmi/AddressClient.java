package liuns.client.rmi;

import liuns.client.serviceInterface.AddressService;
import com.alibaba.fastjson.JSON;
import liuns.model.business.dto.AddressDTO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AddressClient {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("rmi-rpc-client.xml");
        AddressService addressService = (AddressService) ctx.getBean("addressRmiServiceProxy");
        AddressDTO address = new AddressDTO();
        address.setId(8L);
        address.setParentId(1L);
        address.setAddressCode("123");
        address.setAddressName("北京市");
        ResponseDTO<Long> response = addressService.insert(RequestDTO.getInstance(address));
        System.out.println(JSON.toJSONString(response));
    }
}
