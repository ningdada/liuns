package liuns.interfacer.model.business;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 2460744997384716269L;

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
