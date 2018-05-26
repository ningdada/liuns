package model.business.po;

import model.common.po.BasePO;

import java.io.Serializable;

/**
 * 用户
 */
public class UserPO extends BasePO implements Serializable {

    private static final long serialVersionUID = 3758641678884304726L;

    private String username;

    private String pwd;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
