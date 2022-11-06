package com.elcom.plus.mecall.dao;

import com.elcom.plus.mecall.dto.request.video.UpdateLikeRequest;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.category.MyVideoResponse;
import com.elcom.plus.mecall.dto.response.category.VideoDetailResponse;

import java.util.List;

public interface VideoDao {
    ItemVideoResponse findVideoById(String phone, int userId, int id);

    void increaseView(int videoId);

    int updateLikeVideo(String phone, int id, int videoId);

    List<MyVideoResponse> findByUserId(int userId, int skip, int take);
}
