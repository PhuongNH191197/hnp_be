package com.elcom.plus.mecall.dao.impl;

import com.elcom.plus.mecall.config.db.ConnectionManager;
import com.elcom.plus.mecall.config.log.Log;
import com.elcom.plus.mecall.controller.CategoryController;
import com.elcom.plus.mecall.dao.CategoryDao;
import com.elcom.plus.mecall.dto.response.banner.BannerResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryResponse;
import com.elcom.plus.mecall.dto.response.category.ListItemVideoResponse;
import com.elcom.plus.mecall.dto.response.category.VideoDetailResponse;
import com.elcom.plus.mecall.dto.response.channel.ChannelResponse;
import com.elcom.plus.mecall.dto.response.cp.CpResponse;
import com.elcom.plus.mecall.dto.response.hashtag.HashtagResponse;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.music.MusicResponse;
import com.elcom.plus.mecall.dto.response.top.VideoTopResponse;
import com.elcom.plus.mecall.dto.response.video.CheckVideo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class CategoryDaoImpl extends AbstractDao implements CategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public List<CategoryResponse> findAll() {
        List<CategoryResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("========== start query get category ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_ALL_CATEGORY(?)}");
            cs.setInt(1, 0);
            rs = cs.executeQuery();
            while (rs.next()) {
                CategoryResponse category = new CategoryResponse();
                category.setId(rs.getLong("cat_id"));
                category.setOrder(rs.getLong("order_index"));
                category.setName(rs.getString("name"));
                category.setPositionDesign(rs.getInt("position_design_id"));
                category.setGroupCode(rs.getString("group_code"));
                category.setBannerPositionId(rs.getInt("banner_position_id"));
                result.add(category);
            }
        } catch (Exception e) {
            logger.error("Error get category: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("========== output data ==========: {}", result);
        logger.info("========== end query get category ==========");
        return result;
    }

    @Override
    public List<ItemVideoResponse> findItemTrending(String phone, int userId, int videoId, int skip, int take) {
        List<ItemVideoResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            logger.info("[{}] ========== start query get video trending ==========", phone);
            cs = con.prepareCall("{CALL GET_VIDEO_TRENDING(?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, videoId);
            cs.setInt(3, skip);
            cs.setInt(4, take);
            rs = cs.executeQuery();
            while (rs.next()) {
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                result.add(data);
            }
            logger.info("[{}] ========== output data ==========: {}", phone, result);
            logger.info("[{}] ========== end query get video trending ==========", phone);
            return result;
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query video trending: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get video trending ==========", phone);
        return result;
    }

    @Override
    public ListItemVideoResponse findItemByCategory(String phone, int user_id, int id, int videoID, int skip, int take) {
        List<ItemVideoResponse> result = new ArrayList<>();
        ListItemVideoResponse listDataCategory = new ListItemVideoResponse();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            logger.info("[{}] ========== start query get video ==========", phone);
            cs = con.prepareCall("{CALL GET_ITEM_BY_CATEGORY(?,?,?,?,?)}");
            cs.setInt(1, user_id);
            cs.setInt(2, id);
            cs.setInt(3, videoID);
            cs.setInt(4, skip);
            cs.setInt(5, take);
            rs = cs.executeQuery();
            while (rs.next()) {
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                if (listDataCategory.getSkipIndex() < 0) {
                    listDataCategory.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
            logger.info("[{}] ========== output data ==========: {}", phone, result);
            logger.info("[{}] ========== end query get video ==========", phone);
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query video: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        listDataCategory.setData(result);
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get video ==========", phone);
        return listDataCategory;
    }

    @Override
    public List<BannerResponse> findBanner() {
        List<BannerResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("========== start query get list banner ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_ALL_BANNER()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                BannerResponse data = new BannerResponse();
                data.setId(rs.getInt("id"));
                data.setTitle(rs.getString("title"));
                data.setDescription(rs.getString("description"));
                data.setPositionId(rs.getInt("position_id"));
                String mediaSource = rs.getString("media_source") != null ? rs.getString("media_source").replace("//u02", domainFile) : "";
                data.setMediaSource(mediaSource);
                data.setVideoId(rs.getInt("video_id"));
                data.setType(rs.getInt("type"));
                data.setMediaLink(rs.getString("media_link"));
                data.setCatId(rs.getInt("cat_id"));
                result.add(data);
            }
            logger.info("[] ========== output data ==========: {}", result);
            logger.info("[] ========== end query get list banner ==========");
            return result;
        } catch (Exception e) {
            logger.error("Error get list banner: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query get list banner ==========");
        return result;
    }

    @Override
    public List<HashtagResponse> findHashtag(int skip, int take) {
        List<HashtagResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query get list hashtag ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_ALL_HASHTAG(?,?,?)}");
            cs.setInt(1, 0);
            cs.setInt(2, skip);
            cs.setInt(3, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                HashtagResponse data = new HashtagResponse();
                data.setId(rs.getInt("id"));
                data.setHashtag(rs.getString("name"));
                result.add(data);
            }
            logger.info("[] ========== output data ==========: {}", result);
            logger.info("[] ========== end query get list hashtag ==========");
            return result;
        } catch (Exception e) {
            logger.error("Error get list hashtag: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query get list hashtag ==========");
        return result;
    }

    @Override
    public List<VideoTopResponse> findTopVideoByType(int userId, int type, int skip, int take) {
        List<VideoTopResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query video top ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_TOP_VIDEO(?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, type);
            cs.setInt(3, skip);
            cs.setInt(4, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                VideoTopResponse data = new VideoTopResponse();
                data.setId(rs.getInt("id"));
                data.setType(rs.getInt("top_type"));
                data.setOrder(rs.getInt("order_index"));
                data.setItemId(rs.getInt("video_id"));
                data.setFileName(rs.getString("file_name"));
                String thumbnail = rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "";
                data.setThumbnail(thumbnail);
                String filePath = rs.getString("file_path") != null ? rs.getString("file_path").replace("//u02", domainFile) : "";
                data.setFilePath(filePath);
                data.setCreatorAvatar(rs.getString("creator_avatar") != null ? rs.getString("creator_avatar").replace("//u02", domainFile) : "");
                data.setCreditName(rs.getString("credit_name"));
                data.setTimeSet(rs.getTime("time_set"));
                data.setNumberView(rs.getInt("number_view"));
                data.setNumberLike(rs.getInt("number_like"));
                data.setNumberShare(rs.getInt("number_share"));
                data.setTitle(rs.getString("title"));
                data.setPrice(rs.getLong("price"));
                String cpName = rs.getString("cp_name") != null ? rs.getString("cp_name"): "";
                data.setUsername(cpName.equals("END_USER") ? rs.getString("credit_name") : cpName);
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setIsLike(rs.getInt("is_like"));
                data.setCatId(rs.getInt("cat_id"));
                result.add(data);
            }
            logger.info("[] ========== output data ==========: {}", result);
            logger.info("[] ========== end query video top ==========");
            return result;
        } catch (Exception e) {
            logger.error("Error get video top: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query video top ==========");
        return result;
    }

    @Override
    public List<ChannelResponse> findChannel(int skip, int take) {
        List<ChannelResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query channel ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_CHANNEL(?,?)}");
            cs.setInt(1, skip);
            cs.setInt(2, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ChannelResponse data = new ChannelResponse();
                data.setId(rs.getInt("id"));
                data.setName(rs.getString("chanel_name"));
                data.setOrderIndex(rs.getInt("order_index"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                result.add(data);
            }
            logger.info("[] ========== output data ==========: {}", result);
            logger.info("[] ========== end query channel ==========");
            return result;
        } catch (Exception e) {
            logger.error("Error get channel: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query channel ==========");
        return result;
    }

    @Override
    public List<CpResponse> findCp(int skip, int take) {
        List<CpResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query CP ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_CP(?,?)}");
            cs.setInt(1, skip);
            cs.setInt(2, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                CpResponse data = new CpResponse();
                data.setId(rs.getInt("id"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setUsername(rs.getString("username"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("Error get CP: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query CP ==========");
        return result;
    }

    @Override
    public List<CategoryResponse> findCategory() {
        List<CategoryResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query category ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_CATEGORY()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                CategoryResponse category = new CategoryResponse();
                category.setId(rs.getLong("cat_id"));
                category.setOrder(rs.getLong("cat_order"));
                category.setName(rs.getString("cat_name"));
                result.add(category);
            }
        } catch (Exception e) {
            logger.error("Error get category: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: {}", result);
        logger.info("[] ========== end query category ==========");
        return result;
    }

    @Override
    public String findBackgroundUrl() {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query background url ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_BACKGROUND_URL()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                String url = rs.getString("image_path") != null ? rs.getString("image_path").replace("//u02", domainFile) : "";
                logger.info("[] ========== output data ==========: {}", url);
                logger.info("[] ========== end query background url ==========");
                return url;
            }
        } catch (Exception e) {
            logger.error("Error get background url: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[] ========== output data ==========: null");
        logger.info("[] ========== end query background url ==========");
        return null;
    }

    @Override
    public List<MusicResponse> getMusics(String phone) {
        List<MusicResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}]========== start query get music ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_MUSIC()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                MusicResponse music = new MusicResponse();
                music.setId(rs.getInt("id"));
                music.setSingerName(rs.getString("singer_name"));
                music.setSongPath(rs.getString("song_path") != null ? rs.getString("song_path").replace("//u02", domainFile) : "");
                music.setSongName(rs.getString("song_name"));
                music.setThumbnail(rs.getString("thumbnail") != null ? rs.getString("thumbnail").replace("//u02", domainFile) : "");
                music.setDuration(rs.getInt("duration"));
                result.add(music);
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error get music: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get music ==========", phone);
        return result;
    }

    @Override
    public ItemVideoResponse findVideoById(String phone, int videoId, int userID) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        ItemVideoResponse data = null;
        try {
            logger.info("[{}]========== start query find video by id ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CMP.GET_DETAIL_VIDEO_BY_ID(?,?)}");
            cs.setInt(1, videoId);
            cs.setInt(2, userID);
            rs = cs.executeQuery();
            if (rs.next()) {
                data = new ItemVideoResponse();

                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error get music: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}]========== output data ==========: {}", phone, data);
        logger.info("[{}]========== end query find video by id ==========", phone);
        return data;
    }

    @Override
    public List<ItemVideoResponse> findHashtagByName(String phone, int userId, String key, int order, int skip, int take) {
        List<ItemVideoResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}]========== start query find hashtag by name ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CMP.GET_HASHTAG_BY_NAME(?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setString(2, key);
            cs.setInt(3, 0);
            cs.setInt(4, skip);
            cs.setInt(5, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error get hashtag by name: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}]========== output data ==========: {}", phone, result);
        logger.info("[{}]========== start query find hashtag by name ==========", phone);
        return result;
    }

    @Override
    public ListItemVideoResponse searchVideo(String phone, int userId, String key, int videoId, int order, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<ItemVideoResponse> result = new ArrayList<>();
        ListItemVideoResponse listDataCategory = new ListItemVideoResponse();
        try {
            logger.info("[{}]========== start query search video ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CMP.GET_VIDEO_SEARCH(?,?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setString(2, key);
            cs.setInt(3, videoId);
            cs.setInt(4, order);
            cs.setInt(5, skip);
            cs.setInt(6, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                if (listDataCategory.getSkipIndex() < 0) {
                    listDataCategory.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error query search video: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        listDataCategory.setData(result);
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== end query search video ==========", phone);
        return listDataCategory;
    }

    @Override
    public ListItemVideoResponse getListVideoOfChannel(String phone, int userId, String key, int videoId, int order, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<ItemVideoResponse> result = new ArrayList<>();
        ListItemVideoResponse listDataCategory = new ListItemVideoResponse();
        try {
            logger.info("[{}]========== start query get list video of channel ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CMP.GET_VIDEO_CHANNEL_DETAIL(?,?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, Integer.parseInt(key));
            cs.setInt(3, videoId);
            cs.setInt(4, order);
            cs.setInt(5, skip);
            cs.setInt(6, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setChannelId(rs.getInt("channel_id"));
                if (listDataCategory.getSkipIndex() < 0) {
                    listDataCategory.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error get list video channel detail: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        listDataCategory.setData(result);
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== end query get list video of channel ==========", phone);
        return listDataCategory;
    }

    @Override
    public ListItemVideoResponse getListVideoOfCp(String phone, int userId, String key, int videoId, int order, int skip, int take) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        List<ItemVideoResponse> result = new ArrayList<>();
        ListItemVideoResponse listDataCategory = new ListItemVideoResponse();
        try {
            logger.info("[{}]========== start query get list video of cp ==========", phone);
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{CALL CMP.GET_VIDEO_BY_CP(?,?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setInt(2, Integer.parseInt(key));
            cs.setInt(3, videoId);
            cs.setInt(4, order);
            cs.setInt(5, skip);
            cs.setInt(6, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setCpId(rs.getInt("cp_id"));
                if (listDataCategory.getSkipIndex() < 0) {
                    listDataCategory.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("["+ phone + "] Error get list video by CP: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        listDataCategory.setData(result);
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}]========== end query get list video of cp ==========", phone);
        return listDataCategory;
    }

    @Override
    public CheckVideo checkVideoById(String phone, int videoId) {
        CheckVideo checkVideo = new CheckVideo();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[] ========== start query check video by id ==========");
            con = ConnectionManager.getConnection();
            cs = con.prepareCall("{? = call CHECK_VIDEO_BY_ID(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, videoId);
            cs.execute();
            con.commit();
            if (cs.getInt(1) > 0) {
                checkVideo.setStatus(true);
            } else {
                checkVideo.setStatus(false);
            }
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query check video by id: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ========== output data ==========: {}", phone, checkVideo.getStatus());
        logger.info("[{}] ========== end query check video by id ==========", phone);
        return checkVideo;
    }

    @Override
    public ListItemVideoResponse findVideoByHashtag(String phone, int userId, String hashtagInput, int videoID, int skip, int take) {
        List<ItemVideoResponse> result = new ArrayList<>();
        ListItemVideoResponse listDataCategory = new ListItemVideoResponse();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            logger.info("[{}] ========== start query get video by hashtag ==========", phone);
            cs = con.prepareCall("{CALL GET_HASHTAG_BY_NAME(?,?,?,?,?)}");
            cs.setInt(1, userId);
            cs.setString(2, hashtagInput);
            cs.setInt(3, videoID);
            cs.setInt(4, skip);
            cs.setInt(5, take);
            rs = cs.executeQuery();
            while (rs.next()) {
                ItemVideoResponse data = new ItemVideoResponse();
                data.setCatId(rs.getLong("cat_id"));
                String hashtag = rs.getString("hashtag");
                String[] hashtags = StringUtils.isNotBlank(hashtag) ? Arrays.stream(hashtag.split("#")).filter(e -> e.trim().length() > 0).map(s -> "#" + trim(s)).toArray(String[]::new) : new String[0];
                data.setHashtags(Arrays.asList(hashtags));
                data.setItemId(rs.getLong("id"));
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
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                if (listDataCategory.getSkipIndex() < 0) {
                    listDataCategory.setSkipIndex(rs.getInt("skip"));
                }
                result.add(data);
            }
            logger.info("[{}] ========== output data ==========: {}", phone, result);
            logger.info("[{}] ========== end query get video by hashtag ==========", phone);
        } catch (Exception e) {
            logger.error("[" + phone + "] Error query video by hashtag: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        listDataCategory.setData(result);
        logger.info("[{}] ========== output data ==========: {}", phone, result);
        logger.info("[{}] ========== end query get video by hashtag ==========", phone);
        return listDataCategory;
    }
}
