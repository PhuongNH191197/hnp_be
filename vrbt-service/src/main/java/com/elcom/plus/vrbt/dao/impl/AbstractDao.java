package com.elcom.plus.vrbt.dao.impl;

import com.elcom.plus.vrbt.config.db.ConnectionManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public abstract class AbstractDao {
    protected final ConnectionManager connectionManager = new ConnectionManager();
    protected void finallyConnection(Connection con, CallableStatement cs, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
