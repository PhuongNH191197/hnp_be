package com.elcom.plus.mecall.service.impl;

import com.elcom.plus.common.util.constant.ResponseCode;
import com.elcom.plus.common.util.constant.ResponseMessage;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.mecall.controller.CategoryController;
import com.elcom.plus.mecall.dao.CategoryDao;
import com.elcom.plus.mecall.dao.CommonDao;
import com.elcom.plus.mecall.dto.response.banner.BannerResponse;
import com.elcom.plus.mecall.dto.response.category.*;
import com.elcom.plus.mecall.dto.response.channel.ChannelResponse;
import com.elcom.plus.mecall.dto.response.cp.CpResponse;
import com.elcom.plus.mecall.dto.response.hashtag.HashtagResponse;
import com.elcom.plus.mecall.dto.response.music.MusicResponse;
import com.elcom.plus.mecall.dto.response.top.VideoTopAll;
import com.elcom.plus.mecall.dto.response.top.VideoTopResponse;
import com.elcom.plus.mecall.dto.response.video.CheckVideo;
import com.elcom.plus.mecall.entity.Category;
import com.elcom.plus.mecall.service.CategoryService;
import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryDao categoryDAO;
    private CommonDao commonDao;

    @Override
    public List<CategoryResponse> listCategory() {
        return categoryDAO.findAll();
    }

    @Override
    public List<ItemVideoResponse> findItemTrending(String phone, int userId, int videoId, int skip, int take) {
        List<ItemVideoResponse> listVideoTrending = new ArrayList<>();
        try {
            listVideoTrending = categoryDAO.findItemTrending(phone, userId, videoId, skip, take);
            if (videoId > 0) {
                ItemVideoResponse videoExistedIndex = listVideoTrending.stream().filter(obj -> obj.getItemId() == videoId).findFirst().orElse(null);
                if (videoExistedIndex != null) {
                    listVideoTrending.remove(videoExistedIndex);
                    listVideoTrending.add(0, videoExistedIndex);
                } else {
                    ItemVideoResponse videoInfo = categoryDAO.findVideoById(phone, videoId, userId);
                    if (videoInfo != null) {
                        listVideoTrending.remove(listVideoTrending.size() - 1);
                        listVideoTrending.add(0, videoInfo);
                    }
                }
            } else {
                return listVideoTrending;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listVideoTrending;
    }

    @Override
    public Response findItemByCategory(String phone, int userId, int videoId, String typeSearch, String keySearch, int skip, int take) {
        Response response = new Response();
//        ItemVideoResponse videoDetail = null;
        ListItemVideoResponse videoCategory = new ListItemVideoResponse();
        List<ItemVideoResponse> listVideoCategory = new ArrayList<>();
        CategoryData objVideo = new CategoryData();
        Set<ItemVideoResponse> setListItemId = new LinkedHashSet<ItemVideoResponse>();

//        if (videoId > 0) {
//            videoDetail = categoryDAO.findVideoById(phone, videoId, userId);
//        }

        if ("category".equalsIgnoreCase(typeSearch)) {
            videoCategory = categoryDAO.findItemByCategory(phone, userId, Integer.parseInt(keySearch), videoId, skip, take);
            listVideoCategory = videoCategory.getData();
        } else if ("hashtag".equalsIgnoreCase(typeSearch)) {
            videoCategory = categoryDAO.findVideoByHashtag(phone, userId, keySearch, videoId, skip, take);
            listVideoCategory = videoCategory.getData();
        } else if ("search".equalsIgnoreCase(typeSearch)) {
            videoCategory = categoryDAO.searchVideo(phone, userId, keySearch, videoId, 0, skip, take);
            listVideoCategory = videoCategory.getData();
        } else if ("channel".equalsIgnoreCase(typeSearch)) {
            videoCategory = categoryDAO.getListVideoOfChannel(phone, userId, keySearch, videoId, 0, skip, take);
            listVideoCategory = videoCategory.getData();
        } else if ("cp".equalsIgnoreCase(typeSearch)) {
            videoCategory = categoryDAO.getListVideoOfCp(phone, userId, keySearch, videoId, 0, skip, take);
            listVideoCategory = videoCategory.getData();
        }

        objVideo.setListVideoData(listVideoCategory);
//        objVideo.setVideoDetail(videoDetail);
        objVideo.setSkipIndex(videoCategory.getSkipIndex());
        response.setCode(0);
        response.setMessage(ResponseMessage.SUCCESS);
        response.setData(objVideo);
        logger.info("[{}]==========End get video info==========", phone);
        return response;
    }

    @Override
    public Response checkVideoById(String phone, int videoId) {
        Response response = new Response();
        CheckVideo checkVideo = new CheckVideo();
        checkVideo = categoryDAO.checkVideoById(phone, videoId);
        response.setCode(0);
        response.setMessage(ResponseMessage.SUCCESS);
        response.setData(checkVideo);
        return response;
    }

    @Override
    public List<BannerResponse> findBanner() {
        return categoryDAO.findBanner();
    }

    @Override
    public List<HashtagResponse> findHashtag(int skip, int take) {
        return categoryDAO.findHashtag(skip, take);
    }

    @Override
    public VideoTopAll findAllTopVideo(int userId) {
        List<VideoTopResponse> top1 = categoryDAO.findTopVideoByType(userId, 1, 0, 20);
        List<VideoTopResponse> top2 = categoryDAO.findTopVideoByType(userId, 2, 0, 20);
        List<VideoTopResponse> top3 = categoryDAO.findTopVideoByType(userId, 3, 0, 20);
        return new VideoTopAll(top1, top2, top3);
    }

    @Override
    public List<VideoTopResponse> findTopVideoByType(int userId, int type, int skip, int take) {
        return categoryDAO.findTopVideoByType(userId, type, skip, take);
    }

    @Override
    public List<ChannelResponse> findChannel(int skip, int take) {
        return categoryDAO.findChannel(skip, take);
    }

    @Override
    public List<CpResponse> findCp(int skip, int take) {
        return categoryDAO.findCp(skip, take);
    }

    @Override
    public CategoryGroupResponse findCategoryGroup() {
        List<CategoryResponse> data = categoryDAO.findCategory();
        String backgroundUrl = categoryDAO.findBackgroundUrl();
        return new CategoryGroupResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, backgroundUrl, data);
    }

    @Override
    public List<MusicResponse> getMusics(String phone, int userId) {
        if (userId == 0) {
            logger.info("[{}]========== Users need to login to get music ==========", phone);
            return new ArrayList<>();
        }
        return categoryDAO.getMusics(phone);
    }

    @Override
    public List<ItemVideoResponse> getListVideoByType(String phone, int userId, int type, String key, int order, int skip, int take) {
        List<ItemVideoResponse> hashtagData = new ArrayList<>();
        Set<ItemVideoResponse> setListItemId = new LinkedHashSet<ItemVideoResponse>();
        if (type == 0) {
            // search video
            hashtagData = categoryDAO.searchVideo(phone, userId, key, 0, order, skip, take).getData();
        } else if (type == 1) {
            // get list video by channel id
            hashtagData = categoryDAO.getListVideoOfChannel(phone, userId, key, 0, order, skip, take).getData();
        } else if (type == 2) {
            // get detail hashtag
            hashtagData = categoryDAO.findHashtagByName(phone, userId, key, order, skip, take);
        } else if (type == 3) {
            // get list video by cp id
            hashtagData = categoryDAO.getListVideoOfCp(phone, userId, key, 0, order, skip, take).getData();
        }
        return hashtagData;
    }
}
