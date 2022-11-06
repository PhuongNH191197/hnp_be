package com.elcom.plus.user.dao;

import com.elcom.plus.user.dto.request.PackDataResponse;
import com.elcom.plus.user.dto.request.VideoRequest;
import com.elcom.plus.user.dto.request.VideoWait;
import com.elcom.plus.user.dto.response.*;
import com.elcom.plus.user.entity.User;

import java.util.List;

public interface UserDao {
    User findByUsername(String username);

    User findUserInfo(int userVisitId, String userId, int creatorType);

    User findById(String id);

    ListMyVideo findVideoBuy(int userId, int videoId, String phone, int skip, int take);

    ListMyVideo findVideoGift(int userId, int videoId, String phone, int skip, int take);

    boolean insertVideoGift(String phone, String phoneReceive, int videoId);

    VideoRequest getUserSendGift(String phone, int videoId);

    boolean deleteVideoGift(String phone, String phoneReceive, int videoId);

    int checkOverdueTimeGift(String phone, String phoneReceive, int videoId);

    String getSupplier(String id);

    VideoDetailResponse findVideoById(int id, int userId);

    PackDataResponse checkPackage(String phone);

    List<PackDataResponse> packOfData(String phone);

    String getPathConfig(String path);

    String getPathCp(int id);

    boolean checkVideoInCollection(String phone, int videoId);

    List<NotificationResponse> getNotification(String phone, int status);

    boolean saveNotificationStatusDto(String device, int notificationId, int status);

    List<MyVideoResponse> videoUploadByUser(String phone, String userId, int creatorType, int skip, int take);

    boolean saveOrDeleteFollowUser(String phone, int id, int userId, int creatorType);

    List<FollowsUser> getListFollowUser(String phone, int id, int userId, int creatorType);

    List<FollowsUser> getListUserFollow(String phone, int id, int userId, int creatorType);

    boolean saveVideoWait(int type, int ruleId, int timeType, String yearStart, String yearEnd, String monthStart, String monthEnd,
                          String dayStart, String dayEnd, String hoursStart, String hoursEnd, String minuteStart, String minuteEnd,
                          String videoIds, int countVideoId, String phone);

    List<NameVideoWaitSetting> getListNameVideoWait(String phone, int type);

    boolean addPhoneVideoWait(String phone, String name, int type, String sub);

    boolean phoneSettingDel(String phone, int id, int type);

    boolean groupSettingAdd(String phone, int id, String phoneIn, String sub);

    boolean groupPhoneDel(String phone, int ruleId, int contactId);

    List<PhoneGroupResponse> groupPhoneList(String phone, int ruleId);

    VideoWait phoneSettingDetail(String phone, int ruleId, int type);

    boolean saveAvatar(String phone, String id, String filePathAvatar);

    boolean updateUsername(String phone, String id, String name);

    boolean updatePassword(String phone, String id, String passwordNewEncoder);

    boolean updatePhoneUser(String phone, String id, String phoneNew);

    List<BonusHistory> bonusHistory(String phone);

    VideoDetailResponse findVideoGiftById(int videoId, int userId, String phone);

    boolean updateMusic(int id, String phone);
}
