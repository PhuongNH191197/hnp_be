package com.elcom.plus.mecall.dao.impl;

import org.springframework.beans.factory.annotation.Value;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public abstract class AbstractDao {

    @Value("${domain.file}")
    protected String domainFile;
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
//            Log.log().error("ReturnPro.getTransInfoByTxnref -- close ==> error : ", ex);
            ex.printStackTrace();
        }
    }
}
