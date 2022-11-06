package com.elcom.plus.user.controller;

import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.response.HeaderResponse;
import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.response.ResponseUtil;
import com.elcom.plus.user.dto.request.*;
import com.elcom.plus.user.dto.response.*;
import com.elcom.plus.user.dto.response.PhoneGroupResponse;
import com.elcom.plus.user.service.BlockService;
import com.elcom.plus.user.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private BlockService blockService;

    @GetMapping
    public ResponseEntity<Response> getUserProfile(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ==========API GET: /rest/api/users ==========", phone);
        logger.info("[{}] user id: {}", phone, id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Response> updateUser(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ==========API PUT: /rest/api/users ==========", phone);
        logger.info("[{}] ==========user id==========: {}", phone, id);
        return ResponseEntity.ok(userService.updateUser(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest user) {
        logger.info("[{}] ==========API POST: /rest/api/users ==========", user.getUsername());
        logger.info("[{}] ========== input data ==========: {}", user.getUsername(), user);
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("/info/get-user-by-id")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> getUserInfo(HttpServletRequest request, @RequestParam(name = "userId") String userId, @RequestParam(name = "creatorType") Integer creatorType, @RequestParam(name = "videoId") Integer videoId, @RequestParam("skip") int skip, @RequestParam("take") int take) {
        logger.info("========== request ==========: {}", request);
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int id = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            id = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}] ========== API GET: /rest/api/users/info/get-user-by-id ==========", phone);
        logger.info("[{}] ========== input data ==========: userId: {}, creatorType: {}, videoId: {}, skip: {}, take: {}", phone, userId, creatorType, videoId, skip, take);
        Response data = userService.findUserInfo(phone, id, userId, creatorType, videoId, skip, take);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/info/follow")
    public ResponseEntity<Response> followUser(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody FollowUser followUser) {
        logger.info("[{}] ========== API PUT: /rest/api/users/info/follow ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, followUser);
        Response data = userService.followUser(phone, id, followUser);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/info/follows-by-user")
    public ResponseEntity<ListResponse> followsByUser(HttpServletRequest request, @RequestParam(name = "userId") Integer userId, @RequestParam(name = "creatorType") Integer creatorType, @RequestParam(name = "type") Integer type) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int id = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            id = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}] ========== API GET: /rest/api/users/info/follows-by-user ==========", phone);
        logger.info("[{}] ========== input data ==========: userId: {}, creatorType: {}, type: {}", phone, userId, creatorType, type);
        List<FollowsUser> data = userService.followsByUser(phone, id, userId, creatorType, type);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/pack-data")
    public ResponseEntity<ListResponse> packOfData(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API GET: /rest/api/users/pack-data ==========", phone);
        logger.info("[{}] ========== not input data ==========: ", phone);
        List<PackDataResponse> data = userService.packOfData(id);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/info/block-by-user")
    public ResponseEntity<ListResponse> blocks(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API GET: /rest/api/users/info/block-by-user ==========", phone);
        logger.info("[{}] ========== not input data ==========", phone);
        List<UserBlockResponse> data = blockService.blocks(id, phone);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @PutMapping("/setting/block-user")
    public ResponseEntity<Response> updateBlocks(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody FollowUser blockRequest) {
        logger.info("[{}] ========== API PUT: /rest/api/users/info/block-user ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, blockRequest);
        Response data = blockService.updateBlock(id, phone, blockRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/check/user-block")
    public ResponseEntity<Response> checkUserBlock(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestParam(name = "userId") int userId, @RequestParam(name = "creatorType") int creatorType) {
        logger.info("[{}] ========== API PUT: /rest/api/users/check/user-block ==========", phone);
        logger.info("[{}] ========== input data ==========: userId: {}, creatorType: {}", phone, userId, creatorType);
        Response data = blockService.checkUserBlock(id, phone, userId, creatorType);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/info/block-phone-data")
    public ResponseEntity<ListResponse> blockPhoneData(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API PUT: /rest/api/users/info/block-phone-data ==========", phone);
        logger.info("[{}] ========== not input data ==========", phone);
        List<UserBlockPhone> data = blockService.blockPhoneData(id, phone);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @PostMapping("/setting/block-phone")
    public ResponseEntity<Response> updateBlockPhone(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody BlockPhone blockPhone) {
        logger.info("[{}] ========== API PUT: /rest/api/users/setting/block-phone ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, blockPhone);
        Response data = blockService.updateBlockPhone(id, phone, blockPhone);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/pack-data/action")
    public ResponseEntity<Response> addAndDeleteSubscriber(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody PackageData packageData) {
        logger.info("[{}] ========== API POST: /rest/api/users/pack-data/action ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, packageData);
        Response data = userService.addAndDeleteSubscriber(id, packageData);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/video/gift")
    public ResponseEntity<Response> videoGift(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody VideoRequest videoRequest) {
        logger.info("[{}] ========== API POST: /rest/api/users/video/gift ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoRequest);
        Response data = userService.videoGift(id, videoRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/video/buy")
    public ResponseEntity<Response> buyVideo(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody VideoRequest videoRequest) {
        logger.info("[{}] ========== API POST: /rest/api/users/video/buy ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoRequest);
        Response data = userService.buyVideo(id, videoRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/video/cancel")
    public ResponseEntity<Response> cancelVideo(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody VideoRequest videoRequest) {
        logger.info("[{}] ========== API POST: /rest/api/users/video/cancel ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoRequest);
        Response data = userService.cancelVideo(id, videoRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/pack-data/check")
    public ResponseEntity<Response> checkPackage(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API GET: /rest/api/users/pack-data/check ==========", phone);
        logger.info("[{}] ========== not input data ==========", phone);
        Response data = userService.checkPackage(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/video/gift-confirm")
    public ResponseEntity<Response> checkPackage(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody VideoGiftConfirm videoGiftConfirm) {
        logger.info("[{}] ========== API POST: /rest/api/users/video/gift-confirm ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoGiftConfirm);
        Response data = userService.videoGiftComfirm(id, videoGiftConfirm);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity<ListResponse> getNotification(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}] ========== API GET: /rest/api/users/notification ==========", phone);
        logger.info("[{}] ========== not input data ==========", phone);
        List<NotificationResponse> data = userService.getNotification(userId);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @PutMapping("/notification/action")
    public ResponseEntity<Response> saveNotificationStatus(HttpServletRequest request, @RequestBody NotificationRequest notificationRequest) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}] ========== API PUT: /rest/api/users/notification/action ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, notificationRequest);
        Response data = userService.saveNotificationStatus(userId, notificationRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/setting/phone-setting-final")
    public ResponseEntity<Response> videoWait(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody VideoWait videoWait) {
        logger.info("[{}] ========== API POST: /rest/api/users/setting/phone-setting-final ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoWait);
        Response data = userService.videoWait(phone, Integer.valueOf(id), videoWait);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/setting/get-list-main-data")
    public ResponseEntity<Response> getVideoWait(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestParam("type") Integer type) {
        logger.info("[{}] ========== API GET: /rest/api/users/setting/get-list-main-data ==========", phone);
        logger.info("[{}] ========== input data ==========: type: {}", phone, type);
        Response data = userService.getListNameVideoWait(phone, type);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/setting/phone-setting-add")
    public ResponseEntity<Response> addPhoneVideoWait(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody NameVideoWait nameVideoWait) {
        logger.info("[{}] ========== API POST: /rest/api/users/setting/phone-setting-add ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, nameVideoWait);
        Response data = userService.addPhoneVideoWait(phone, nameVideoWait);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/setting/group-phone-add")
    public ResponseEntity<Response> groupSettingAdd(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody PhoneGroup phoneGroup) {
        logger.info("[{}] ========== API POST: /rest/api/users/setting/group-phone-add ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, phoneGroup);
        Response data = userService.groupSettingAdd(phone, phoneGroup);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/setting/phone-setting-del")
    public ResponseEntity<Response> phoneSettingDel(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody PhoneSetting phoneSetting) {
        logger.info("[{}] ========== API POST: /rest/api/users/setting/phone-setting-del ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, phoneSetting);
        Response data = userService.phoneSettingDel(phone, phoneSetting);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/setting/group-phone-del")
    public ResponseEntity<Response> groupPhoneDel(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody GroupPhoneDel groupPhoneDel) {
        logger.info("[{}] ========== API POST: /rest/api/users/setting/group-phone-del ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, groupPhoneDel);
        Response data = userService.groupPhoneDel(phone, groupPhoneDel);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/setting/group-phone-list")
    public ResponseEntity<ListResponse> groupPhoneList(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestParam("id") Integer ruleId) {
        logger.info("[{}] ========== API GET: /rest/api/users/setting/group-phone-list ==========", phone);
        logger.info("[{}] ========== input data ==========: id: {}, phone: {}", phone, ruleId, phone);
        List<PhoneGroupResponse> data = userService.groupPhoneList(phone, ruleId);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/setting/phone-setting-detail")
    public ResponseEntity<Response> phoneSettingDetail(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestParam("id") Integer ruleId, @RequestParam("type") Integer type) {
        logger.info("[{}] ========== API GET: /rest/api/users/setting/phone-setting-detail ==========", phone);
        logger.info("[{}] ========== input data ==========: id: {}, phone: {}, type: {}", phone, ruleId, phone, type);
        Response data = userService.phoneSettingDetail(phone, ruleId, type);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/info/upload-avatar")
    public ResponseEntity<Response> uploadAvatar(@RequestParam(name = "avatar") MultipartFile avatar, @RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) throws IOException {
        logger.info("[{}] ========== API POST: /rest/api/users/info/upload-avatar ==========", phone);
        logger.info("[{}] ========== input data ==========: phone: {}, id: {}, avatar: {}", phone, phone, id, avatar);
        Response data = userService.uploadAvatar(phone, id, avatar);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/info/change-name")
    public ResponseEntity<Response> updateUsername(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody UsernameDto usernameDto) {
        logger.info("[{}] ========== API POST: /rest/api/users/info/change-name ==========", phone);
        logger.info("[{}] ========== input data ==========:id: {}, phone: {}, name: {}", phone, id, phone, usernameDto.getName());
        Response data = userService.updateUsername(phone, id, usernameDto.getName());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/info/change-password")
    public ResponseEntity<Response> updatePassword(@RequestBody ChangePasswordDto changePasswordDto, @RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API POST: /rest/api/users/info/change-password ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, changePasswordDto);
        Response data = userService.updatePassword(phone, id, changePasswordDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/info/change-phone")
    public ResponseEntity<Response> updatePhoneUser(@RequestHeader(value = "id") String id, @RequestHeader(value = "phone") String phone, @RequestBody PhoneUserDto phoneUserDto) {
        logger.info("[{}] ========== API POST: /rest/api/users/info/change-phone ==========", phone);
        logger.info("[{}] ========== input data ==========:id: {}, phone: {}, phoneNew: {}", phone, id, phone, phoneUserDto.getPhone());
        Response data = userService.updatePhoneUser(phone, id, phoneUserDto.getPhone());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/bonus/history")
    public ResponseEntity<ListResponse> bonusHistory(@RequestHeader(value = "phone") String phone) {
        logger.info("[{}] ========== API GET: /rest/api/users/info/change-password ==========", phone);
        logger.info("[{}] ========== input data ==========: phone: {}", phone, phone);
        List<BonusHistory> data = userService.bonusHistory(phone);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }
}
