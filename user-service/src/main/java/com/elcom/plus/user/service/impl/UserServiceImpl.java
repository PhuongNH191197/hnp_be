package com.elcom.plus.user.service.impl;

import com.elcom.plus.common.util.file.FileUtil;
import com.elcom.plus.user.controller.VideoController;
import com.elcom.plus.user.dto.response.PhoneGroupResponse;
import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.constant.ResponseMessageAPI;
import com.elcom.plus.common.util.request.ActionInforRequest;
import com.elcom.plus.common.util.request.SmsRequest;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.dao.CommonDao;
import com.elcom.plus.user.dao.UserDao;
import com.elcom.plus.user.dto.request.*;
import com.elcom.plus.user.dto.response.*;
import com.elcom.plus.user.entity.User;
import com.elcom.plus.user.service.UserService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private UserDao userDao;
    private CommonDao commonDao;

    @Value("${domain.file}")
    protected String domainFile;

    public UserServiceImpl(UserDao userDao, CommonDao commonDao) {
        this.userDao = userDao;
        this.commonDao = commonDao;
    }

    private static ConnectionManager connectionManager = new ConnectionManager();
    ResponseMessageAPI responseMessageAPI = new ResponseMessageAPI();

    @Value("${server.rest.api.path}")
    private String api_server;

    @Override
    public Response findById(String id) {
        Response response = new Response();
        User user = userDao.findById(id);
        String message = commonDao.getMessage("USER", 9002);
        if (Objects.isNull(user)) {
            logger.info("[]========== User not exists ==========");
            message = commonDao.getMessage("USER", 8002);
            response.setCode(1);
            response.setMessage(message);
            return response;
        }
        response.setCode(0);
        response.setMessage(message);
        response.setData(new UserResponse(user));
        logger.info("[{}]========== id:message => [{}: {}] ==========", user.getPhone(), id, message);
        logger.info("========== Phone user ==========: " + user.getPhone());
        logger.info("==================End [{}]==================", user.getPhone());
        return response;
    }

    @Override
    public Response updateUser(String id) {

        return null;
    }

    @Override
    public UserResponse save(UserRequest request) {
        return null;
    }

    @Override
    public Response findUserInfo(String phone, int userIdVisit, String userId, int creatorType, int videoId, int skip, int take) {
        Response response = new Response();
        VideoDetailResponse videoDetail = null;
        logger.info("[{}]==========start get user info==========", phone);
        logger.info("[{}]========== get info user by id ==========: {}", phone, userId);
        User user = userDao.findUserInfo(userIdVisit, userId, creatorType);
        logger.info("[{}]========== data response info user by id ==========: {}", phone, user);
        logger.info("[{}]========== get list video of user by id ==========: {}", phone, userId);
        List<MyVideoResponse> listVideoData = userDao.videoUploadByUser(phone, userId, creatorType, skip, take);
        logger.info("[{}]========== list video of user by id ==========: {}", phone, listVideoData);
        if (videoId > 0) {
            videoDetail = userDao.findVideoById(videoId, userIdVisit);
        }
        if (Objects.isNull(user)) {
            response.setCode(1);
            response.setMessage(commonDao.getMessage("USER", 8002));
            return response;
        }
        InfoUser infoUser = new InfoUser();
        infoUser.setUsername(user.getUsername());
        infoUser.setAvatar(user.getAvatar());
        infoUser.setFollowers(user.getFollowers());
        infoUser.setFollowings(user.getFollowings());
        infoUser.setStatus(user.getStatus());
        infoUser.setListVideoData(listVideoData);
        infoUser.setVideoDetail(videoDetail);
        logger.info("[{}]========== info user ==========: {}", phone, listVideoData);
        response.setCode(0);
        response.setMessage(commonDao.getMessage("USER", 9002));
        response.setData(infoUser);
        logger.info("[{}]==========End get user info==========", phone);
        return response;
    }

    @Override
    public List<PackDataResponse> packOfData(String id) {
        User user = userDao.findById(id);
        List<PackDataResponse> result = new ArrayList<>();
        String phone = user.getPhone();
        try {
            logger.info("[{}]========== start get list pack ==========", phone);
            result = userDao.packOfData(phone);
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error get list pack ==========: ", e);
            e.printStackTrace();
        }
        logger.info("[{}]========== End get list pack ==========", phone);
        return result;
    }

    @Override
    public Response addAndDeleteSubscriber(String id, PackageData packageData) {
        Response response = new Response();
        User user = userDao.findById(id);
        Connection con = null;
        CallableStatement cs = null;
        String phone = user.getPhone();
        try {
            String username = user.getUsername();
            String path = "";
            if (packageData.getId() == 0) {
                logger.info("[{}]========== File id is required ==========", phone);
                return response;
            } else if (packageData.getName().isEmpty()) {
                logger.info("[{}]========== File name is required ==========", phone);
                return response;
            } else if (packageData.getTitle().isEmpty()) {
                logger.info("[{}]========== File title is required ==========", phone);
                return response;
            } else if (packageData.getPrice().isEmpty()) {
                logger.info("[{}]========== File price is required ==========", phone);
                return response;
            } else if (packageData.getDescription().isEmpty()) {
                logger.info("[{}]========== File description is required ==========", phone);
                return response;
            } else if (packageData.getExpired().isEmpty()) {
                logger.info("[{}]========== File expired is required ==========", phone);
                return response;
            } else if (phone == null) {
                response.setCode(8009);
                response.setMessage(commonDao.getMessage("USER", 8009));
                return response;
            } else if (packageData.getStatus() == 1) {
                logger.info("[{}] ==========start unregister subscriber ==========", phone);
                path = api_server + "/app/unregister";
                logger.info("[{}] ========== API POST: {} ==========", phone, path);
            } else {
                logger.info("[{}] ========== start register subscriber ==========", phone);
                path = api_server + "/app/register";
                logger.info("[{}] ========== API POST: {} ==========", phone, path);
            }
            VideoDetailResponse videoDetail = userDao.findVideoById(packageData.getId(), Integer.valueOf(id));
            logger.info("[{}] ========== video detail ==========: {}", phone, videoDetail);
            PackDataResponse checkPackage = userDao.checkPackage(user.getPhone());
            logger.info("[{}] ========== checkPackage ==========: {}", phone, checkPackage);
            RegisterSubscribers registerSubscribers = new RegisterSubscribers();
            registerSubscribers.setMsisdn(phone);
            registerSubscribers.setPackage_name(packageData.getName());
            registerSubscribers.setDynamicSubserviceID(String.valueOf(packageData.getId()));
            registerSubscribers.setUser(username);
            registerSubscribers.setPrice(String.valueOf(packageData.getPrice()));
            registerSubscribers.setSupplier(videoDetail.getSupplier());
            logger.info("[{}] ========== subscribers input ==========: {}", phone, registerSubscribers);
            PackageReturn packageReturn = getDataServerVebt(path, registerSubscribers);
            logger.info("[{}] ========== subscribers output ==========: {}", phone, packageReturn);
            if (packageReturn != null) {
                Integer code = packageReturn.getError_code();
                if (code == 1000) {
                    String sms = "";
                    if ("MECALL_GOIVIP".equalsIgnoreCase(packageData.getName())) {
                        sms = "YV";
                    }
                    if ("MECALL_GOICOBAN".equalsIgnoreCase(packageData.getName())) {
                        sms = "Y";
                    }
                    String str_message = String.format(commonDao.getMessage("USER", 7000), packageData.getTitle(), sms, packageData.getPrice());
                    response.setCode(0);
                    if (!Objects.isNull(checkPackage) && "MECALL_GOIVIP".equalsIgnoreCase(packageData.getName()) && "MECALL_GOICOBAN".equalsIgnoreCase(checkPackage.getName())) {
                        str_message = String.format(commonDao.getMessage("USER", 7001), checkPackage.getTitle(), checkPackage.getPrice());
                    }
                    if (!Objects.isNull(checkPackage) && "MECALL_GOIVIP".equalsIgnoreCase(packageData.getName()) && "MECALL_GOIKHUYENMAI".equalsIgnoreCase(checkPackage.getName())) {
                        str_message = String.format(commonDao.getMessage("USER", 7002), checkPackage.getTitle(), checkPackage.getPrice());
                    }
                    if (!Objects.isNull(checkPackage) && !"MECALL_GOIVIP".equalsIgnoreCase(packageData.getName())) {
                        str_message = commonDao.getMessage("USER", 1007);
                    }
                    response.setMessage(str_message);
                    return response;
                }
                if (code == 18) {
                    String str_message = String.format(commonDao.getMessage("USER", 7003), packageData.getTitle());
                    response.setCode(code);
                    response.setMessage(str_message);
                    return response;
                }
                if (code == 1001 || code == 1002) {
                    response.setCode(0);
                    response.setMessage(commonDao.getMessage("USER", code));
                } else {
                    response.setCode(code);
                    response.setMessage(commonDao.getMessage("USER", code));
                }
            }

        } catch (Exception e) {
            logger.error("[" + phone + "] Error: ", e);
            e.printStackTrace();
        }
        logger.info("================== End [{}] ==================", phone);
        return response;
    }

    public static PackageReturn getDataServerVebt(String path, RegisterSubscribers registerSubscribers) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<RegisterSubscribers> request = new HttpEntity<>(registerSubscribers);
            PackageReturn packageReturn = restTemplate.postForObject(path, request, PackageReturn.class);
            return packageReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response videoGift(String id, VideoRequest videoRequest) {
        Response response = new Response();
        User user = userDao.findById(id);
        response.setCode(1);
        response.setMessage(commonDao.getMessage("USER", 1));
        String phone = user.getPhone();
        if (phone.isEmpty()) {
            response.setCode(8009);
            response.setMessage(commonDao.getMessage("USER", 8009));
            return response;
        } else if (videoRequest.getVideoId() == null) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        } else if (videoRequest.getMsisdnReceiver() == null) {
            logger.info("[{}]========== Field msisdnReceiver is required ==========", phone);
            return response;
        }
