package com.elcom.plus.user.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.dto.request.*;
import com.elcom.plus.user.dto.response.*;
import com.elcom.plus.user.dto.response.PhoneGroupResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    Response findById(String id);

    Response updateUser(String id);
    UserResponse save(UserRequest request);

    Response findUserInfo(String phone, int userIdVisit, String userId, int creatorType, int videoId, int skip, int take);

    List<PackDataResponse> packOfData(String id);

    Response addAndDeleteSubscriber(String id, PackageData packageData);

    Response videoGift(String id, VideoRequest videoRequest);

    Response videoGiftComfirm(String id, VideoGiftConfirm videoGiftConfirm);

    Response buyVideo(String id, VideoRequest videoRequest);

    Response cancelVideo(String id, VideoRequest videoRequest);

    Response checkPackage(String id);

    List<NotificationResponse> getNotification(int id);

    Response saveNotificationStatus(int id, NotificationRequest notificationRequest);

    String getIdHeader(String header);

    Response followUser(String phone, String id, FollowUser followUser);

    List<FollowsUser> followsByUser(String phone, int id, int userId, int creatorType, int type);

    Response videoWait(String phone, int id, VideoWait videoWait);

    Response getListNameVideoWait(String phone, int type);

    Response addPhoneVideoWait(String phone, NameVideoWait nameVideoWait);

    Response phoneSettingDel(String phone, PhoneSetting phoneSetting);

    Response groupSettingAdd(String phone, PhoneGroup phoneGroup);

    Response groupPhoneDel(String phone, GroupPhoneDel groupPhoneDel);

    List<PhoneGroupResponse> groupPhoneList(String phone, int ruleId);

    Response phoneSettingDetail(String phone, int ruleId, int type);

    Response uploadAvatar(String phone, String id, MultipartFile avatar) throws IOException;

    Response updateUsername(String phone, String id, String name);

    Response updatePassword(String phone, String id, ChangePasswordDto changePasswordDto);

    Response updatePhoneUser(String phone, String id, String phoneNew);

    List<BonusHistory> bonusHistory(String phone);
}
