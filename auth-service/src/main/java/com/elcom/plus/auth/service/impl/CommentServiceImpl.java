package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.entity.Comment;
import com.elcom.plus.auth.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends AbstractDao implements CommentService {
    @Override
    public List<Comment> getAllCommentByPostId(Long postId) {
        return null;
    }

    @Override
    public int getCommentCount() {
        return 0;
    }
}
