package com.elcom.plus.vrbt.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.vrbt.dto.response.PackDataResponse;

import java.util.List;

public interface PackageService {
    List<PackDataResponse> packOfData();
    Response registerPackData(Integer id, String phone, String username);
    Response unRegisterPackData(Integer id, String phone, String username);
    Response buyContent(Integer id, String phone, String username);
    Response cancelContent(Integer id, String phone, String username);
}
