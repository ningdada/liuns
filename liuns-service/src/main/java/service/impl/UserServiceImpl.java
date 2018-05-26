package service.impl;

import business.UserManage;
import model.business.dto.UserDTO;
import model.business.po.UserPO;
import model.common.dto.RequestDTO;
import model.common.dto.ResponseDTO;
import org.springframework.stereotype.Service;
import service.UserService;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserManage userManage;

    public ResponseDTO<UserDTO> get(RequestDTO<Long> request) {
        if (!checkNullForRequest(request)) {
            return ResponseDTO.fail("请求参数为空");
        }
        Long userId = request.getData();
        UserPO user = userManage.get(userId);
        if (user == null) {
            return ResponseDTO.fail("用户信息为空");
        }
        return ResponseDTO.success(getUserDTO(user));
    }


    private boolean checkNullForRequest(RequestDTO<?> request) {
        return request == null || request.getData() == null ? false : true;
    }


    /**
     * po -> dto
     *
     * @param po
     * @return
     */
    private UserDTO getUserDTO(UserPO po) {
        UserDTO dto = new UserDTO();
        dto.setId(po.getId());
        dto.setPwd(po.getPwd());
        dto.setUsername(po.getUsername());
        return dto;
    }
}
