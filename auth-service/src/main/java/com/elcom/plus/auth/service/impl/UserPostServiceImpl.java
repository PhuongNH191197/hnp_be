package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.entity.Post;
import com.elcom.plus.auth.service.UserPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostServiceImpl extends AbstractDao implements UserPostService {
    @Override
    public List<Post> getAllPostsByUserId(Long userId) {
        return null;
    }
}
