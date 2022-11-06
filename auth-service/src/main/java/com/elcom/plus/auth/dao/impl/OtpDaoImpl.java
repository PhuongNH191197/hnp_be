package com.elcom.plus.auth.dao.impl;

import com.elcom.plus.auth.client.db.ConnectionManager;
import com.elcom.plus.auth.dao.OtpDao;
import com.elcom.plus.auth.entity.OtpCode;
import com.elcom.plus.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

@Repository
public class OtpDaoImpl extends AbstractDao implements OtpDao {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(OtpDaoImpl.class);

    @Override
    public int checkOtp(String phone, String otp, int otpType) {
        int check = -1;

        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}] ================== start query check OTP ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_OTP(?,?,?,?)}");
            cs.setString(1, phone);
            cs.setString(2, otp);
            cs.setInt(3, otpType);
            cs.setInt(4, java.sql.Types.INTEGER);
            cs.execute();
            check = cs.getInt(4);
        } catch (Exception e) {
            logger.error("[{}] Error query check OTP: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ================== output data ==================: {}", phone, check);
        logger.info("[{}] ================== end query check OTP ==================", phone);
        return check;
    }

    @Override
    public String checkDelayTimeOTP(String phone, int otpType) {
        String delayTimeOTP = "";

        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            logger.info("[{}] ================== start query check delay time OTP ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_DELAY_TIME_OTP(?,?,?)}");
            cs.setString(1, phone);
            cs.setInt(2, otpType);
            cs.setInt(3, java.sql.Types.VARCHAR);
            cs.execute();
            delayTimeOTP = cs.getString(3);
        } catch (Exception e) {
            logger.error("[{}] Error query check delay time OTP: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        logger.info("[{}] ================== output data ==================: {}", phone, delayTimeOTP);
        logger.info("[{}] ================== end query check delay time OTP ==================", phone);
        return delayTimeOTP;
    }

    @Override
    public void saveOtp(String phone, String otp, int otpType, int status) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            logger.info("[{}] ================== start query save OTP ==================", phone);
            con = connectionManager.getConnection();
            cs = con.prepareCall("{? = call SAVE_OTP(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, phone);
            cs.setString(3, otp);
            cs.setInt(4, otpType);
            cs.setInt(5, status);
            cs.execute();
            con.commit();
            logger.info("[{}] ================== query save OPT done ==================", phone);
            logger.info("[{}] ================== end query save OTP ==================", phone);
        } catch (Exception e) {
            logger.error("[{}] Error query check save OTP: {}", phone, e);
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, null);
        }
    }
}
