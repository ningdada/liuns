package liuns.interfacer.model.business;

import java.io.Serializable;

/**
 * 地区
 */
public class AddressDTO implements Serializable {

    private static final long serialVersionUID = 3665386524937909265L;

    private Long id;

    private Long parentId;

    private String addressCode;

    private String addressName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}
