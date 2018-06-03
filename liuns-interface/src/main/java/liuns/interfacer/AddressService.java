package liuns.interfacer;


import liuns.interfacer.model.business.AddressDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;

public interface AddressService {

    /**
     * 新增地区
     *
     * @param request
     * @return
     */
    ResponseDTO<Long> insert(RequestDTO<AddressDTO> request);
}
