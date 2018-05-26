package business.impl;

import business.BaseTest;
import business.UserManage;
import model.business.po.UserPO;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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
        userUpdate.setId(2L);
        userUpdate.setUsername("username");
        userUpdate.setPwd("pwd");
        userManage.insert(userUpdate);
        System.out.println("success..");
    }
}