package com.elcom.plus.auth.dao;

import com.elcom.plus.auth.dto.response.PostResponse;
import com.elcom.plus.auth.entity.Post;

import java.util.List;

public interface PostDao {
    List<PostResponse> getAllPosts();
    PostResponse getOnePost();
    boolean savePost();
    boolean updatePost();
    boolean deletePost();
}
