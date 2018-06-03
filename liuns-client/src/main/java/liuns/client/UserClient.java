package liuns.client;

import liuns.interfacer.UserService;
import liuns.interfacer.model.business.UserDTO;
import liuns.interfacer.model.common.RequestDTO;
import liuns.interfacer.model.common.ResponseDTO;

import javax.annotation.Resource;

public class UserClient implements UserService {

    private UserService userService;

    @Override
    public ResponseDTO<UserDTO> get(RequestDTO<Long> request) {
        return userService.get(request);
    }

    @Override
    public ResponseDTO<Long> insert(RequestDTO<UserDTO> request) {
        return userService.insert(request);
    }
}
