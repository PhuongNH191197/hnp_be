package com.elcom.plus.auth.dao.impl;

import com.elcom.plus.auth.client.db.ConnectionManager;
import com.elcom.plus.auth.dao.OtpDao;
import com.elcom.plus.auth.dao.PostDao;
import com.elcom.plus.auth.dto.response.PostResponse;
import com.elcom.plus.auth.entity.Post;
import com.elcom.plus.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class PostDaoImpl extends AbstractDao implements PostDao {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(PostDaoImpl.class);

    @Override
    public List<PostResponse> getAllPosts() {
        List<PostResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("========== start query get category ==========");
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_ALL_POST(?)}");
            cs.setInt(1, 0);
            rs = cs.executeQuery();
            while (rs.next()) {
                PostResponse postResponse = new PostResponse();
                postResponse.setId(rs.getLong("ID"));
                postResponse.setContentPost(rs.getString("CONTENT_POST"));
                postResponse.setAddressId(rs.getInt("ADDRESS_ID"));
                postResponse.setStatus(rs.getInt("STATUS"));

                LocalDateTime createdAt = rs.getTimestamp("CREATE_AT") != null ? rs.getTimestamp("CREATE_AT").toLocalDateTime() : null;
                postResponse.setCreatedAt(createdAt);
                LocalDateTime updatedAt = rs.getTimestamp("UPDATE_AT") != null ? rs.getTimestamp("UPDATE_AT").toLocalDateTime() : null;
                postResponse.setUpdatedAt(updatedAt);
                postResponse.setOrderIndex(rs.getInt("ORDER_INDEX"));
                postResponse.setCmtCount(rs.getInt("COMMENT_COUNT"));
                postResponse.setLikeCount(rs.getInt("LIKE_COUNT"));
                result.add(postResponse);
            }
        } catch (Exception e) {
            logger.error("Error get post: ", e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("========== output data ==========: {}", result);
        logger.info("========== end query get post ==========");
        return result;
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
