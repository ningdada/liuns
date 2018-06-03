package liuns.client.hessian;

import com.alibaba.fastjson.JSON;
import liuns.interfacer.AddressService;
import liuns.interfacer.model.business.AddressDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AddressClient {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("hessian-rpc-client.xml");
        AddressService addressService = (AddressService) ctx.getBean("addressHessianService");
        AddressDTO address = new AddressDTO();
        address.setId(9L);
        address.setParentId(1L);
        address.setAddressCode("123");
        address.setAddressName("北京市");
        ResponseDTO<Long> response = addressService.insert(RequestDTO.getInstance(address));
        System.out.println(JSON.toJSONString(response));
    }
}
