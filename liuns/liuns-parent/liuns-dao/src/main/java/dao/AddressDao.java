package dao;

import model.business.po.AddressPO;

public interface AddressDao {

    /**
     * 新增地区
     *
     * @param address
     * @return
     */
    long insert(AddressPO address);

    /**
     * 根据id更新地区
     *
     * @param address
     */
    void update(AddressPO address);

    /**
     * 根据id获取地区
     *
     * @param id
     * @return
     */
    AddressPO get(long id);
}