//        response.setMessage(responseMessageAPI.getMessage("ERROR_CODE_1"));
        try {
            logger.info("[{}] ==========start video gift==========", phone);
//            int code = checkSim(videoRequest.getMsisdnReceiver());
//            System.out.println("code: " + code);
//            if (code != 0) {
//                String message_str = "ERROR_CODE_" + code;
//                response.setCode(code);
//                response.setMessage(responseMessageAPI.getMessage(message_str));
//                return response;
//            }
            VideoDetailResponse videoDetail = userDao.findVideoById(videoRequest.getVideoId(), Integer.valueOf(id));
            logger.info("[{}] ========== videoRequest ==========: {}", phone, videoRequest);
            String nameVideo = CollectionsUtil.removeAccentSms(videoDetail.getTitle());
            if (videoRequest.getMsisdnReceiver().equalsIgnoreCase(user.getPhone())) {
                String message = commonDao.getMessage("USER", 8000);
                response.setCode(8000);
                response.setMessage(message);
                logger.info("[{}] ========== message ==========: {}", phone, message);
                return response;
            }

            if (userDao.checkVideoInCollection(videoRequest.getMsisdnReceiver(), videoRequest.getVideoId())) {
                response.setCode(8001);
                String str_message = String.format(commonDao.getMessage("USER", 8001), videoRequest.getMsisdnReceiver());
                response.setMessage(str_message);
                logger.info("[{}]========== message ==========: {}", phone, str_message);
                return response;
            }

            // SMS Builder
//            ActionInforRequest actionInforRequest = new ActionInforRequest();
//            actionInforRequest.setVideoName(videoDetail.getTitle());
//            actionInforRequest.setVideoId(String.valueOf(videoRequest.getVideoId()));
//            actionInforRequest.setVideoProvider(videoDetail.getSupplier());
//            actionInforRequest.setGifterMsisdn(user.getPhone());
//            SmsRequest smsRequest = new SmsRequest();
//            smsRequest.setAction("TANG_GIFT_GIFTEE");
//            smsRequest.setCondition(0);
//            smsRequest.setActionInfo(actionInforRequest);
//            smsRequest.setUnaccented(true);
//            System.out.println("smsRequest1: " + smsRequest);
//            String content = CollectionsUtil.getMessageSms(smsRequest);
//            System.out.println("content: " + content);
            PackDataResponse checkPackage = userDao.checkPackage(videoRequest.getMsisdnReceiver());
            logger.info("[{}]========== checkPackage ==========: {}", phone, checkPackage);
            long price = videoDetail.getPrice() + 20000;
            String contentFrom = "";
            if (!Objects.isNull(checkPackage)) {
                contentFrom = String.format(commonDao.getMessage("USER", 7004), nameVideo, videoRequest.getVideoId(), videoDetail.getSupplier(), videoRequest.getMsisdnReceiver(), videoDetail.getPrice());
                if ("MECALL_GOIVIP".equalsIgnoreCase(checkPackage.getName())) {
                    contentFrom = String.format(commonDao.getMessage("USER", 7005), nameVideo, videoRequest.getVideoId(), videoDetail.getSupplier(), videoRequest.getMsisdnReceiver());
                }
            } else {
                contentFrom = String.format(commonDao.getMessage("USER", 7006), nameVideo, videoRequest.getVideoId(), videoDetail.getSupplier(), videoRequest.getMsisdnReceiver(), String.valueOf(price), videoDetail.getPrice());
            }
            logger.info("[{}]========== contentFrom ==========: {}", phone, contentFrom);
            String content = String.format(commonDao.getMessage("USER", 7007), nameVideo, videoRequest.getVideoId(), videoDetail.getSupplier(), user.getPhone());
            userDao.insertVideoGift(user.getPhone(), videoRequest.getMsisdnReceiver(), videoRequest.getVideoId());
            sendSms(user.getPhone(), contentFrom);
            if (sendSms(videoRequest.getMsisdnReceiver(), content)) {
                response.setCode(0);
                String message = String.format(commonDao.getMessage("USER", 7008), videoRequest.getMsisdnReceiver());
                response.setMessage(message);
                logger.info("[{}]========== video gift done ==========", phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[{}]========== Error ==========: ", phone, e);
        }
        logger.info("[{}]================== End video gift ==================", phone);
        return response;
    }

    @Override
    public Response videoGiftComfirm(String id, VideoGiftConfirm videoGiftConfirm) {
        Response response = new Response();
        User user = userDao.findById(id);
        String phone = user.getPhone();
        if (videoGiftConfirm.getVideoId() == null) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        } else if (videoGiftConfirm.getType() == null) {
            logger.info("[{}]========== Field type is required ==========", phone);
            return response;
        }
        try {
            logger.info("[{}]========== start comfirm video gift ==========", phone);
            VideoRequest data = userDao.getUserSendGift(user.getPhone(), videoGiftConfirm.getVideoId());
            logger.info("[{}]========== data user send gift ==========: {}", phone, data);
            VideoDetailResponse videoDetail = userDao.findVideoById(videoGiftConfirm.getVideoId(), Integer.valueOf(id));
            if (videoGiftConfirm.getType() == 0) {
                if (userDao.checkOverdueTimeGift(data.getMsisdn(), data.getMsisdnReceiver(), data.getVideoId()) == 0) {
//                    String content = "Qua tang Video cho cua Quy khach gui toi thue bao " + user.getPhone() + " that bai do nguoi duoc tang khong xac nhan nhan qua. De tang lai hoac lua chon video cho khac, truy cap https://mecall.vn (mien cuoc truy cap data). Chi tiet soan HD gui 9738 hoac LH " +
//                            "198 (0d).";
                    String strMessage = String.format(commonDao.getMessage("USER", 7009), data.getMsisdn());
//                    sendSms(data.getMsisdn(), content);
                    response.setCode(11);
                    response.setMessage(strMessage);
                    logger.info("[{}]========== message ==========: ", strMessage);
                    return response;
                }
                String path = api_server + "/app/gift";
                RegisterSubscribers registerSubscribers = new RegisterSubscribers();
                registerSubscribers.setMsisdn(data.getMsisdn());
                registerSubscribers.setDynamicSubserviceID(String.valueOf(data.getVideoId()));
                registerSubscribers.setMsisdnReceiver(data.getMsisdnReceiver());
                registerSubscribers.setSupplier(videoDetail.getSupplier());
                registerSubscribers.setPrice(String.valueOf(videoDetail.getPrice()));
                registerSubscribers.setUser(user.getUsername());
                registerSubscribers.setPackage_name("");
                logger.info("[{}]========== request confirm gift ==========: {}", phone, registerSubscribers);
                PackageReturn packageReturn = getDataServerVebt(path, registerSubscribers);
                logger.info("[{}]========== response confirm gift ==========: {}", phone, packageReturn);
                if (packageReturn != null) {
                    Integer code = packageReturn.getError_code();
                    if (code == 0) {
                        userDao.deleteVideoGift(data.getMsisdn(), user.getPhone(), videoGiftConfirm.getVideoId());
                    }
//                    if (code == 0) {
//                        String content_from = "Số thuê bao " + data.getMsisdnReceiver() + "đã nhận quà từ bạn thành công.";
//                        String content_to = "Quý khách nhận được quà tặng video chờ " + videoDetail.getTitle() + " - " + videoDetail.getSupplier() + " từ số thuê bao " + data.getMsisdn() + ", miễn cước 30 ngày đầu tiên, từ chu kỳ tiếp theo, giá cước " + videoDetail.getPrice() + "₫/30 ngày.";
//                        sendSms("0" + data.getMsisdn(), content_from);
//                        sendSms(user.getPhone(), content_to);
//                    }
                    if (code == 1011) {
                        String message_str = commonDao.getMessage("USER", 7010);
                        response.setCode(code);
                        response.setMessage(message_str);
                        logger.info("[{}]========== message video gif comfirm ==========: {}", phone, message_str);
                        return response;
                    }
                    String message = "ERROR_CODE_" + String.valueOf(code);
                    response.setCode(code);
                    String messageStr = commonDao.getMessage("USER", code);
                    response.setMessage(messageStr);
                    logger.info("[{}]========== message video gif comfirm ==========: {}", phone, messageStr);
                }
            }
            if (videoGiftConfirm.getType() == 1) {
                if (userDao.deleteVideoGift(data.getMsisdn(), user.getPhone(), videoGiftConfirm.getVideoId())) {
                    String nameVideo = "";
                    if (videoDetail.getTitle() != null) {
                        nameVideo = CollectionsUtil.removeAccentSms(videoDetail.getTitle());
                    }
                    String str_message = String.format(commonDao.getMessage("USER", 7011), data.getMsisdn());
                    // SMS Builder
                    ActionInforRequest actionInforRequest = new ActionInforRequest();
                    actionInforRequest.setGifteeMsisdn(user.getPhone());
                    actionInforRequest.setVideoName(videoDetail.getTitle());
                    actionInforRequest.setVideoProvider(videoDetail.getSupplier());
                    actionInforRequest.setVideoPrice(String.valueOf(videoDetail.getPrice()));
                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setAction("TANG_REJECT");
                    smsRequest.setActionInfo(actionInforRequest);
                    smsRequest.setUnaccented(true);
                    logger.info("[{}]========== request sms video gift confirm ==========: {}", phone, smsRequest);
                    String content = CollectionsUtil.getMessageSms(smsRequest);
                    logger.info("[{}]========== response sms video gift confirm ==========: {}", phone, content);
//                    String content = "Thue bao " + user.getPhone() + " da tu choi nhan qua tang video cho (" + nameVideo + " - " + supplier + " - " + videoDetail.getPrice() + "₫). De xem va tang video cho khac, truy cap https://mecall.vn (mien cuoc truy cap data). Chi tiet soan HD gui 9738 hoac LH 198(0d).";
                    sendSms(data.getMsisdn(), content);
                    response.setCode(0);
                    response.setMessage(str_message);
                }
            }
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error video gift comfirm ==========: ", e);
            e.printStackTrace();
        }
        logger.info("[" + phone + "]========== message response video gift confirm ==========: ", response);
        return response;
    }

    @Override
    public Response buyVideo(String id, VideoRequest videoRequest) {
        Response response = new Response();
        User user = userDao.findById(id);
        String phone = user.getPhone();
        if (phone.isEmpty()) {
            response.setCode(8009);
            response.setMessage(commonDao.getMessage("USER", 8009));
            return response;
        } else if (videoRequest.getVideoId() == null) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        }
        try {
            logger.info("[{}]========== start buy video ========== ", phone);
            VideoDetailResponse videoDetail = userDao.findVideoById(videoRequest.getVideoId(), Integer.valueOf(id));
            String path = api_server + "/app/buy-content";
            RegisterSubscribers registerSubscribers = new RegisterSubscribers();
            registerSubscribers.setMsisdn(phone);
            registerSubscribers.setDynamicSubserviceID(String.valueOf(videoRequest.getVideoId()));
            registerSubscribers.setPrice(String.valueOf(videoDetail.getPrice()));
            registerSubscribers.setSupplier(videoDetail.getSupplier());
            registerSubscribers.setUser(user.getUsername());
            logger.info("[" + phone + "]========== request object ==========: {}", registerSubscribers);
            PackageReturn packageReturn = getDataServerVebt(path, registerSubscribers);
            logger.info("[" + phone + "]========== response object ==========: {}", packageReturn);
            if (packageReturn != null) {
                Integer code = packageReturn.getError_code();
                String message = "ERROR_CODE_" + String.valueOf(code);
                if (code == 18) {
//                    String content = String.format(commonDao.getMessage("USER", 1000), CollectionsUtil.removeAccentSms(videoDetail.getTitle()), videoDetail.getItemId(), videoDetail.getSupplier(), videoDetail.getPrice());

                    String str_message = commonDao.getMessage("USER", 8003);
//                    sendSms(user.getPhone(), content);
                    response.setCode(code);
                    response.setMessage(str_message);
                    logger.info("[{}]========== message ==========: {}", phone, str_message);
                    return response;
                }
                if (code == 1000 || code == 1004) {
                    response.setCode(0);
                    String str_message = commonDao.getMessage("USER", 0);
                    response.setMessage(str_message);
                    logger.info("[{}]========== message ==========: {}", phone, str_message);
                    return response;
                } else if (code == 19) {
                    response.setCode(code);
                    response.setMessage(commonDao.getMessage("SYSTEM", 8000));
                } else {
                    response.setCode(code);
                }
                String str_message = commonDao.getMessage("USER", code);
                response.setMessage(str_message);
                logger.info("[{}]========== message ==========: {}", phone, str_message);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error ==========: ", e);
            e.printStackTrace();
        }
        logger.info("==================End [{}]==================", phone);
        return response;
    }

    @Override
    public Response cancelVideo(String id, VideoRequest videoRequest) {
        Response response = new Response();
        User user = userDao.findById(id);
        String phone = user.getPhone();

        if (videoRequest.getVideoId() == null) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        }

        try {
            logger.info("[{}]========== start cancel video ==========", phone);
            VideoDetailResponse videoDetail = userDao.findVideoById(videoRequest.getVideoId(), Integer.valueOf(id));
            String path = api_server + "/app/cancel-content";
            RegisterSubscribers registerSubscribers = new RegisterSubscribers();
            registerSubscribers.setMsisdn(phone);
            registerSubscribers.setDynamicSubserviceID(String.valueOf(String.valueOf(videoRequest.getVideoId())));
            registerSubscribers.setSupplier(videoDetail.getSupplier());
            registerSubscribers.setPrice(String.valueOf(videoDetail.getPrice()));
            registerSubscribers.setUser(user.getUsername());
            logger.info("[" + phone + "]========== request ==========: ", registerSubscribers);
            PackageReturn packageReturn = getDataServerVebt(path, registerSubscribers);
            logger.info("[" + phone + "]========== response ==========: ", packageReturn);
            if (packageReturn != null) {
                Integer code = packageReturn.getError_code();
                String message = "ERROR_CODE_" + String.valueOf(code);
                if (code == 1008) {
                    response.setCode(0);
                } else {
                    response.setCode(code);
                }
                response.setMessage(commonDao.getMessage("USER", code));
            }
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error ==========: ", e);
            e.printStackTrace();
        }
        logger.info("==================End [{}]==================", phone);
        return response;
    }

    @Override
    public Response checkPackage(String id) {
        User user = userDao.findById(id);
        Response response = new Response();
        PackDataResponse data = null;
        String phone = user.getPhone();
        try {
            logger.info("[{}]========== start check package ==========", phone);
            data = userDao.checkPackage(phone);
            logger.info("[" + phone + "]========== package data ==========: ", data);
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
            response.setData(data);
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error ==========: ", e);
            e.printStackTrace();
        }
        logger.info("==================End [{}]==================", phone);
        return response;
    }

    public boolean sendSms(String phone, String content) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Accept-Language", "vi");
            MTsms mTsms = new MTsms(phone, content);
            HttpEntity<MTsms> request = new HttpEntity<>(mTsms, headers);
            String path = api_server + "/app/send-mt";
            MpsResponse responseData = restTemplate.postForObject(path, request, MpsResponse.class);
            if (responseData.getError_code() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int checkSim(String phone) {
        RestTemplate restTemplate = new RestTemplate();
        int code = 999;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Accept-Language", "vi");
            CheckSim checkSim = new CheckSim(phone);
            HttpEntity<CheckSim> request = new HttpEntity<>(checkSim, headers);
            String path = api_server + "/app/check-sim-mdm";
            MpsResponse responseData = restTemplate.postForObject(path, request, MpsResponse.class);
            code = responseData.getError_code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public List<NotificationResponse> getNotification(int id) {
        int status = 0;
        List<NotificationResponse> result = new ArrayList<>();
        String phone = "";
        try {
            if (id != 0) {
                User user = userDao.findById(String.valueOf(id));
                phone = user.getPhone();
                status = 1;
            }
            logger.info("[{}]========== start get notification ==========", phone);
            result = userDao.getNotification(phone, status);
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error notification ==========: ", e);
            e.printStackTrace();
        }
        logger.info("[{}]========== list notification ==========: {}", phone, result);
        return result;
    }

    @Override
    public Response saveNotificationStatus(int id, NotificationRequest notificationRequest) {
        Response response = new Response();
        int status = 1;

        String device = notificationRequest.getDevice();
        if (id != 0) {
            User user = userDao.findById(String.valueOf(id));
            device = user.getPhone();
        }

        if (notificationRequest.getNotificationId() == 0) {
            logger.info("[{}]========== Field notificationId is required ==========", device);
            return response;
        }

        logger.info("[{}]========== notify info ==========: {}", device, notificationRequest);
        if (userDao.saveNotificationStatusDto(device, notificationRequest.getNotificationId(), status)) {
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9003));
        }
        return response;
    }

    @Override
    public Response followUser(String phone, String id, FollowUser followUser) {
        Response response = new Response();
        logger.info("[{}]========== start follow user ==========", phone);
        if (followUser.getUserId() == 0) {
            response.setCode(999);
            response.setMessage(commonDao.getMessage("SYSTEM", 8000));
            logger.info("[{}]========== Field userId is required ==========", phone);
            return response;
        } else if (Integer.valueOf(id) == followUser.getUserId()) {
            response.setCode(8005);
            response.setMessage(commonDao.getMessage("USER", 8005));
            return response;
        } else if (userDao.saveOrDeleteFollowUser(phone, Integer.valueOf(id), followUser.getUserId(), followUser.getCreatorType())) {
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
        } else {
            response.setCode(8004);
            response.setMessage(commonDao.getMessage("USER", 8004));
        }
        logger.info("[{}]========== end follow user ==========", phone);
        return response;
    }

    @Override
    public List<FollowsUser> followsByUser(String phone, int id, int userId, int creatorType, int type) {
        List<FollowsUser> result = new ArrayList<>();
        Response response = new Response();
//        if (id == userId) {
//            return result;
//        }
        if (type == 0) {
            if (creatorType == 0) {
                result = userDao.getListFollowUser(phone, id, userId, creatorType);
            }
        }
        if (type == 1) {
            result = userDao.getListUserFollow(phone, id, userId, creatorType);
        }
        return result;
    }

    @Override
    public Response videoWait(String phone, int id, VideoWait videoWait) {
        Response response = new Response();
        int timeType = videoWait.getTimeType();
        int ruleId = videoWait.getId();
        int type = videoWait.getType();
        String yearStart = null;
        String monthStart = null;
        String dayStart = null;
        String hoursStart = null;
        String minuteStart = null;
        String yearEnd = null;
        String monthEnd = null;
        String dayEnd = null;
        String hoursEnd = null;
        String minuteEnd = null;
        String videoIds = videoWait.getVideoIds();
        int countVideoId = videoWait.getVideoIds().split(",").length;
        logger.info("[{}]========== data video wait input ==========: {}", phone, videoWait);

        if (videoWait.getId() == 0) {
            logger.info("[{}]========== Field id is required ==========", phone);
            return response;
        } else if (videoWait.getVideoIds().isEmpty()) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
        } else if (videoWait.getTimeFrom().isEmpty()) {
            logger.info("[{}]========== Field timeFrom is required ==========", phone);
        } else if (videoWait.getTimeTo().isEmpty()) {
            logger.info("[{}]========== Field timeTo is required ==========", phone);
        }

        try {
            if (timeType == 0) {

            } else if (timeType == 1) {
                String[] splitText = videoWait.getTimeFrom().split(" ");
                String[] dateText = splitText[0].split("/");
                String[] timeText = splitText[1].split(":");
                yearStart = dateText[2];
                monthStart = dateText[1];
                dayStart = dateText[0];
                hoursStart = timeText[0];
                minuteStart = timeText[1];

                String[] splitTextEnd = videoWait.getTimeTo().split(" ");
                String[] dateTextEnd = splitTextEnd[0].split("/");
                String[] timeTextEnd = splitTextEnd[1].split(":");
                yearEnd = dateTextEnd[2];
                monthEnd = dateTextEnd[1];
                dayEnd = dateTextEnd[0];
                hoursEnd = timeTextEnd[0];
                minuteEnd = timeTextEnd[1];
            } else if (timeType == 2) {
                String[] timeTextStart = videoWait.getTimeFrom().split(":");
                hoursStart = timeTextStart[0];
                minuteStart = timeTextStart[1];
                String[] timeTextEnd = videoWait.getTimeTo().split(":");
                hoursEnd = timeTextEnd[0];
                minuteEnd = timeTextEnd[1];
            } else if (timeType == 3) {
                dayStart = videoWait.getTimeFrom();
                dayEnd = videoWait.getTimeTo();
            } else if (timeType == 4) {
                monthStart = videoWait.getTimeFrom();
                monthEnd = videoWait.getTimeTo();
            }
            if (userDao.saveVideoWait(type, ruleId, timeType, yearStart, yearEnd, monthStart, monthEnd, dayStart, dayEnd, hoursStart, hoursEnd, minuteStart, minuteEnd, videoIds, countVideoId, phone)) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response getListNameVideoWait(String phone, int type) {
        List<NameVideoWaitSetting> listPhone = new ArrayList<>();
        List<NameVideoWaitSetting> listGroup = new ArrayList<>();
        Response response = new Response();
        try {
            if (type == 0) {
                listPhone = userDao.getListNameVideoWait(phone, 0);
            } else if (type == 1) {
                listGroup = userDao.getListNameVideoWait(phone, 1);
            }

            DataNameVideoWait result = new DataNameVideoWait();
            result.setListPhone(listPhone);
            result.setListGroup(listGroup);
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
            response.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response addPhoneVideoWait(String phone, NameVideoWait nameVideoWait) {
        Response response = new Response();
        try {
            if (nameVideoWait.getName().isEmpty()) {
                logger.info("[{}]========== Field name is required ==========", phone);
                return response;
            } else if (nameVideoWait.getType() == null) {
                logger.info("[{}]========== Field type is required ==========", phone);
                return response;
            } else if (phone.isEmpty()) {
                response.setCode(8009);
                response.setMessage(commonDao.getMessage("USER", 8009));
            } else if (userDao.addPhoneVideoWait(phone, nameVideoWait.getName(), nameVideoWait.getType(), nameVideoWait.getSub())) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response phoneSettingDel(String phone, PhoneSetting phoneSetting) {
        Response response = new Response();
        try {
            if (phoneSetting.getId() == null) {
                logger.info("[{}]========== Field id is required ==========", phone);
                return response;
            } else if (phoneSetting.getType() == null) {
                logger.info("[{}]========== Field type is required ==========", phone);
                return response;
            }
            if (userDao.phoneSettingDel(phone, phoneSetting.getId(), phoneSetting.getType())) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response groupSettingAdd(String phone, PhoneGroup phoneGroup) {
        Response response = new Response();
        try {
            if (phoneGroup.getId() == 0) {
                logger.info("[{}]========== Field id is required ==========", phone);
                return response;
            } else if (phoneGroup.getPhone().isEmpty()) {
                logger.info("[{}]========== Field phone is required ==========", phone);
                return response;
            } else if (phoneGroup.getSub().isEmpty()) {
                logger.info("[{}]========== Field sub is required ==========", phone);
                return response;
            } else if (phone.isEmpty()) {
                response.setCode(8009);
                response.setMessage(commonDao.getMessage("USER", 8009));
            } else if (userDao.groupSettingAdd(phone, phoneGroup.getId(), phoneGroup.getPhone(), phoneGroup.getSub())) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response groupPhoneDel(String phone, GroupPhoneDel groupPhoneDel) {
        Response response = new Response();
        try {
            if (groupPhoneDel.getId() == 0) {
                logger.info("[{}]========== Field id is required ==========", phone);
                return response;
            } else if (groupPhoneDel.getContactId() == 0) {
                logger.info("[{}]========== Field contactId is required ==========", phone);
                return response;
            }
            if (userDao.groupPhoneDel(phone, groupPhoneDel.getId(), groupPhoneDel.getContactId())) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<PhoneGroupResponse> groupPhoneList(String phone, int ruleId) {
        List<PhoneGroupResponse> listPhone = new ArrayList<>();
        try {
            listPhone = userDao.groupPhoneList(phone, ruleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPhone;
    }

    @Override
    public Response phoneSettingDetail(String phone, int ruleId, int type) {
        Response response = new Response();
        VideoWait videoWait = new VideoWait();
        try {
            videoWait = userDao.phoneSettingDetail(phone, ruleId, type);
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
            response.setData(videoWait);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response uploadAvatar(String phone, String id, MultipartFile avatar) throws IOException {
        Response response = new Response();
        try {
            File avatarCv = new File(Objects.requireNonNull(avatar.getOriginalFilename()));
            String fileNameAvatar = FileUtil.getFileName(avatarCv);
            String filePathAvatar = "/" + userDao.getPathCp(4) + fileNameAvatar;
            avatar.transferTo(new File(filePathAvatar));
            logger.info("[{}]========== save file avatar done ==========: {}", phone, filePathAvatar);
            userDao.saveAvatar(phone, id, filePathAvatar);
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response updateUsername(String phone, String id, String name) {
        Response response = new Response();
        try {
            if (name.isEmpty()) {
                logger.info("[{}]========== Field name is required ==========", phone);
            }
            if (userDao.updateUsername(phone, id, name)) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response updatePassword(String phone, String id, ChangePasswordDto changePasswordDto) {
        Response response = new Response();
        try {
            if (changePasswordDto.getPasswordOld().isEmpty()) {
                logger.info("[{}]========== Field passwordOld is required ==========", phone);
                return response;
            } else if (changePasswordDto.getPasswordNew().isEmpty()) {
                logger.info("[{}]========== Field passwordNew is required ==========", phone);
                return response;
            }
            User user = userDao.findById(id);
            if (user.getLoginType() == 2 || user.getLoginType() == 3 || user.getLoginType() == 4) {
                response.setCode(8008);
                response.setMessage(commonDao.getMessage("USER", 8008));
                return response;
            }
            String passwordDb = user.getPassword();
            boolean checkPassword = BCrypt.checkpw(changePasswordDto.getPasswordOld(), passwordDb);
            String passwordNewEncoder = BCrypt.hashpw(changePasswordDto.getPasswordNew(), BCrypt.gensalt());
            if (checkPassword) {
                userDao.updatePassword(phone, id, passwordNewEncoder);
                response.setCode(0);
                response.setMessage(commonDao.getMessage("USER", 9002));
            } else {
                response.setCode(8006);
                response.setMessage(commonDao.getMessage("USER", 8006));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response updatePhoneUser(String phone, String id, String phoneNew) {
        Response response = new Response();
        try {
            if (phoneNew.isEmpty()) {
                logger.info("[{}]========== Field phone is required ==========", phone);
                return response;
            }
            User user = userDao.findById(id);
            if (user.getLoginType() == 2 || user.getLoginType() == 3 || user.getLoginType() == 4) {
                if (userDao.updatePhoneUser(phone, id, phoneNew)) {
                    response.setCode(0);
                    response.setMessage(commonDao.getMessage("USER", 9002));
                }
            } else {
                response.setCode(8007);
                response.setMessage(commonDao.getMessage("USER", 8007));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<BonusHistory> bonusHistory(String phone) {
        List<BonusHistory> listBonus = new ArrayList<>();
        try {
            if (phone == null) {
                return listBonus;
            } else {
                listBonus = userDao.bonusHistory(phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBonus;
    }

    @Override
    public String getIdHeader(String header) {
        String id = "";
        if (header != null) {
            String jwtToken = header.replace("Bearer ", "");
            String[] split_string = jwtToken.split("\\.");
            String base64EncodedBody = split_string[1];
            Base64 base64Url = new Base64(true);
            String body = new String(base64Url.decode(base64EncodedBody));
            JSONObject json = new JSONObject(body);
            System.out.println("JWT json : " + json);
            id = String.valueOf(json.getInt("id"));
            System.out.println("JWT dm : " + id);
        }
        return id;
    }
}
