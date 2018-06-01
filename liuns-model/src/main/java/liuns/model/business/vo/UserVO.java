package liuns.model.business.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

    private static final long serialVersionUID = 3449818844111815678L;

    private Long id;

    private String username;

    private String pwd;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
