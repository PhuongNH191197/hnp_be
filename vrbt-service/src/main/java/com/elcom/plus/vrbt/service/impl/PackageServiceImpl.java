package com.elcom.plus.vrbt.service.impl;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.vrbt.dao.PackageDao;
import com.elcom.plus.vrbt.dto.request.ApsRequest;
import com.elcom.plus.vrbt.dto.response.ApsRegisterResponse;
import com.elcom.plus.vrbt.dto.response.PackDataResponse;
import com.elcom.plus.vrbt.service.PackageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {
    @Value("${domain.aps}")
    String apsUrl;
    private final PackageDao packageDao;
    private final RestTemplate restTemplate;

    public PackageServiceImpl(PackageDao packageDao, RestTemplate restTemplate) {
        this.packageDao = packageDao;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<PackDataResponse> packOfData() {
        return packageDao.packOfData();
    }

    @Override
    public Response registerPackData(Integer id, String phone, String username) {
        PackDataResponse packageData = packageDao.findById(id);
        ApsRequest request = mapApsRequest(packageData, phone, username);
        ApsRegisterResponse appResult = restTemplate.postForObject(apsUrl + "/app/register", request, ApsRegisterResponse.class);
        Assert.notNull(appResult, "Failed to register user. Please try again later");
        return new Response(appResult.getError_code(), appResult.getError_desc());
    }

    @Override
    public Response unRegisterPackData(Integer id, String phone, String username) {
        PackDataResponse packageData = packageDao.findById(id);
        ApsRequest request = mapApsRequest(packageData, phone, username);
        ApsRegisterResponse appResult = restTemplate.postForObject(apsUrl + "/app/unregister", request, ApsRegisterResponse.class);
        Assert.notNull(appResult, "Failed to register user. Please try again later");
        return new Response(appResult.getError_code(), appResult.getError_desc());
    }

    @Override
    public Response buyContent(Integer id, String phone, String username) {
        PackDataResponse packageData = packageDao.findById(id);
        ApsRequest request = mapApsRequest(packageData, phone, username);
        ApsRegisterResponse appResult = restTemplate.postForObject(apsUrl + "/app/buy-content", request, ApsRegisterResponse.class);
        Assert.notNull(appResult, "Failed to register user. Please try again later");
        return new Response(appResult.getError_code(), appResult.getError_desc());
    }

    @Override
    public Response cancelContent(Integer id, String phone, String username) {
        PackDataResponse packageData = packageDao.findById(id);
        ApsRequest request = mapApsRequest(packageData, phone, username);
        ApsRegisterResponse appResult = restTemplate.postForObject(apsUrl + "/app/cancel-content", request, ApsRegisterResponse.class);
        Assert.notNull(appResult, "Failed to register user. Please try again later");
        return new Response(appResult.getError_code(), appResult.getError_desc());
    }

    private ApsRequest mapApsRequest(PackDataResponse packData, String phone, String username) {
        ApsRequest request = new ApsRequest();
        request.setMsisdn(phone);
        request.setPackage_name(packData.getTitle());
        request.setDynamicSubserviceID(String.valueOf(packData.getId()));
        request.setUser(username != null ? username : phone);
        request.setPrice(String.valueOf(packData.getPrice()));
        return request;
    }
}
