package liuns.interfacer;


import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;

public interface UserService {

    /**
     * 根据用户id获取用户
     *
     * @param request
     * @return
     */
    ResponseDTO<UserDTO> get(RequestDTO<Long> request);

    /**
     * 添加用户
     *
     * @param request
     * @return 用户ID
     */
    ResponseDTO<Long> insert(RequestDTO<UserDTO> request);
}
