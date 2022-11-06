package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.service.LikeService;

public class LikeServiceImpl extends AbstractDao implements LikeService {
    @Override
    public LikeService getOneByPostIdAndUserId(Long postId, Long userId) {
        return null;
    }

    @Override
    public int getLikeCount() {
        return 0;
    }
}
