package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.PostDao;
import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.dto.response.PostResponse;
import com.elcom.plus.auth.service.PostService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl extends AbstractDao implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostDao postDao;

    @Override
    public List<PostResponse> getAllPosts() {
        return postDao.getAllPosts();
    }

    @Override
    public PostResponse getOnePost() {
        return null;
    }

    @Override
    public boolean savePost() {
        return false;
    }

    @Override
    public boolean updatePost() {
        return false;
    }

    @Override
    public boolean deletePost() {
        return false;
    }
}
