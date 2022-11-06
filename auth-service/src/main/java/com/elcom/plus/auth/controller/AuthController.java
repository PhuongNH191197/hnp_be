package com.elcom.plus.auth.controller;

import com.elcom.plus.auth.dto.request.*;
import com.elcom.plus.auth.service.AuthService;
import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.http.HttpUtils;
import com.elcom.plus.common.util.response.Response;
import lombok.AllArgsConstructor;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@RestController
@RequestMapping(value = "rest/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        logger.info("[] ========== API POST: /rest/api/auth/register ==========");
        logger.info("[] ========== input data ==========: {}", request);
        Response data = authService.register(request);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPassword request) {
        logger.info("[] ========== API POST: /rest/api/auth/forgot-password ==========");
        logger.info("[] ========== input data ==========: {}", request);
        Response data = authService.forgotPassword(request);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/otp")
    public ResponseEntity<Response> sendOtp(@RequestParam("phone") String phone, @RequestParam("otpType") int otpType) {
        logger.info("[{}] ========== API GET: /rest/api/auth/otp ==========", phone);
        logger.info("[{}] ========== input data ==========: otpType: {}", phone, otpType);
        Response data = authService.sentOtp(phone, otpType);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Response> login(HttpServletRequest request, @RequestBody AuthRequest authRequest) {
        Enumeration<String> headerNames = request.getHeaderNames();
        logger.info("[] ========== API POST: /rest/api/auth/login ==========");
        logger.info("[] ========== input data ==========: {}", authRequest);
        System.out.println("========== Start check Header =========== ");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("======= Header ====== "+ headerName + "==== Value ==== "+headerValue);
        }
        System.out.println("========== End check Header =========== ");
        String ip = HttpUtils.getClientIpAdd(request);
        return ResponseEntity.ok(authService.login(authRequest, ip));
    }

    @PostMapping(value = "/google/login")
    public ResponseEntity<Response> loginGoogle(@RequestBody SocialAccount socialAccount) throws ClientProtocolException, IOException {
        logger.info("[] ========== API: /rest/api/auth/google/login ==========");
        logger.info("[] ========== input data ==========: {}", socialAccount);
        System.out.println("=== request google code ===: " + socialAccount.getCode());
        Response data = authService.loginGoogle(socialAccount.getCode());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/facebook/login")
    public ResponseEntity<Response> loginFacebook(@RequestBody SocialAccount socialAccount) throws ClientProtocolException, IOException {
        logger.info("[] ========== API: /rest/api/auth/facebook/login ==========");
        logger.info("[] ========== input data ==========: {}", socialAccount);
        System.out.println("=== request facebook code ===: " + socialAccount.getCode());
        Response data = authService.loginFacebook(socialAccount.getCode());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/tiktok/login")
    public ResponseEntity<Response> loginTiktok(@RequestBody SocialAccount socialAccount) throws ClientProtocolException, IOException {
        logger.info("[] ========== API: /rest/api/auth/tiktok/login ==========");
        logger.info("[] ========== input data ==========: {}", socialAccount);
        System.out.println("=== request tiktok code ===: " + socialAccount.getCode());
        Response data = authService.loginTiktok(socialAccount.getCode());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/firebase/token")
    public ResponseEntity<Response> saveToken(HttpServletRequest request, @RequestBody TokenRequest tokenRequest) {
        String header = request.getHeader("authorization");
        String phone = CollectionsUtil.getIdHeader(header);
        logger.info("[{}] ========== API POST: /rest/api/auth/firebase/token ==========", phone);
        logger.info("[{}] ========== input data ==========: {}", phone, tokenRequest);
        Response data = authService.saveToken(phone, tokenRequest);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
