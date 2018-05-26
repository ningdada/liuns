package client.serviceInterface;

import model.business.dto.UserDTO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;

public interface UserService {

    /**
     * 根据用户id获取用户
     *
     * @param request
     * @return
     */
    ResponseDTO<UserDTO> get(RequestDTO<Long> request);
}
