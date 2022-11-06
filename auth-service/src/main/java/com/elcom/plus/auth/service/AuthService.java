package com.elcom.plus.auth.service;

import com.elcom.plus.auth.dto.request.AuthRequest;
import com.elcom.plus.auth.dto.request.ForgotPassword;
import com.elcom.plus.auth.dto.request.RegisterRequest;
import com.elcom.plus.auth.dto.request.TokenRequest;
import com.elcom.plus.auth.dto.response.AuthResponse;
import com.elcom.plus.common.util.response.Response;

import java.io.IOException;

public interface AuthService {
    Response register(RegisterRequest request);

    Response login(AuthRequest authRequest, String ip);

    Response sentOtp(String phone, int otpType);

    Response forgotPassword(ForgotPassword request);

    Response saveToken(String phone, TokenRequest tokenRequest);

    Response loginGoogle(String code) throws IOException;

    Response loginFacebook(String code) throws IOException;

    Response loginTiktok(String code) throws IOException;
}
