package com.elcom.plus.auth.dao;

import com.elcom.plus.auth.entity.OtpCode;

public interface OtpDao {
    int checkOtp(String phone, String otp, int otpType);
    void saveOtp(String phone, String otp, int otpType, int status);
    String checkDelayTimeOTP(String phone, int otpType);
}
