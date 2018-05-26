package service;

import model.business.dto.AddressDTO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;

public interface AddressService {

    /**
     * 新增地区
     *
     * @param request
     * @return
     */
    ResponseDTO<Long> insert(RequestDTO<AddressDTO> request);
}
