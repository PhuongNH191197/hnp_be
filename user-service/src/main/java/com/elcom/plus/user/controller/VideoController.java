package com.elcom.plus.user.controller;

import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.response.ResponseUtil;
import com.elcom.plus.user.client.MeCallClient;
import com.elcom.plus.user.dto.request.UploadVideo;
import com.elcom.plus.user.dto.request.VideoRequest;
import com.elcom.plus.user.dto.response.MyVideoResponse;
import com.elcom.plus.user.service.VideoService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/api/users/video")
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private VideoService videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> uploadVideo(@RequestParam(name = "fileVideo") MultipartFile fileVideo,
                                                @RequestParam(name = "fileImage") MultipartFile fileImage,
                                                @RequestPart(name = "videoInfo") String videoInfo,
                                                @RequestHeader("phone") String phone,
                                                @RequestHeader("id") String id) throws IOException {
        logger.info("[{}] ========== API POST: /rest/api/users/video ==========", phone);
        logger.info("[{}] ========== input data ==========: videoInfo: {}, fileVideo: {}, fileImage: {}", phone, videoInfo, fileVideo, fileImage);
//        Response data = videoService.uploadVideoTest(fileVideo, fileImage, videoInfo, id);
//        return new ResponseEntity<>(data, HttpStatus.OK);
        Response data = videoService.uploadVideo(fileVideo, fileImage, videoInfo, id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Response> myVideo(@RequestHeader("id") String id,
                                                @RequestHeader("phone") String phone,
                                                @RequestParam("type") int type,
                                                @RequestParam("videoId") int videoId,
                                                @RequestParam("skip") int skip,
                                                @RequestParam("take") int take) {
        logger.info("[{}] ========== API GET: /rest/api/users/video ==========", phone);
        logger.info("[{}] ========== input data ==========: type: {}, videoId: {}, skip: {}, take: {}", phone, type, videoId, skip, take);
        Response data = videoService.myVideo(id, type, videoId, skip, take);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Response> deleteVideo(@RequestHeader("id") String id, @RequestHeader("phone") String phone, @RequestBody VideoRequest videoRequest) {
        logger.info("[{}] ========== API PUT: /rest/api/users/video ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, videoRequest);
        Response data = videoService.deleteVideo(id, phone, videoRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
