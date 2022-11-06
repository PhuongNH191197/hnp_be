package com.elcom.plus.user.dao;

import com.elcom.plus.user.dto.request.UploadVideoEndUser;
import com.elcom.plus.user.dto.response.ListMyVideo;
import com.elcom.plus.user.dto.response.MyVideoResponse;

import java.util.List;

public interface VideoDao {
    boolean saveVideoUpload(UploadVideoEndUser uploadVideoEndUser);

    ListMyVideo getVideoUploadByUserId(int userId, String phone, int videoId , int skip, int take);

    ListMyVideo getVideoLikeByUserID(int userId, int videoId, int skip, int take, String phone);

    boolean deleteVideoUpload(int id, String phone, String videoId);
}
