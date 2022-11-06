package com.elcom.plus.user.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.dto.request.UploadVideo;
import com.elcom.plus.user.dto.request.VideoRequest;
import com.elcom.plus.user.dto.response.MyVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface VideoService {
    Response myVideo(String id, int type, int videoId, int skip, int take);
//    Response uploadVideoTest(MultipartFile fileVideo, MultipartFile fileImage, String videoInfo, String id);
    Response uploadVideo(MultipartFile fileVideo, MultipartFile fileImage, String videoInfo, String id);
    Response deleteVideo(String id, String phone, VideoRequest videoRequest);
}
