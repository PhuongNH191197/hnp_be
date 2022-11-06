package com.elcom.plus.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpCode {
    private String phone;
    private String otp;
    private Timestamp expiredTime;

}
