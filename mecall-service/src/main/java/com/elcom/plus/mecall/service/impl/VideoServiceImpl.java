package com.elcom.plus.mecall.service.impl;

import com.elcom.plus.common.util.constant.ResponseCode;
import com.elcom.plus.common.util.constant.ResponseMessage;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.mecall.controller.CategoryController;
import com.elcom.plus.mecall.dao.CategoryDao;
import com.elcom.plus.mecall.dao.CommonDao;
import com.elcom.plus.mecall.dao.VideoDao;
import com.elcom.plus.mecall.dto.request.video.UpdateLikeRequest;
import com.elcom.plus.mecall.dto.request.video.ViewRequest;
import com.elcom.plus.mecall.dto.response.category.*;
import com.elcom.plus.mecall.service.VideoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class VideoServiceImpl implements VideoService {
    private CommonDao commonDao;
    private VideoDao videoDao;
    private CategoryDao categoryDAO;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Override
    public Response findById(String phone, int userId, int id, Integer category) {
        VideoDataDetailResponse data = new VideoDataDetailResponse();
        ListItemVideoResponse categoryData = new ListItemVideoResponse();
        data.setVideoDetail(videoDao.findVideoById(phone, userId, id));
        if (category > 0) {
            categoryData = categoryDAO.findItemByCategory(phone, userId, category, 0, 0, 10);
        }
        data.setVideoCategory(categoryData.getData());
        if (Objects.isNull(data)) {
            return new Response(ResponseCode.VIDEO_NOT_FOUND, ResponseMessage.VIDEO_NOT_FOUND);
        }
        return new Response(data);
    }

    @Override
    public Response increaseView(String phone, int userId, ViewRequest viewRequest) {
        Response response = new Response();
        if (viewRequest.getVideoId() == 0) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        }
        ItemVideoResponse data = videoDao.findVideoById(phone, userId, viewRequest.getVideoId());
        if (Objects.isNull(data)) {
            return new Response(8002, commonDao.getMessage("USER", 8002));
        }
        videoDao.increaseView(viewRequest.getVideoId());
        return new Response(0, commonDao.getMessage("USER", 9002));
    }

    @Override
    public Response updateLikeVideo(String phone, int id, UpdateLikeRequest updateLikeRequest) {
        Response response = new Response();
        if (updateLikeRequest.getVideoId() == 0) {
            logger.info("[{}]========== Field videoId is required ==========", phone);
            return response;
        }
        if (id == 0) {
            response.setCode(8000);
            response.setMessage(commonDao.getMessage("MECALL", 8000));
        }
        int like = videoDao.updateLikeVideo(phone, id, updateLikeRequest.getVideoId());
        if (like == 0) {
            response.setCode(0);
            response.setMessage(commonDao.getMessage("USER", 9002));
        }
        logger.info("[{}]========== message update like video ==========: {}", phone, response);
        logger.info("[{}]========== end update like video ==========", phone);
        return response;
    }

    @Override
    public List<MyVideoResponse> findByUserId(int userId, int skip, int take) {
        return videoDao.findByUserId(userId, skip, take);
    }
}
