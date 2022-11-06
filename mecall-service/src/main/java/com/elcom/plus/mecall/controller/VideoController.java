package com.elcom.plus.mecall.controller;

import com.elcom.plus.common.util.response.HeaderResponse;
import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.response.ResponseUtil;
import com.elcom.plus.mecall.dao.impl.CategoryDaoImpl;
import com.elcom.plus.mecall.dto.request.video.UpdateLikeRequest;
import com.elcom.plus.mecall.dto.request.video.ViewRequest;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.category.MyVideoResponse;
import com.elcom.plus.mecall.service.VideoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rest/api/mecall/video")
@AllArgsConstructor
public class VideoController {
    private VideoService videoService;
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @GetMapping("/detail")
    public ResponseEntity<Response> findVideoById(HttpServletRequest request,
                                                  @RequestParam(value = "category") Integer category,
                                                  @RequestParam(value = "videoId") int videoId) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/video/detail ==========", phone);
        logger.info("[{}] ========== input data: category: {}, videoId: {}", phone, category, videoId);
        logger.info("[{}]==========User id ==========: {}", phone, userId);
        Response data = videoService.findById(phone, userId, videoId, category);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/view")
    public ResponseEntity<Response> increaseView(HttpServletRequest request, @RequestBody ViewRequest viewRequest) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API PUT: /rest/api/mecall/video/view ==========", phone);
        logger.info("[{}] ========== input data: {}", phone, viewRequest);
        logger.info("[{}]========== User id ==========: {}", phone, userId);
        Response data = videoService.increaseView(phone, userId, viewRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/like")
    public ResponseEntity<Response> updateLikeVideo(HttpServletRequest request, @RequestBody UpdateLikeRequest updateLikeRequest) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int id = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            id = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API PUT: /rest/api/mecall/video/like ==========", phone);
        logger.info("[{}] ========== input data: {}", phone, updateLikeRequest);
        logger.info("[{}]========== User id ==========: {}", phone, id);
        Response data = videoService.updateLikeVideo(phone, id, updateLikeRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public List<MyVideoResponse> myVideo(@PathVariable(value = "userId") int userId,
                                                @RequestParam(value = "skip") Integer skip,
                                                @RequestParam(value = "take") Integer take) {
        logger.info("[]========== API GET: /rest/api/mecall/video/users/{userId} ==========");
        logger.info("[] ========== input data: skip: {}, take: {}", skip, take);
        logger.info("[]========== User id ==========: {}", userId);
        return videoService.findByUserId(userId, skip, take);
    }
}
