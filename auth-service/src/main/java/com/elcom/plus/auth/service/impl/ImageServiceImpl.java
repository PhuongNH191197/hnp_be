package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.entity.Image;
import com.elcom.plus.auth.service.ImageService;

import java.util.List;

public class ImageServiceImpl extends AbstractDao implements ImageService {
    @Override
    public List<Image> getAllImageByPostId(Long postId) {
        return null;
    }
}
