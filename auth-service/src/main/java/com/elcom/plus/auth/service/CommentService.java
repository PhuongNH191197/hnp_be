package com.elcom.plus.auth.service;

import com.elcom.plus.auth.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllCommentByPostId(Long postId);
    int getCommentCount();
}
