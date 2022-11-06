package com.elcom.plus.user.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.dto.request.BlockPhone;
import com.elcom.plus.user.dto.request.FollowUser;
import com.elcom.plus.user.dto.response.UserBlockPhone;
import com.elcom.plus.user.dto.response.UserBlockResponse;

import java.util.List;

public interface BlockService {
    List<UserBlockResponse> blocks(String id, String phone);
    Response updateBlock(String id, String phone, FollowUser blockRequest);
    List<UserBlockPhone> blockPhoneData(String id, String phone);
    Response updateBlockPhone(String id, String phone, BlockPhone phoneBlock);
    Response checkUserBlock(String id, String phone, int userId, int creatorType);
}
