package com.elcom.plus.auth.service;

import com.elcom.plus.auth.entity.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserPostService {
    List<Post> getAllPostsByUserId(Long userId);
}
