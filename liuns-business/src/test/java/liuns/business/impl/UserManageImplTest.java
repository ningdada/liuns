package liuns.business.impl;

import liuns.business.BaseTest;
import liuns.business.UserManage;
import liuns.model.business.po.UserPO;
import org.junit.Test;

import javax.annotation.Resource;

public class UserManageImplTest extends BaseTest {

    @Resource
    private UserManage userManage;

    @Test
    public void insert() {
        UserPO userInsert = new UserPO();
        userInsert.setId(2L);
        userInsert.setPwd("pwd");
        userInsert.setUsername("username");
        userManage.update(userInsert);
        System.out.println("success.");
    }

    @Test
    public void update() {
        UserPO userUpdate = new UserPO();
        userUpdate.setId(2003L);
        userUpdate.setUsername("刘宁");
        userUpdate.setPwd("pwd");
        userManage.insert(userUpdate);
        System.out.println("success..");
    }
}