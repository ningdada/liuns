package liuns.business.impl;

import liuns.business.AddressManage;
import liuns.dao.AddressDao;
import liuns.model.business.po.AddressPO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AddressManageImpl implements AddressManage {

    @Resource
    private AddressDao addressDao;

    @Override
    public long insert(AddressPO address) {
        if (address == null) {
            throw new NullPointerException();
        }
        return addressDao.insert(address);
    }

    @Override
    public void update(AddressPO address) {
        if (address == null || address.getId() == null) {
            throw new NullPointerException();
        }
        addressDao.update(address);
    }

    @Override
    public AddressPO get(long id) {
        return addressDao.get(id);
    }


}
