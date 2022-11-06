package com.elcom.plus.auth.dao.impl;

import com.elcom.plus.auth.client.db.ConnectionManager;
import com.elcom.plus.auth.dao.UserDao;
import com.elcom.plus.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;

@Repository
public class UserDaoImpl extends AbstractDao implements UserDao {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);


    @Override
    public User findByUsername(String username) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}] ================== start query find by username ==================", username);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_USER_BY_USERNAME(?)}");
            cs.setString(1, username);
            rs = cs.executeQuery();
            while (rs.next()) {
                User data = new User();
                data.setId(rs.getLong("id"));
                data.setUsername(rs.getString("username"));
                data.setPassword(rs.getString("password"));
                data.setPhone(rs.getString("phone"));
                data.setEmail(rs.getString("email"));
                String avatar = rs.getString("avatar") != null ? rs.getString("avatar").replace("//u02", domainFile) : "";
                data.setAvatar(avatar);
                data.setAddress(rs.getString("address"));
                data.setAge(rs.getInt("age"));
                System.out.println("rs.getTimestamp: " + rs.getTimestamp("date_block"));
                LocalDateTime dateBlock = rs.getTimestamp("date_block") != null ? rs.getTimestamp("date_block").toLocalDateTime() : null;
                System.out.println("dateBlock : " + dateBlock);
                data.setDateBlock(dateBlock);
                data.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null;

                data.setUpdatedAt(updatedAt);
                data.setRankId(rs.getInt("rank_id"));
                logger.info("[{}] ================== output data ==================: {}", username, data);
                logger.info("[{}] ================== end query find by username ==================", username);
                return data;
            }
        } catch (Exception e) {
            logger.error("[{}] Error query find by username: {}", username, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ================== output data ==================: null", username);
        logger.info("[{}] ================== end query find by username ==================", username);
        return null;
    }

    @Override
    public User findUserById(int id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        User data = new User();
        try {
            logger.info("[{}] ================== start query find by id ==================", id);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL WAP_AUTH.GET_USER_BY_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                String avatar = "";
                data.setId(rs.getLong("id"));
                data.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
                data.setPassword(rs.getString("password"));
                data.setPhone(rs.getString("phone") != null ? rs.getString("phone") : "");
                data.setEmail(rs.getString("email") != null ? rs.getString("email") : "");
                if (rs.getString("avatar") == null) {
                    avatar = "";
                } else if (rs.getString("avatar").contains("https://")) {
                    avatar = rs.getString("avatar");
                } else {
                    avatar = rs.getString("avatar").replace("//u02", domainFile);
                }
                data.setAvatar(avatar);

                data.setLoginType(rs.getInt("login_type"));
            }
        } catch (Exception e) {
            logger.error("[{}] ================== Error query find by id ==================: {}", id, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ================== output data ==================: {}", id, data);
        logger.info("[{}] ================== end query find by id ==================", id);
        return data;
    }

    @Override
    public void save(User user) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            logger.info("[{}] ================== start query insert user ==================", user.getPhone());
            con = connectionManager.getConnection();
            cs = con.prepareCall("{call INSERT_USER(?,?,?,?)}");
            cs.setString(1, user.getUsername());
            cs.setString(2, user.getPassword());
            cs.setString(3, user.getPhone());
            cs.setInt(4, user.getLoginType());
            cs.execute();
            con.commit();
            logger.info("[{}] ================== query insert user done ==================", user.getPhone());
        } catch (Exception e) {
            logger.error("[{}] Error query insert user: {}", user.getPhone(), e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== end query insert user ==================", user.getPhone());
    }

    @Override
    public void updateLastLogin(User user) {

    }

    @Override
    public void forgotPassword(String username, String passwordEncoder) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            logger.info("[{}] ================== start query forgot password ==================", username);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL WAP_AUTH.FORGOT_PASSWORD(?,?)}");
            cs.setString(1, username);
            cs.setString(2, passwordEncoder);
            cs.execute();
            con.commit();
            logger.info("[{}] ================== query forgot password done ==================", username);
        } catch (Exception e) {
            logger.error("[{}] Error query forgot password: {}", username, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== end query forgot password ==================", username);
    }

    @Override
    public boolean saveToken(String phone, String token) {
        Connection con = null;
        CallableStatement cs = null;
        boolean saveToken = false;
        try {
            logger.info("[{}] ================== start query save token ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = CALL WAP_AUTH.SAVE_TOKEN(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, phone);
            cs.setString(3, token);
            cs.execute();
            con.commit();
            saveToken = true;
        } catch (Exception e) {
            logger.error("[{}] Error query save token: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[{}] ================== output data ==================: {}", phone, saveToken);
        logger.info("[{}] ================== end query save token ==================", phone);
        return saveToken;
    }

    @Override
    public int saveSocialUser(String id, String name, String avatar, int type) {
        Connection con = null;
        CallableStatement cs = null;
        int saveFacebookUser = 0;
        try {
            logger.info("[] ================== start query save facebook user ==================");
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = CALL WAP_AUTH.SAVE_SOCIAL_USER(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, id);
            cs.setString(3, name);
            cs.setString(4, avatar);
            cs.setInt(5, type);
            cs.execute();
            con.commit();
            saveFacebookUser = cs.getInt(1);
        } catch (Exception e) {
            logger.error("[] Error query save social user", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
        logger.info("[] ================== output data ==================: {}", saveFacebookUser);
        logger.info("[] ================== end query save social user ==================");
        return saveFacebookUser;
    }
}
