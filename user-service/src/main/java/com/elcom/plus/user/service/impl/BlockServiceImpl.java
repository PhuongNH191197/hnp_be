package com.elcom.plus.user.service.impl;

import com.elcom.plus.common.util.constant.ResponseCode;
import com.elcom.plus.common.util.constant.ResponseMessage;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.controller.VideoController;
import com.elcom.plus.user.dao.CommonDao;
import com.elcom.plus.user.dao.UserBlockDao;
import com.elcom.plus.user.dao.impl.AbstractDao;
import com.elcom.plus.user.dto.request.BlockPhone;
import com.elcom.plus.user.dto.request.FollowUser;
import com.elcom.plus.user.dto.response.CheckUserBlock;
import com.elcom.plus.user.dto.response.UserBlockPhone;
import com.elcom.plus.user.dto.response.UserBlockResponse;
import com.elcom.plus.user.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockServiceImpl extends AbstractDao implements BlockService {
    private UserBlockDao blockDao;
    private CommonDao commonDao;
    private static final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    public BlockServiceImpl(UserBlockDao blockDao, CommonDao commonDao) {
        this.blockDao = blockDao;
        this.commonDao = commonDao;
    }

    @Override
    public List<UserBlockResponse> blocks(String id, String phone) {
        logger.info("[{}]========== start get list user block ==========", phone);
        return blockDao.findUserBlock(id, phone);
    }

    @Override
    public Response updateBlock(String id, String phone, FollowUser blockRequest) {
        Response response = new Response();
        logger.info("[{}]========== start update user block ==========", phone);
        if(blockRequest.getUserId() == 0) {
            response.setCode(999);
            response.setMessage(commonDao.getMessage("SYSTEM", 8000));
            logger.info("[{}]========== Field userId is required ==========", phone);
            return response;
        }
        Integer data = blockDao.updateBlock(Long.parseLong(id), phone, blockRequest.getUserId(), blockRequest.getCreatorType());
        return new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public Response checkUserBlock(String id, String phone, int userId, int creatorType) {
        Response response = new Response();
        CheckUserBlock data = new CheckUserBlock();
        if(userId == 0) {
            logger.info("[{}]========== Field userId is required ==========", phone);
            return response;
        }
        data = blockDao.checkUserBlock(Long.parseLong(id), phone, userId, creatorType);
        response.setCode(0);
        response.setMessage(commonDao.getMessage("USER", 9002));
        response.setData(data);
        return response;
    }

    @Override
    public List<UserBlockPhone> blockPhoneData(String id, String phone) {
        logger.info("[{}]========== start get list phone block ==========", phone);
        return blockDao.findPhoneBlock(id, phone);
    }

    @Override
    public Response updateBlockPhone(String id, String phone, BlockPhone phoneBlock) {
        Response response = new Response();
        logger.info("[{}]========== start update phone block ==========", phone);
        if(phoneBlock.getPhone().isEmpty()) {
            logger.info("[{}]========== Field phone is required ==========", phone);
            return response;
        } else if(phoneBlock.getType() == null) {
            logger.info("[{}]========== Field type is required ==========", phone);
            return response;
        }
        Integer data = blockDao.updateBlockPhone(Long.parseLong(id), phone, phoneBlock);
        if (data == 1) {
            response.setCode(8010);
            response.setMessage(commonDao.getMessage("USER", 8010));
            return response;
        }
        return new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
}
