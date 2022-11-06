package com.elcom.plus.vrbt.dao;

import com.elcom.plus.vrbt.dto.response.PackDataResponse;

import java.util.List;

public interface PackageDao {
    List<PackDataResponse> packOfData();

    PackDataResponse findById(Integer id);
}
