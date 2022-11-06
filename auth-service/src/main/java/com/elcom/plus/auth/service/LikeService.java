package com.elcom.plus.auth.service;

public interface LikeService {
    LikeService getOneByPostIdAndUserId(Long postId, Long userId);
    int getLikeCount();
}
