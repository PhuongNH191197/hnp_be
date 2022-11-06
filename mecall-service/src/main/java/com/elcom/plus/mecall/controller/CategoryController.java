package com.elcom.plus.mecall.controller;

import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.response.HeaderResponse;
import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.response.ResponseUtil;
import com.elcom.plus.mecall.dto.response.banner.BannerResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryGroupResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryResponse;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.channel.ChannelResponse;
import com.elcom.plus.mecall.dto.response.cp.CpResponse;
import com.elcom.plus.mecall.dto.response.hashtag.HashtagResponse;
import com.elcom.plus.mecall.dto.response.music.MusicResponse;
import com.elcom.plus.mecall.dto.response.top.VideoTopAll;
import com.elcom.plus.mecall.dto.response.top.VideoTopResponse;
import com.elcom.plus.mecall.service.CategoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rest/api/mecall")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/category")
    public ResponseEntity<ListResponse> findCategory() {
        logger.info("[]========== API GET: /rest/api/mecall/category ==========");
        logger.info("[]========== not input data ==========");
        List<CategoryResponse> data = categoryService.listCategory();
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<ListResponse> findItemTrending(HttpServletRequest request,
                                                         @RequestParam(value = "videoId") int videoId,
                                                         @RequestParam(value = "skip") int skip,
                                                         @RequestParam(value = "take") int take) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/trending ==========", phone);
        logger.info("[{}]========== input data ==========: videoId: {}, skip: {}, take: {}, userId: {}", phone, videoId, skip, take, userId);
        List<ItemVideoResponse> data = categoryService.findItemTrending(phone, userId, videoId, skip, take);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/video")
    public ResponseEntity<Response> findItemByCategory(HttpServletRequest request,
                                                       @RequestParam(value = "typeSearch") String typeSearch,
                                                       @RequestParam(value = "keySearch") String keySearch,
                                                       @RequestParam(value = "videoId") int videoId,
                                                       @RequestParam(value = "skip") int skip,
                                                       @RequestParam(value = "take") int take) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/video ==========", phone);
        logger.info("[{}]========== input data ==========: typeSearch: {}, keySearch: {}, videoId: {}, skip: {}, take: {}, userId: {}", phone, typeSearch, keySearch, videoId, skip, take, userId);
        Response data = categoryService.findItemByCategory(phone, userId, videoId, typeSearch, keySearch, skip, take);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/check-video")
    public ResponseEntity<Response> checkVideoById(HttpServletRequest request,
                                                       @RequestParam(value = "videoId") int videoId) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/check-video ==========", phone);
        logger.info("[{}]========== input data ==========: videoId: {}", phone, videoId);
        Response data = categoryService.checkVideoById(phone, videoId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/banner")
    public ResponseEntity<ListResponse> findBanner() {
        logger.info("[]========== API GET: /rest/api/mecall/banner ==========");
        logger.info("[]========== not input data ==========");
        List<BannerResponse> data = categoryService.findBanner();
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/hashtag")
    public ResponseEntity<ListResponse> findHashtag(@RequestParam(value = "skip") int skip,
                                                    @RequestParam(value = "take") int take) {
        logger.info("[]========== API GET: /rest/api/mecall/hashtag ==========");
        logger.info("[]========== input data ==========: skip: {}, take: {}", skip, take);
        List<HashtagResponse> data = categoryService.findHashtag(skip, take);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/home/common")
    public ResponseEntity<ListResponse> getListVideoByType(HttpServletRequest request,
                                                @RequestParam(value = "type") int type,
                                                @RequestParam(value = "key") String key,
                                                @RequestParam(value = "order") int order,
                                                @RequestParam(value = "skip") int skip,
                                                @RequestParam(value = "take") int take) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            phone = headerData.getPhone();
            userId = headerData.getId();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/home/common ==========", phone);
        logger.info("[{}] ========== input data: type={}, key={}, order={}, skip={}, take={} ==========", phone, type, key, order, skip, take);
        List<ItemVideoResponse> data = categoryService.getListVideoByType(phone, userId, type, key, order, skip, take);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<Response> findAllTopVideo(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/top ==========", phone);
        logger.info("[]========== not input data ==========");
        logger.info("[{}]========== userId: {} ==========", phone, userId);
        VideoTopAll data = categoryService.findAllTopVideo(userId);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

//    @GetMapping("/top/detail")
//    public ResponseEntity<ListResponse> findVideoTopType(HttpServletRequest request,
//                                                    @RequestParam(value = "type") int type,
//                                                     @RequestParam(value = "skip") int skip,
//                                                     @RequestParam(value = "take") int take) {
//        String header = request.getHeader("Authorization");
//        HeaderResponse headerData = CollectionsUtil.getHeader(header);
//        int userId = 0;
//        String phone = "";
//        if (!Objects.isNull(headerData)) {
//            userId = headerData.getId();
//            phone = headerData.getPhone();
//        }
//        logger.info("[{}]========== API GET: /rest/api/mecall/top/detail ==========", phone);
//        logger.info("[{}] ========== input data: type: {}, skip: {}, take: {}", phone, type, skip, take);
//        logger.info("[{}]==========User id: {} ==========", phone, userId);
//        List<VideoTopResponse> data = categoryService.findTopVideoByType(userId, type, skip, take);
//        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
//    }

    @GetMapping("/channel")
    public ResponseEntity<ListResponse> findChannel(@RequestParam(value = "skip") int skip,
                                                    @RequestParam(value = "take") int take) {
        logger.info("[]========== API GET: /rest/api/mecall/channel ==========");
        logger.info("[] ========== input data: skip: {}, take: {}", skip, take);
        List<ChannelResponse> data = categoryService.findChannel(skip, take);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/cp")
    public ResponseEntity<ListResponse> findCp(@RequestParam(value = "skip") int skip,
                                               @RequestParam(value = "take") int take) {
        logger.info("[]========== API GET: /rest/api/mecall/cp ==========");
        logger.info("[] ========== input data: skip: {}, take: {}", skip, take);
        List<CpResponse> data = categoryService.findCp(skip, take);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("/category/group")
    public ResponseEntity<CategoryGroupResponse> findCategoryGroup() {
        logger.info("[]========== API GET: /rest/api/mecall/category/group ==========");
        logger.info("[]========== not input data ==========");
        CategoryGroupResponse data = categoryService.findCategoryGroup();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/music")
    public ResponseEntity<ListResponse> getMusics(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        HeaderResponse headerData = CollectionsUtil.getHeader(header);
        int userId = 0;
        String phone = "";
        if (!Objects.isNull(headerData)) {
            userId = headerData.getId();
            phone = headerData.getPhone();
        }
        logger.info("[{}]========== API GET: /rest/api/mecall/music ==========", phone);
        logger.info("[{}]========== not input data ==========", phone);
        logger.info("[{}]========== User id: ==========: {}", phone, userId);
        List<MusicResponse> data = categoryService.getMusics(phone, userId);
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }
}
