package com.elcom.plus.mecall.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.mecall.dto.request.video.UpdateLikeRequest;
import com.elcom.plus.mecall.dto.request.video.ViewRequest;
import com.elcom.plus.mecall.dto.response.category.MyVideoResponse;

import java.util.List;

public interface VideoService {
    Response findById(String phone, int userId, int id, Integer category);

    Response increaseView(String phone, int userId, ViewRequest viewRequest);

    Response updateLikeVideo(String phone, int id, UpdateLikeRequest request);

    List<MyVideoResponse> findByUserId(int userId, int skip, int take);
}
