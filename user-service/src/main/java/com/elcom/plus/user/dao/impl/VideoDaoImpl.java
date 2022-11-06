package com.elcom.plus.user.dao.impl;

import com.elcom.plus.user.controller.VideoController;
import com.elcom.plus.user.dto.response.ListMyVideo;
import com.elcom.plus.user.service.impl.UserServiceImpl;
import com.elcom.plus.user.service.impl.VideoServiceImpl;
import org.apache.logging.log4j.LogManager;
import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.dao.VideoDao;
import com.elcom.plus.user.dto.request.UploadVideoEndUser;
import com.elcom.plus.user.dto.response.MyVideoResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.trim;

@Repository
public class VideoDaoImpl extends AbstractDao implements VideoDao {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Override
    public boolean saveVideoUpload(UploadVideoEndUser uploadVideoEndUser) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            System.out.println("uploadVideoEndUser: " + uploadVideoEndUser);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL ADD_VIDEO_UPLOAD(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, uploadVideoEndUser.getUserId());
            cs.setString(2, uploadVideoEndUser.getTitle());
            cs.setString(3, uploadVideoEndUser.getDescription());
            cs.setString(4, uploadVideoEndUser.getPathVideo());
            cs.setString(5, uploadVideoEndUser.getAvatar());
            cs.setString(6, uploadVideoEndUser.getHashtags());
            cs.setInt(7, uploadVideoEndUser.getExpirationDays());
            cs.setInt(8, uploadVideoEndUser.getTimeVideo());
            cs.setInt(9, uploadVideoEndUser.getVertical());
            cs.execute();
            con.commit();
            logger.info("User id: " + uploadVideoEndUser.getUserId() + " get list video buy done.");
            return true;
        } catch (Exception e) {
            logger.error("[phone]: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return false;
    }

    @Override
    public ListMyVideo getVideoUploadByUserId(int userId, String phone, int videoId, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<MyVideoResponse> result = new ArrayList<>();
        ListMyVideo listMyVideo = new ListMyVideo();
        try {
            con = ConnectionManager.getConnection();
            logger.info("[{}]========== Start query get video upload ==========", phone);
            cs = con.prepareCall("{CALL GET_VIDEO_UPLOAD(?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, videoId);
            cs.setInt(3, skip);
            cs.setInt(4, take);
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
                String ogrPath = rs.getString("org_path") != null ? rs.getString("org_path").replace("//u02", domainFile) : "";
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : ogrPath;
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
                data.setStatus(rs.getInt("status"));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setExpireTime(rs.getString("expire_time") != null ? rs.getString("expire_time") : "");
                if (listMyVideo.getSkipIndex() < 0) {
                    listMyVideo.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("[" + phone +"] Error: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== End query get video upload ==========", phone);
        listMyVideo.setData(result);
        return listMyVideo;
    }

    @Override
    public boolean deleteVideoUpload(int id, String phone, String videoId) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        boolean deleteVideo = false;
        try {
            logger.info("[{}]========== Start query delete video upload ==========", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = call DEL_VIDEO_UPLOAD(?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.setString(3, videoId);
            cs.execute();
            con.commit();
            deleteVideo = true;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error delete video upload: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, deleteVideo);
        logger.info("[{}]========== End query delete video upload ==========", phone);
        return deleteVideo;
    }

    @Override
    public ListMyVideo getVideoLikeByUserID(int userId, int videoId, int skip, int take, String phone) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        ListMyVideo listMyVideo = new ListMyVideo();
        List<MyVideoResponse> result = new ArrayList<>();
        try {
            logger.info("[{}]========== Start query get video like ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_VIDEO_LIKE(?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, videoId);
            cs.setInt(3, skip);
            cs.setInt(4, take);
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
                String ogrPath = rs.getString("org_path") != null ? rs.getString("org_path").replace("//u02", domainFile) : "";
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : ogrPath;
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
                data.setUsername(cpName);
                data.setSupplier(rs.getString("supplier"));
                data.setStatus(rs.getInt("status"));
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
            logger.error("[" + phone +"] Error get video like: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== End query get video like ==========", phone);
        listMyVideo.setData(result);
        return listMyVideo;
    }
}
