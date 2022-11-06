package com.elcom.plus.auth.service;

import com.elcom.plus.auth.dto.response.PostResponse;
import com.elcom.plus.auth.entity.Post;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllPosts();
    PostResponse getOnePost();
    boolean savePost();
    boolean updatePost();
    boolean deletePost();
}
