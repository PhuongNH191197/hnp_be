package com.elcom.plus.user.dao.impl;

import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.dao.UserBlockDao;
import com.elcom.plus.user.dto.request.BlockPhone;
import com.elcom.plus.user.dto.response.CheckUserBlock;
import com.elcom.plus.user.dto.response.UserBlockPhone;
import com.elcom.plus.user.dto.response.UserBlockResponse;
import com.elcom.plus.user.service.impl.BlockServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserBlockDaoImpl extends AbstractDao implements UserBlockDao {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(UserBlockDaoImpl.class);

    @Override
    public List<UserBlockResponse> findUserBlock(String id, String phone) {
        List<UserBlockResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ================== start query user block ==================", phone);
            cs = con.prepareCall("{CALL WAP_AUTH.GET_LIST_BLOCK_BY_USER_ID(?)}");
            cs.setInt(1, Integer.parseInt(id));
            rs = cs.executeQuery();
            while (rs.next()) {
                UserBlockResponse data = new UserBlockResponse();
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setStatus(rs.getInt("status"));
                data.setUsername(rs.getString("user_name"));
                result.add(data);
            }
            logger.info("[{}] ================== output data ==================: {}", phone, result);
            logger.info("[{}]================== end query user block ==================", phone);
            return result;
        } catch (Exception e) {
            logger.error("[" + phone + "]Error user block: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return null;
    }

    @Override
    public Integer updateBlock(Long id, String phone, int userId, int creatorType) {
        Connection con = null;
        CallableStatement cs = null;
        int data = 0;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}]================== start query update user block ==================", phone);
            logger.info("[{}] request update user block user request id={}, userId={}, creatorType= {}", phone, id, userId, creatorType);
            cs = con.prepareCall("{? = call WAP_AUTH.UPDATE_BLOCK_USER(?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setLong(2, id);
            cs.setInt(3, userId);
            cs.setInt(4, creatorType);
            cs.execute();
            con.commit();
        } catch (Exception e) {
            logger.error("[" + phone + "] Error update user block: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== output data ==================: {}", phone, data);
        logger.info("[{}]================== end query update user block ==================", phone);
        return data;
    }

    @Override
    public CheckUserBlock checkUserBlock(Long id, String phone, int userId, int creatorType) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        CheckUserBlock data = new CheckUserBlock();
        try {
            con = connectionManager.getConnection();
            logger.info("[{}]================== start query update user block ==================", phone);
            logger.info("[{}] request update user block user request id={}, userId={}", phone, id, userId);
            cs = con.prepareCall("{call WAP_AUTH.CHECK_USER_BLOCK(?,?,?)}");
            cs.setLong(1, id);
            cs.setInt(2, userId);
            cs.setInt(3, creatorType);
            rs = cs.executeQuery();
            if (rs.next()) {
                data.setUserId(rs.getInt("user_id"));
                data.setCreatorType(rs.getInt("creator_type"));
                data.setType(rs.getInt("type"));
            }
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error update user block ==========: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== output data check user block ==================: data {}", phone, data);
        logger.info("[{}]================== end query check user block ==================", phone);
        return data;
    }

    @Override
    public List<UserBlockPhone> findPhoneBlock(String id, String phone) {
        List<UserBlockPhone> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}] ================== start query phone block ==================", phone);
            cs = con.prepareCall("{CALL VRBT.GET_LIST_PHONE_BLOCK(?)}");
            cs.setString(1, phone);
            rs = cs.executeQuery();
            while (rs.next()) {
                UserBlockPhone data = new UserBlockPhone();
                data.setUserId(rs.getInt("user_id"));
                data.setPhone("0" + rs.getString("phone"));
                data.setUsername(rs.getString("user_name"));
                data.setName(rs.getString("name"));
                result.add(data);
            }
            logger.info("[{}] ================== output data ==================: {}", phone, result);
            logger.info("[{}]================== end query phone block ==================", phone);
            return result;
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error phone block ==========", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return null;
    }

    @Override
    public Integer updateBlockPhone(Long id, String phone, BlockPhone phoneBlock) {
        Connection con = null;
        CallableStatement cs = null;
        int data = 0;
        try {
            con = connectionManager.getConnection();
            logger.info("[{}]================== start query update phone block ==================", phone);
            logger.info("[{}] request update user block user request id={}, phone = {}", phone, id, phoneBlock);
            cs = con.prepareCall("{? = call VRBT.UPDATE_BLOCK_PHONE(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, phone);
            cs.setString(3, phoneBlock.getPhone());
            cs.setInt(4, phoneBlock.getType());
            cs.setString(5, phoneBlock.getName());
            cs.execute();
            con.commit();
            data = cs.getInt(1);
        } catch (Exception e) {
            logger.error("[" + phone + "]========== Error update phone block ==========", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== output data ==================: {}", phone, data);
        logger.info("[{}]================== end query update phone block ==================", phone);
        return data;
    }
}
