package com.elcom.plus.auth.service;

import com.elcom.plus.auth.entity.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    List<Image> getAllImageByPostId(Long postId);
}