package com.elcom.plus.auth.service;

import com.elcom.plus.auth.entity.Address;
import com.elcom.plus.auth.entity.Post;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddress();
    Address getOneAddress();
    boolean saveAddress();
    boolean updateAddress();
    boolean deleteAddress();
}
