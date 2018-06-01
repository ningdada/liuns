package liuns.service;

import liuns.model.business.dto.AddressDTO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;

public interface AddressService {

    /**
     * 新增地区
     *
     * @param request
     * @return
     */
    ResponseDTO<Long> insert(RequestDTO<AddressDTO> request);
}
