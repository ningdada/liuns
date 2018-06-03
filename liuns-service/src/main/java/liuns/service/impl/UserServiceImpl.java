package liuns.service.impl;

import liuns.business.UserManage;
import liuns.interfacer.UserService;
import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;
import liuns.model.business.po.UserPO;
import org.springframework.stereotype.Service;

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
        try {
            UserPO user = userManage.get(userId);
            if (user == null) {
                return ResponseDTO.fail("用户信息为空");
            }
            return ResponseDTO.success(getUserDTO(user));
        } catch (Exception e) {
            return ResponseDTO.fail(e.getMessage());
        }
    }

    @Override
    public ResponseDTO<Long> insert(RequestDTO<UserDTO> request) {
        if (!checkNullForRequest(request)) {
            return ResponseDTO.fail("请求参数为空");
        }
        long userId = 0;
        try {
            userId = userManage.insert(getUserPO(request.getData()));
            return ResponseDTO.success(userId);
        } catch (Exception e) {
            return ResponseDTO.fail(e.getMessage());
        }
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

    private UserPO getUserPO(UserDTO dto) {
        UserPO po = new UserPO();
        po.setId(dto.getId());
        po.setUsername(dto.getUsername());
        po.setPwd(dto.getPwd());
        return po;
    }
}
