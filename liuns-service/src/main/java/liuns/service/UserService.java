package liuns.service;


import liuns.model.business.dto.UserDTO;
import liuns.model.common.dto.RequestDTO;
import liuns.model.common.dto.ResponseDTO;

public interface UserService {

    /**
     * 根据用户id获取用户
     *
     * @param request
     * @return
     */
    ResponseDTO<UserDTO> get(RequestDTO<Long> request);
}
