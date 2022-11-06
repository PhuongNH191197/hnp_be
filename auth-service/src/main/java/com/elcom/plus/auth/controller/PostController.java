package com.elcom.plus.auth.controller;

import com.elcom.plus.auth.dto.response.PostResponse;
import com.elcom.plus.auth.service.PostService;
import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "rest/api/post")
@AllArgsConstructor
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;
    @GetMapping("/list")
    public ResponseEntity<ListResponse> getAllPost() {
        logger.info("[]========== API GET: /rest/api/post/list ==========");
        logger.info("[]========== not input data ==========");
        List<PostResponse> data = postService.getAllPosts();
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

}
