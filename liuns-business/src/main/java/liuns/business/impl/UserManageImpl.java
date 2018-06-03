package liuns.business.impl;

import liuns.business.UserManage;
import liuns.dao.UserDao;
import liuns.model.business.po.UserPO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserManageImpl implements UserManage {

    @Resource
    private UserDao userDao;

    @Transactional
    public long insert(UserPO user) {
        if (user == null) throw new NullPointerException();
        int result = userDao.insert(user);
        if (result == 1) {
            return user.getId();
        }
        return 0L;
    }

    @Transactional
    public void update(UserPO user) {
        if (user == null || user.getId() == null) throw new NullPointerException();
        userDao.update(user);
    }

    public UserPO get(long id) {
        return userDao.get(id);
    }

    @Override
    public UserPO login(UserPO user) {
        if (user == null || user.getUsername() == null || user.getPwd() == null) {
            return null;
        }

        UserPO currentUser = userDao.selectBy(user);
        if (currentUser == null || !Objects.equals(user.getPwd(), currentUser.getPwd())) {
            return null;
        }
        return currentUser;
    }

}
