package liuns.service.impl;

import liuns.business.AddressManage;
import liuns.model.business.dto.AddressDTO;
import liuns.model.business.po.AddressPO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;
import liuns.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressManage addressManage;

    public ResponseDTO<Long> insert(RequestDTO<AddressDTO> request) {
        if (!checkNullForRequest(request)) {
            return ResponseDTO.fail("请求参数为空");
        }
        long addressId = addressManage.insert(getAddressPO(request.getData()));
        return ResponseDTO.success(addressId);
    }

    private boolean checkNullForRequest(RequestDTO<?> request) {
        return request == null || request.getData() == null ? false : true;
    }

    /**
     * dto -> po
     *
     * @param dto
     * @return
     */
    private AddressPO getAddressPO(AddressDTO dto) {
        AddressPO po = new AddressPO();
        po.setId(dto.getId());
        po.setAddressName(dto.getAddressName());
        po.setParentId(dto.getParentId());
        po.setAddressCode(dto.getAddressCode());
        return po;
    }
}
