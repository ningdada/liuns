package liuns.model.business.po;


import liuns.model.common.po.BasePO;

import java.io.Serializable;

/**
 * 地区
 */
public class AddressPO extends BasePO implements Serializable {

    private static final long serialVersionUID = 1767287279128379373L;

    /**
     * 父级地区id，第一级地区的父级地区id为0
     */
    private Long parentId;
    /**
     * 地区代码
     */
    private String addressCode;
    /**
     * 地区名称
     */
    private String addressName;

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
