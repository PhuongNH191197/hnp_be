package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.entity.Address;
import com.elcom.plus.auth.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl extends AbstractDao implements AddressService {
    @Override
    public List<Address> getAllAddress() {
        return null;
    }

    @Override
    public Address getOneAddress() {
        return null;
    }

    @Override
    public boolean saveAddress() {
        return false;
    }

    @Override
    public boolean updateAddress() {
        return false;
    }

    @Override
    public boolean deleteAddress() {
        return false;
    }
}
