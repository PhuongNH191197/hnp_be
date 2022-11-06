package com.elcom.plus.user.dao.impl;

import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.dao.CommonDao;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

@Repository
public class CommonDaoImpl extends AbstractDao implements CommonDao {
    private final ConnectionManager connectionManager = new ConnectionManager();

    @Override
    public String getMessage(String role, int code) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String message = "";
        try {
            System.out.println("role: "+ role);
            System.out.println("code: "+ code);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_MESSAGE(?,?)}");
            cs.setString(1, role);
            cs.setInt(2, code);
            rs = cs.executeQuery();
            if (rs.next()) {
                message = rs.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return message;
    }
}
