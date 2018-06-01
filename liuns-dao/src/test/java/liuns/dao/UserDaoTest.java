package liuns.dao;

import liuns.model.business.po.UserPO;
import org.junit.Test;

import javax.annotation.Resource;

public class UserDaoTest extends BaseTest {

    @Resource
    private UserDao userDao;

    @Test
    public void insert() {
        UserPO user = new UserPO();
        user.setId(1L);
        user.setPwd("pwd");
        user.setUsername("username");
        userDao.insert(user);
        System.out.println("success.");
    }
}