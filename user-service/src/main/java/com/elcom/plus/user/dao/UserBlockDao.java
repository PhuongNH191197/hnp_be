package com.elcom.plus.user.dao;

import com.elcom.plus.user.dto.request.BlockPhone;
import com.elcom.plus.user.dto.response.CheckUserBlock;
import com.elcom.plus.user.dto.response.UserBlockPhone;
import com.elcom.plus.user.dto.response.UserBlockResponse;

import java.util.List;

public interface UserBlockDao {
    List<UserBlockResponse> findUserBlock(String id, String phone);
    Integer updateBlock(Long id, String phone, int userId, int creatorType);
    List<UserBlockPhone> findPhoneBlock(String id, String phone);
    Integer updateBlockPhone(Long id, String phone, BlockPhone phoneBlock);
    CheckUserBlock checkUserBlock(Long id, String phone, int userId, int creatorType);
}
