package business;

import model.business.po.UserPO;

public interface UserManage {

    /**
     * 新增用户
     *
     * @param user
     * @return ID 用户id
     */
    long insert(UserPO user);

    /**
     * 根据id更新用户
     *
     * @param user
     */
    void update(UserPO user);

    /**
     * 根据用户id获取用户
     *
     * @param id
     * @return
     */
    UserPO get(long id);

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    UserPO login(UserPO user);

}
