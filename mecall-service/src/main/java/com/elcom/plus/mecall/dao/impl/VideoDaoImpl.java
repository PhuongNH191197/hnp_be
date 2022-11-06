package com.elcom.plus.mecall.dao.impl;

import com.elcom.plus.mecall.config.db.ConnectionManager;
import com.elcom.plus.mecall.controller.CategoryController;
import com.elcom.plus.mecall.dao.VideoDao;
import com.elcom.plus.mecall.dto.request.video.UpdateLikeRequest;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.category.MyVideoResponse;
import com.elcom.plus.mecall.dto.response.category.VideoDetailResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.trim;

@Repository
public class VideoDaoImpl extends AbstractDao implements VideoDao {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Override
    public ItemVideoResponse findVideoById(String phone, int userId, int id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}]========== start query video by id ==========",phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_VIDEO_BY_ID(?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("item_id"));
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String creatorAvatar = rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "";
                data.setCreatorAvatar(creatorAvatar);
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name"): "";
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
                logger.info("[{}]========== output data ==========: {}",phone, data);
                logger.info("[{}]========== end query video by id ==========",phone);
                return data;
            }
        } catch (Exception e) {
            logger.error("Error get video by id: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}]========== output data ==========: null",phone);
        logger.info("[{}]==========end query video by id==========",phone);
        return null;
    }

    @Override
    public void increaseView(int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            logger.info("========== start query increase view ==========");
            logger.info("========== request increase view ==========: {}",videoId);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{? = call UPDATE_VIEW_ITEM(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, videoId);
            cs.execute();
            con.commit();
            logger.info("========== query increase done ==========");
            logger.info("========== end query increase view ==========");
        } catch (Exception e) {
            logger.error("Error increase view: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
    }
    @Override
    public int updateLikeVideo(String phone, int id, int videoId) {
        Connection con = null;
        CallableStatement cs = null;
        int data = 1;
        try {
            logger.info("[{}]========== start query update like video ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{? = call UPDATE_LIKE_ITEM(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.setInt(3, videoId);
            cs.execute();
            con.commit();
            data = cs.getInt(1);
        } catch (Exception e) {
            logger.error("Error update like video: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}]========== output data ==========: {}", phone, data);
        logger.info("========== end query update like video ==========");
        return data;
    }

    @Override
    public List<MyVideoResponse> findByUserId(int userId, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<MyVideoResponse> result = new ArrayList<>();
        try {
            logger.info("========== start query find video by user id ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_VIDEO_TEST(?, ?, ?)}");
            cs.setInt(1, userId);
            cs.setInt(2, skip);
            cs.setInt(3, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                MyVideoResponse data = new MyVideoResponse();
                data.setId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setPrice(rs.getLong("price"));
                data.setSettingId(rs.getInt("setting_id"));
                data.setNumberView(rs.getLong("number_view"));
                data.setNumberLike(rs.getLong("number_like"));
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("Error find video by user id: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("========== output data ==========: {}", result);
        logger.info("========== end query find video by user id ==========");
        return result;
    }
}
