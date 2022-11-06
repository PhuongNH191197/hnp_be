package com.elcom.plus.user.dao.impl;

import com.elcom.plus.common.util.constant.ResponseCode;
import com.elcom.plus.common.util.constant.ResponseMessage;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.controller.VideoController;
import com.elcom.plus.user.dao.UserDao;
import com.elcom.plus.user.dto.request.*;
import com.elcom.plus.user.dto.response.*;
import com.elcom.plus.user.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.http.annotation.Obsolete;
import org.openxmlformats.schemas.drawingml.x2006.chart.STXstring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static org.apache.commons.lang.StringUtils.trim;

@Repository
public class UserDaoImpl extends AbstractDao implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final ConnectionManager connectionManager = new ConnectionManager();

    @Override
    public User findByUsername(String username) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_USER_BY_USERNAME(?)}");
            cs.setString(1, username);
            rs = cs.executeQuery();
            while (rs.next()) {
                User data = new User();
                data.setId(rs.getLong("id"));
                data.setUsername(rs.getString("username"));
                data.setPassword(rs.getString("password"));
                data.setPhone(rs.getString("phone"));
                data.setEmail(rs.getString("email"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setAutoPlayVideo(rs.getInt("is_auto_play_video"));
                data.setEarnMoney(rs.getInt("is_earn_money"));
                logger.info("[{}] ========== output data ==========: {}", username, data);
                return data;
            }
        } catch (Exception e) {
            logger.error("[{}] Error get info user: {}", username, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: null", username);
        return null;
    }

    @Override
    public User findUserInfo(int userVisitId, String userId, int creatorType) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            // creatorType = 0 => is user | creatorType = 1 => is admin
            if (creatorType == 0) {
                cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_WAP_INFO_BY_ID(?,?)}");
            } else {
                cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_CMS_INFO_BY_ID(?,?)}");
            }
            cs.setInt(1, userVisitId);
            cs.setInt(2, Integer.valueOf(userId));
            rs = cs.executeQuery();
            while (rs.next()) {
                User data = new User();
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setUsername(rs.getString("username"));
                data.setFollowings(rs.getInt("followings"));
                data.setFollowers(rs.getInt("followers"));
                data.setStatus(rs.getInt("status"));
                logger.info("[{}] ========== output data ==========: {}", userVisitId, data);
                return data;
            }

        } catch (Exception e) {
            logger.error("[{}] Error get info user: {}", userVisitId, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: null", userVisitId);
        return null;
    }

    @Override
    public User findById(String id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_BY_ID(?)}");
            cs.setInt(1, Integer.parseInt(id));
            rs = cs.executeQuery();
            while (rs.next()) {
                User data = new User();
                data.setId(rs.getLong("id"));
                data.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
                data.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
                data.setPhone(rs.getString("phone") != null ? rs.getString("phone") : "");
                data.setEmail(rs.getString("email") != null ? rs.getString("email") : "");
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setAutoPlayVideo(rs.getInt("is_auto_play_video"));
                data.setEarnMoney(rs.getInt("is_earn_money"));
                data.setOmapUserId(rs.getLong("omap_user_id"));
                data.setFollowers(rs.getInt("followers"));
                data.setFollowings(rs.getInt("followings"));
                data.setLoginType(rs.getInt("login_type"));
                logger.info("[{}] ========== output data ==========: {}", id, data);
                return data;
            }
        } catch (Exception e) {
            logger.error("[{}] Error get info user: {}", id, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: null", id);
        return null;
    }

    @Override
    public ListMyVideo findVideoBuy(int userId, int videoId, String phone, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        ListMyVideo listMyVideo = new ListMyVideo();
        List<MyVideoResponse> result = new ArrayList<>();
        try {
            logger.info("[{}]==========start query get video buy==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_VIDEO_BUY(?, ?, ?, ?, ?)}");
            cs.setInt(1, userId);
            cs.setInt(2, videoId);
            cs.setString(3, phone);
            cs.setInt(4, skip);
            cs.setInt(5, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                MyVideoResponse data = new MyVideoResponse();
                data.setItemId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setPrice(rs.getLong("price"));
                data.setSettingId(rs.getInt("setting_id"));
                data.setNumberView(rs.getLong("number_view"));
                data.setNumberLike(rs.getLong("number_like"));
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                String fileName = rs.getString("file_name") != null ? rs.getString("file_name").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                data.setFileName(fileName);
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                data.setCreatorAvatar(creatorAvatar);
                data.setCatId(rs.getInt("cat_id"));
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name") : "";
                data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : rs.getString("cp_name"));
                data.setSupplier(rs.getString("supplier"));
                data.setIsLike(rs.getInt("is_like"));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setExpireTime(rs.getString("expire_time") != null ? rs.getString("expire_time") : "");
                if (listMyVideo.getSkipIndex() < 0) {
                    listMyVideo.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get video buy: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]==========end query get video buy==========", phone);
        listMyVideo.setData(result);
        return listMyVideo;
    }

    @Override
    public boolean insertVideoGift(String phone, String phoneReceive, int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL SET_VIDEO_GIFT(?, ?, ?)}");
            cs.setString(1, phone);
            cs.setString(2, phoneReceive);
            cs.setInt(3, videoId);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== insert video gift done ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[{}] Error insert video gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== insert video gift fail ==========", phone);
        return false;
    }

    @Override
    public ListMyVideo findVideoGift(int userId, int videoId, String phone, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        ListMyVideo listMyVideo = new ListMyVideo();
        List<MyVideoResponse> result = new ArrayList<>();
        try {
            logger.info("[{}]==========start get video gift==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_VIDEO_GIFT(?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, videoId);
            cs.setString(3, phone);
            cs.setInt(4, skip);
            cs.setInt(5, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                MyVideoResponse data = new MyVideoResponse();
                data.setItemId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setPrice(rs.getLong("price"));
                data.setSettingId(rs.getInt("setting_id"));
                data.setNumberView(rs.getLong("number_view"));
                data.setNumberLike(rs.getLong("number_like"));
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                String fileName = rs.getString("file_name") != null ? rs.getString("file_name").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                data.setFileName(fileName);
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                data.setCreatorAvatar(creatorAvatar);
                data.setCatId(rs.getInt("cat_id"));
                data.setPhoneGift(rs.getString("phone"));
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name") : "";
                data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : rs.getString("cp_name"));
                data.setSupplier(rs.getString("supplier"));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setIsLike(rs.getInt("is_like"));
                data.setExpireTime(rs.getString("expire_time") != null ? rs.getString("expire_time") : "");
                if (listMyVideo.getSkipIndex() < 0) {
                    listMyVideo.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get video gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== end query get video gift ==========", phone);
        listMyVideo.setData(result);
        return listMyVideo;
    }

    @Override
    public VideoRequest getUserSendGift(String phone, int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        VideoRequest data = new VideoRequest();
        logger.info("[{}]========== start query get user send gift ==========", phone);
        try {
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_USER_SEND_GIFT(?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, videoId);
            rs = cs.executeQuery();
            if (rs.next()) {
                data.setMsisdn(rs.getString("phone"));
                data.setMsisdnReceiver(rs.getString("phone_receive"));
                data.setVideoId(rs.getInt("item_id"));
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get user send gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, data);
        logger.info("[{}]========== end query get user send gift ==========", phone);
        return data;
    }

    @Override
    public boolean deleteVideoGift(String phone, String phoneReceive, int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String phoneGift = "";
        try {
            con = ConnectionManager.getConnection();
            logger.info("[{}] ==========start query delete video gift.==========", phone);
            cs = con.prepareCall("{CALL DEL_VIDEO_GIFT(?, ?, ?)}");
            cs.setString(1, phone);
            cs.setString(2, phoneReceive);
            cs.setInt(3, videoId);
            cs.execute();
            con.commit();
            logger.info("[{}] query delete video gift done.", phone);
            logger.info("[{}] ==========end query delete video gift.==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[{}] Error query delete video gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] query delete video gift fail.", phone);
        logger.info("[{}] ==========end query delete video gift.==========", phone);
        return false;
    }

    @Override
    public int checkOverdueTimeGift(String phone, String phoneReceive, int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        int overdueTime = 0;
        try {
            logger.info("[{}] ==========start query check overdue time gift.==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CHECK_OVERDUE_TIME(?, ?, ?, ?)}");
            cs.setString(1, phone);
            cs.setString(2, phoneReceive);
            cs.setInt(3, videoId);
            cs.setInt(4, java.sql.Types.INTEGER);
            cs.execute();
            overdueTime = cs.getInt(4);
        } catch (Exception e) {
            logger.error("[{}] Error query check overdue time gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, overdueTime);
        logger.info("[{}] ==========end query check overdue time gift.==========", phone);
        return overdueTime;
    }

    @Override
    public String getSupplier(String id) {
        String supplier = "";
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}] ==========start query get supplier.==========", id);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_SUPPLIER(?,?)}");
            cs.setInt(1, Integer.valueOf(id));
            cs.setInt(2, Types.DOUBLE);
            cs.execute();
            supplier = String.valueOf(cs.getInt(2));
            cs.close();
        } catch (Exception e) {
            logger.error("[{}] Error query check overdue time gift: {}", id, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", id, supplier);
        logger.info("[{}] ==========start query get supplier.==========", id);
        return supplier;
    }

    @Override
    public VideoDetailResponse findVideoById(int id, int userId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        VideoDetailResponse data = new VideoDetailResponse();
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_DETAIL_VIDEO_BY_ID(?,?)}");
            cs.setInt(1, id);
            cs.setInt(2, userId);
            rs = cs.executeQuery();
            logger.info("[{}] ========== start query get detail video by id. ==========", id);
            if (rs.next()) {
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                data.setCreatorAvatar(creatorAvatar);
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name") : "";
                data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : cpName);
                String fileName = rs.getString("file_name") != null ? rs.getString("file_name").replace("//u02", domainFile) : "";
                data.setFileName(fileName);
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                data.setTitle(rs.getString("title"));
                data.setPrice(rs.getLong("price"));
                data.setNumberView(rs.getInt("number_view"));
                data.setNumberLike(rs.getInt("number_like"));
                data.setNumberShare(rs.getInt("number_share"));
                data.setSupplier(rs.getString("supplier"));
                data.setIsLike(rs.getInt("is_like"));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                logger.info("[{}] ========== output data ==========: {}", userId, data);
                logger.info("[{}] ========== end query get detail video by id. ==========", userId);
                return data;
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get detail video by id: {}", userId, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", userId, data);
        logger.info("[{}] ========== end query get detail video by id. ==========", userId);
        return data;
    }

    @Override
    public PackDataResponse checkPackage(String phone) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        PackDataResponse data = null;
        try {
            logger.info("[{}] ========== start query check package ==========", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL VRBT.GET_PACKAGE_ALL(?)}");
            cs.setString(1, phone);
            rs = cs.executeQuery();
            while (rs.next()) {
                if (rs.getInt("status") == 1) {
                    data = new PackDataResponse();
                    data.setId(rs.getInt("id"));
                    data.setTitle(rs.getString("title"));
                    data.setPrice(rs.getString("price"));
                    data.setDescription(rs.getString("description"));
                    data.setStatus(rs.getInt("status"));
                    data.setExpired(rs.getString("expired"));
                    data.setName(rs.getString("name"));
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("[{}] Error query check package: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, data);
        logger.info("[{}] ========== end query check package ==========", phone);
        return data;
    }

    @Override
    public List<PackDataResponse> packOfData(String phone) {
        List<PackDataResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}]================== start query get pack ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL VRBT.GET_PACKAGE_OBJ(?)}");
            cs.setString(1, phone);
            rs = cs.executeQuery();
            while (rs.next()) {
                PackDataResponse data = new PackDataResponse();
                data.setId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                data.setPrice(rs.getString("price"));
                data.setDescription(rs.getString("description"));
                data.setStatus(rs.getInt("status"));
                data.setExpired(rs.getString("expired"));
                data.setName(rs.getString("name"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get pack: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]================== end query get pack ==================", phone);
        return result;
    }

    @Override
    public String getPathConfig(String path) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String path_config = "";
        try {
            logger.info("[]================== start query get path config ==================");
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_CONFIG_PATH(?)}");
            cs.setString(1, path);
            rs = cs.executeQuery();
            while (rs.next()) {
                PathComfig data = new PathComfig();
                data.setTitle(rs.getString("title"));
                data.setValue(rs.getString("value"));
                data.setNote(rs.getString("note"));
                path_config = data.getValue();
            }
        } catch (Exception e) {
            logger.error("Error query get path config: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", path_config);
        logger.info("[]================== end query get path config ==================");
        return path_config;
    }

    @Override
    public String getPathCp(int id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String path = "";
        try {
            logger.info("[]================== start query get path CP ==================");
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_CP_PATH(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            while (rs.next()) {
                CpPath data = new CpPath();
                data.setId(rs.getInt("id"));
                data.setName(rs.getString("name"));
                data.setPath(rs.getString("path"));
                path = data.getPath();
            }
        } catch (Exception e) {
            logger.error("Error query get path CP: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", path);
        logger.info("[]================== end query get path CP ==================");
        return path;
    }

    @Override
    public boolean checkVideoInCollection(String phone, int videoId) {
        boolean checkVideo = false;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}]================== start query check video in collection ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL VRBT.CHECK_VIDEO_INCOLLECTION(?,?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, videoId);
            cs.setInt(3, Types.INTEGER);
            cs.execute();
            Integer countVideo = cs.getInt(3);
            System.out.println("countVideo: " + countVideo);
            if (countVideo > 0) {
                checkVideo = true;
            }
        } catch (Exception e) {
            logger.error("[{}] Error query check video in collection: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, checkVideo);
        logger.info("[{}]================== end query check video in collection ==================", phone);
        return checkVideo;
    }

    @Override
    public List<NotificationResponse> getNotification(String phone, int status) {
        List<NotificationResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query notification ==========", phone);
            cs = con.prepareCall("{CALL GET_NOTIFICATION(?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, status);
            rs = cs.executeQuery();
            while (rs.next()) {
                NotificationResponse data = new NotificationResponse();
                data.setId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                data.setLinkType(rs.getInt("link"));
                data.setTime(rs.getString("time_notification"));
                data.setStatus(rs.getInt("status"));
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                data.setLinkDetail(rs.getString("link_detail"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setPositionDesign(rs.getInt("position_design"));
                data.setCatName(rs.getString("cat_name"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error query notification ==========: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query notification ==========", phone);
        return result;
    }

    @Override
    public boolean saveNotificationStatusDto(String device, int notificationId, int status) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        boolean noti = false;
        try {
            logger.info("[] ========== start query save notification ==========");
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = call ADD_NOTIFICATION_STATUS(?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, device);
            cs.setInt(3, notificationId);
            cs.setInt(4, status);
            cs.execute();
            con.commit();
            noti = true;
        } catch (Exception e) {
            logger.error("[] Error query save notification: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", noti);
        logger.info("[] ========== end query save notification ==========");
        return noti;
    }

    @Override
    public List<MyVideoResponse> videoUploadByUser(String phone, String userId, int creatorType, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<MyVideoResponse> result = new ArrayList<>();
        try {
            con = connectionManager.getConnection();
            // creatorType = 0 => is user | creatorType = 1 => is admin
            if (creatorType == 0) {
                logger.info("[{}] ========== start query get video upload ==========", phone);
                cs = con.prepareCall("{CALL CMP.GET_VIDEO_UPLOAD(?,?,?)}");
            } else {
                logger.info("[{}] ========== start query get video upload cms ==========", phone);
                cs = con.prepareCall("{CALL CMP.GET_VIDEO_UPLOAD_CMS(?,?,?)}");
            }
            cs.setInt(1, Integer.valueOf(userId));
            cs.setInt(2, skip);
            cs.setInt(3, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                MyVideoResponse data = new MyVideoResponse();
                if (rs.getInt("status") == 1) {
                    data.setItemId(rs.getInt("id"));
                    data.setTitle(rs.getString("title"));
                    String hashtag = rs.getString("hashtag");
                    String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                    data.setHashtags(Arrays.asList(hashtags));
                    data.setPrice(rs.getLong("price"));
                    data.setSettingId(rs.getInt("setting_id"));
                    data.setNumberView(rs.getLong("number_view"));
                    data.setNumberLike(rs.getLong("number_like"));
                    String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                    String fileName = rs.getString("file_name") != null ? rs.getString("file_name").replace("//u02", domainFile) : "";
                    data.setFilePath(filePath);
                    data.setFileName(fileName);
                    String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                    data.setThumbnail(thumbnail);
                    String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                    data.setCreatorAvatar(creatorAvatar);
                    data.setCatId(rs.getInt("cat_id"));
                    String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name") : "";
                    data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : rs.getString("cp_name"));
                    data.setSupplier(rs.getString("supplier"));
                    data.setIsLike(rs.getInt("is_like"));
                    data.setUserId(rs.getInt("user_id"));
                    data.setCreatorType(rs.getInt("creator_type"));
                    result.add(data);
                }
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get video upload: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get video upload ==========", phone);
        return result;
    }

    @Override
    public boolean saveOrDeleteFollowUser(String phone, int id, int userId, int creatorType) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        boolean followUser = false;
        try {
            logger.info("[{}]========== Start query follow user ==========", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = call WAP_AUTH.SAVE_FOLLOW_USER(?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.setInt(3, userId);
            cs.setInt(4, creatorType);
            cs.execute();
            con.commit();
            followUser = true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query follow user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}]========== query follow user ==========: {}", phone, followUser);
        logger.info("[{}]========== End query follow user ==========", phone);
        return followUser;
    }

    @Override
    public List<FollowsUser> getListFollowUser(String phone, int id, int userId, int creatorType) {
        List<FollowsUser> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get list follow user ==========", phone);
            cs = con.prepareCall("{CALL WAP_AUTH.GET_FOLLOWS_USER(?,?,?)}");
            cs.setInt(1, id);
            cs.setInt(2, userId);
            cs.setInt(3, creatorType);
            rs = cs.executeQuery();
            while (rs.next()) {
                FollowsUser data = new FollowsUser();
                data.setUserId(rs.getInt("user_id"));
                data.setUsername(rs.getString("user_name"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setCreatorType(rs.getInt("creator_type"));
                data.setStatus(rs.getInt("status"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query get list follow user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] output data: {}", phone, result);
        logger.info("[{}] ========== end query get list follow user ==========", phone);
        return result;
    }

    @Override
    public List<FollowsUser> getListUserFollow(String phone, int id, int userId, int creatorType) {
        List<FollowsUser> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get list user follow ==========", phone);
            cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_FOLLOWS(?,?,?)}");
//            if (creatorType == 0) {
//                cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_FOLLOWS_WAP(?,?,?)}");
//            } else {
//                cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_FOLLOWS_CMS(?,?,?)}");
//            }
            cs.setInt(1, id);
            cs.setInt(2, userId);
            cs.setInt(3, creatorType);
            rs = cs.executeQuery();
            while (rs.next()) {
                FollowsUser data = new FollowsUser();
                data.setUserId(rs.getInt("user_id"));
                data.setUsername(rs.getString("user_name"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setCreatorType(rs.getInt("creator_type"));
                data.setStatus(rs.getInt("status"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query get list user follow: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] output data: {}", phone, result);
        logger.info("[{}] ========== end query get list follow user ==========", phone);
        return result;
    }

    @Override
    public boolean saveVideoWait(int type, int ruleId, int timeType, String yearStart, String yearEnd, String monthStart, String monthEnd, String dayStart, String dayEnd, String hoursStart, String hoursEnd, String minuteStart, String minuteEnd, String videoIds, int countVideoId, String phone) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query save video wait ==========", phone);
            cs = con.prepareCall("{? = CALL VRBT.SAVE_VIDEO_WAIT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, timeType);
            cs.setString(3, yearStart);
            cs.setString(4, yearEnd);
            cs.setString(5, monthStart);
            cs.setString(6, monthEnd);
            cs.setString(7, dayStart);
            cs.setString(8, dayEnd);
            cs.setString(9, hoursStart);
            cs.setString(10, hoursEnd);
            cs.setString(11, minuteStart);
            cs.setString(12, minuteEnd);
            cs.setString(13, videoIds);
            cs.setInt(14, countVideoId);
            cs.setInt(15, type);
            cs.setInt(16, ruleId);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query save video wait done ==========", phone);
            logger.info("[{}] ========== end query save video wait ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query save video wait: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query save video wait done ==========", phone);
        logger.info("[{}] ========== end query save video wait ==========", phone);
        return false;
    }

    @Override
    public List<NameVideoWaitSetting> getListNameVideoWait(String phone, int type) {
        List<NameVideoWaitSetting> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get list name video wait ==========", phone);
            cs = con.prepareCall("{CALL VRBT.GET_NAME_VIDEO_WAIT(?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, type);
            rs = cs.executeQuery();
            while (rs.next()) {
                NameVideoWaitSetting data = new NameVideoWaitSetting();
                data.setId(rs.getInt("rule_id"));
                data.setName(rs.getString("name"));
                data.setSub(rs.getString("sub"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query get list name video wait: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] output data: {}", phone, result);
        logger.info("[{}] ========== end query get list name video wait ==========", phone);
        return result;
    }

    @Override
    public boolean addPhoneVideoWait(String phone, String name, int type, String sub) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query save phone video wait ==========", phone);
            cs = con.prepareCall("{? = CALL VRBT.SAVE_PHONE_VIDEO_WAIT(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, phone);
            cs.setString(3, name);
            cs.setInt(4, type);
            cs.setString(5, sub);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query save phone video wait done ==========", phone);
            logger.info("[{}] ========== end save phone video wait ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query save phone video wait: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query save phone video wait fail ==========", phone);
        logger.info("[{}] ========== end save phone video wait ==========", phone);
        return false;
    }

    @Override
    public boolean phoneSettingDel(String phone, int id, int type) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query del name rule group ==========", phone);
            cs = con.prepareCall("{? = CALL VRBT.DEL_SETTING_PHONE(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.setInt(3, type);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query del name rule group done ==========", phone);
            logger.info("[{}] ========== end query del name rule group ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query del name rule group: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query del name rule group fail ==========", phone);
        logger.info("[{}] ========== end query del name rule group ==========", phone);
        return false;
    }

    @Override
    public boolean groupSettingAdd(String phone, int id, String phoneIn, String sub) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query add phone to group setting ==========", phone);
            cs = con.prepareCall("{? = CALL VRBT.ADD_PHONE_GROUP_SETTING(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, sub);
            cs.setInt(3, id);
            cs.setString(4, phoneIn);
            cs.setString(5, phone);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query add phone to group setting done ==========", phone);
            logger.info("[{}] ========== end query add phone to group setting ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error add phone to group setting: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query add phone to group setting fail ==========", phone);
        logger.info("[{}] ========== end query add phone to group setting ==========", phone);
        return false;
    }

    @Override
    public boolean groupPhoneDel(String phone, int ruleId, int contactId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query del phone group to setting ==========", phone);
            cs = con.prepareCall("{? = CALL VRBT.DEL_GROUP_PHONE(?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, phone);
            cs.setInt(3, ruleId);
            cs.setInt(4, contactId);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query del phone group to setting done ==========", phone);
            logger.info("[{}] ========== end query del phone group to setting ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query del phone group to setting: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query del phone group to setting fail ==========", phone);
        logger.info("[{}] ========== end query del phone group to setting ==========", phone);
        return false;
    }

    @Override
    public List<PhoneGroupResponse> groupPhoneList(String phone, int ruleId) {
        List<PhoneGroupResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get list phone group ==========", phone);
            cs = con.prepareCall("{CALL VRBT.GET_LIST_PHONE_GROUP(?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, ruleId);
            rs = cs.executeQuery();
            while (rs.next()) {
                if (rs.getString("phone") != null) {
                    PhoneGroupResponse data = new PhoneGroupResponse();
                    data.setContactId(rs.getInt("contact_id"));
                    data.setId(rs.getInt("rule_id"));
                    data.setPhone(rs.getString("phone"));
                    data.setSub(rs.getString("sub"));
                    result.add(data);
                }
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query get list phone group: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] output data: {}", phone, result);
        logger.info("[{}] ========== end query get list phone group ==========", phone);
        return result;
    }

    @Override
    public VideoWait phoneSettingDetail(String phone, int ruleId, int type) {
        VideoWait data = new VideoWait();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String videoIds = "";
        int timeType = 0;
        int countVideo = 0;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get detail rule ==========", phone);
            cs = con.prepareCall("{CALL VRBT.GET_RULE_INFO(?)}");
            cs.setInt(1, ruleId);
            rs = cs.executeQuery();
            if (rs.next()) {
                timeType = rs.getInt("time_type");
                data.setId(rs.getInt("rule_id"));
                data.setType(rs.getInt("type_name"));
                data.setTimeType(rs.getInt("time_type"));
                if (timeType == 0) {
                    data.setTimeFrom("");
                    data.setTimeTo("");
                } else if (timeType == 1) {
                    data.setTimeFrom(String.format("%02d", rs.getInt("start_day")) + "/" + String.format("%02d", rs.getInt("start_month")) + "/" + String.format("%02d", rs.getInt("start_year")) + " " + String.format("%02d", rs.getInt("start_hour")) + ":" + String.format("%02d", rs.getInt("start_minute")));
                    data.setTimeTo(String.format("%02d", rs.getInt("stop_day")) + "/" + String.format("%02d", rs.getInt("stop_month")) + "/" + String.format("%02d", rs.getInt("stop_year")) + " " + String.format("%02d", rs.getInt("stop_hour")) + ":" + String.format("%02d", rs.getInt("stop_minute")));
                } else if (timeType == 2) {
                    data.setTimeFrom(String.format("%02d", rs.getInt("start_hour")) + ":" + String.format("%02d", rs.getInt("start_minute")));
                    data.setTimeTo(String.format("%02d", rs.getInt("stop_hour")) + ":" + String.format("%02d", rs.getInt("stop_minute")));
                } else if (timeType == 3) {
                    data.setTimeFrom(String.format("%02d", rs.getInt("start_day")));
                    data.setTimeTo(String.format("%02d", rs.getInt("stop_day")));
                } else if (timeType == 4) {
                    data.setTimeFrom(String.format("%02d", rs.getInt("start_month")));
                    data.setTimeTo(String.format("%02d", rs.getInt("stop_month")));
                }
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (cs != null) {
                cs.close();
                cs = null;
            }

            cs = con.prepareCall("{CALL VRBT.GET_RULE_ITEM(?)}");
            cs.setInt(1, ruleId);
            rs = cs.executeQuery();
            while (rs.next()) {
                if (countVideo > 0) {
                    videoIds = videoIds + "," + rs.getInt("item_id");
                } else {
                    videoIds = videoIds + rs.getInt("item_id");
                }
                countVideo++;
            }
            data.setVideoIds(videoIds);
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query get detail rule: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] output data: {}", phone, data);
        logger.info("[{}] ========== end query get detail rule ==========", phone);
        if (data.getVideoIds().isEmpty()) {
            return null;
        }
        return data;
    }

    @Override
    public boolean saveAvatar(String phone, String id, String filePathAvatar) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query save avatar for user ==========", phone);
            cs = con.prepareCall("{? = CALL WAP_AUTH.SAVE_AVATAR(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, Integer.parseInt(id));
            cs.setString(3, filePathAvatar);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query save avatar for user done ==========", phone);
            logger.info("[{}] ========== end query save avatar for user ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query save avatar for user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query save avatar for user fail ==========", phone);
        logger.info("[{}] ========== end query save avatar for user ==========", phone);
        return false;
    }

    @Override
    public boolean updateUsername(String phone, String id, String name) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query update username for user ==========", phone);
            cs = con.prepareCall("{? = CALL WAP_AUTH.UPDATE_USERNAME(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, Integer.parseInt(id));
            cs.setString(3, name);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query update username for user done ==========", phone);
            logger.info("[{}] ========== end query update username for user ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query update username for user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query update username for user fail ==========", phone);
        logger.info("[{}] ========== end query update username for user ==========", phone);
        return false;
    }

    @Override
    public boolean updatePassword(String phone, String id, String passwordNewEncoder) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query update password for user ==========", phone);
            cs = con.prepareCall("{? = CALL WAP_AUTH.UPDATE_PASSWORD(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, Integer.parseInt(id));
            cs.setString(3, passwordNewEncoder);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query update password for user done ==========", phone);
            logger.info("[{}] ========== end query update password for user ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query update password for user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query update password for user fail ==========", phone);
        logger.info("[{}] ========== end query update password for user ==========", phone);
        return false;
    }

    @Override
    public List<BonusHistory> bonusHistory(String phone) {
        List<BonusHistory> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query get list bonus history ==========", phone);
            cs = con.prepareCall("{CALL VRBT.GET_BONUS_HISTORY_BY_PHONE(?)}");
            cs.setString(1, phone);
            rs = cs.executeQuery();
            while (rs.next()) {
                BonusHistory data = new BonusHistory();
                data.setKeyTime(rs.getString("keytime"));
                data.setContent(rs.getString("content"));
                data.setTime(rs.getString("time"));
                data.setPrice(rs.getLong("price"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error get list bonus history: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get list bonus history ==========", phone);
        return result;
    }

    @Override
    public boolean updatePhoneUser(String phone, String id, String phoneNew) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query update phone for user ==========", phone);
            cs = con.prepareCall("{? = CALL WAP_AUTH.UPDATE_PHONE_USER(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, Integer.parseInt(id));
            cs.setString(3, phoneNew);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query update phone for user done ==========", phone);
            logger.info("[{}] ========== end query update phone for user ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query update phone for user: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query update phone for user fail ==========", phone);
        logger.info("[{}] ========== end query update phone for user ==========", phone);
        return false;
    }

    @Override
    public VideoDetailResponse findVideoGiftById(int videoId, int userId, String phone) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        VideoDetailResponse data = new VideoDetailResponse();
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_DETAIL_VIDEO_GIFT(?,?,?)}");
            cs.setInt(1, videoId);
            cs.setInt(2, userId);
            cs.setString(3, phone);
            rs = cs.executeQuery();
            logger.info("[{}] ========== start query get detail video gift. ==========", phone);
            if (rs.next()) {
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                data.setCreatorAvatar(creatorAvatar);
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name") : "";
                data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : cpName);
                String fileName = rs.getString("file_name") != null ? rs.getString("file_name").replace("//u02", domainFile) : "";
                data.setFileName(fileName);
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                data.setTitle(rs.getString("title"));
                data.setPrice(rs.getLong("price"));
                data.setNumberView(rs.getInt("number_view"));
                data.setNumberLike(rs.getInt("number_like"));
                data.setNumberShare(rs.getInt("number_share"));
                data.setSupplier(rs.getString("supplier"));
                data.setIsLike(rs.getInt("is_like"));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setPhoneGift(rs.getString("phone_gift"));
            }
        } catch (Exception e) {
            logger.error("[{}] Error query get detail video gift: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, data);
        logger.info("[{}] ========== end query get detail video gift. ==========", phone);
        return data;
    }

    @Override
    public boolean updateMusic(int id, String phone) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ========== start query update count music ==========", phone);
            cs = con.prepareCall("{? = CALL WAP_AUTH.UPDATE_NUMBER_USE_MUSIC(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.execute();
            con.commit();
            logger.info("[{}] ========== query update count music done ==========", phone);
            logger.info("[{}] ========== end query update count music ==========", phone);
            return true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query update count music: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== query update count music fail ==========", phone);
        logger.info("[{}] ========== end query update count music ==========", phone);
        return false;
    }
}
