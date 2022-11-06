package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.client.db.ConnectionManager;
import com.elcom.plus.auth.config.JwtUtil;
import com.elcom.plus.auth.controller.AuthController;
import com.elcom.plus.auth.dao.OtpDao;
import com.elcom.plus.auth.dao.UserDao;
import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.dto.request.*;
import com.elcom.plus.auth.dto.response.*;
import com.elcom.plus.auth.entity.OtpCode;
import com.elcom.plus.auth.entity.User;
import com.elcom.plus.auth.service.AuthService;
import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.common.util.constant.LoginType;
import com.elcom.plus.common.util.constant.ResponseCode;
import com.elcom.plus.common.util.constant.ResponseMessage;
import com.elcom.plus.common.util.request.ActionInforRequest;
import com.elcom.plus.common.util.request.SmsRequest;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.soap.SoapConstant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl extends AbstractDao implements AuthService {
    @Value("${soap.vaaa.username}")
    private String usernameSoap;
    @Value("${soap.vaaa.password}")
    private String passwordSoap;
    @Value("${soap.vaaa.url}")
    private String urlSoap;

    @Value("${domain.sent.otp}")
    private String domainOtp;
    private final RestTemplate restTemplate;
    private final JwtUtil jwt;
    private UserDao userDao;
    private OtpDao otpDao;

    @Value("${server.rest.api.path}")
    private String api_server;

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private static ConnectionManager connectionManager = new ConnectionManager();

    public AuthServiceImpl(RestTemplate restTemplate, JwtUtil jwt, UserDao userDao, OtpDao otpDao) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
        this.userDao = userDao;
        this.otpDao = otpDao;
    }

    @Override
    public Response register(RegisterRequest request) {
        Response response = new Response();
        if (request.getUsername().isEmpty()) {
            logger.info("[]========== Field username is required ==========");
            return response;
        } else if (request.getPassword().isEmpty()) {
            logger.info("[]========== Field password is required ==========");
            return response;
        } else if (request.getOtp().isEmpty()) {
            logger.info("[]========== Field otp is required ==========");
            return response;
        }
        //do validation if user already exists
        int checkOtp = otpDao.checkOtp(request.getUsername(), request.getOtp(), 0);
        User user = userDao.findByUsername(request.getUsername());
        if (!Objects.isNull(user)) {
            return new Response(ResponseCode.USER_EXISTS, ResponseMessage.USER_EXISTS);
        }
        if (checkOtp == -1) {
            return new Response(ResponseCode.ERROR_SYSTEM, ResponseMessage.ERROR_SYSTEM);
        }

        if (checkOtp == 1) {
            return new Response(ResponseCode.OTP_NOT_FOUND, ResponseMessage.OTP_NOT_FOUND);
        }

        if (checkOtp == 2) {
            return new Response(ResponseCode.INVALID_OTP_CORRECT, ResponseMessage.INVALID_OTP_CORRECT);
        }

        if (checkOtp == 3) {
            return new Response(ResponseCode.OTP_INVALID_EXPIRED, ResponseMessage.OTP_INVALID_EXPIRED);
        }
        user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setPhone(request.getUsername());
        user.setLoginType(LoginType.NORMAL);
        userDao.save(user);
        String accessToken = jwt.generate(user, "ACCESS");
        String refreshToken = jwt.generate(user, "REFRESH");
        UserInfoResponse userInfo = new UserInfoResponse(user);
        AuthResponse data = new AuthResponse(accessToken, refreshToken, userInfo);
        otpDao.saveOtp(request.getUsername(), request.getOtp(), 1, 1);
        return new Response(data);
    }

    @Override
    public Response forgotPassword(ForgotPassword request) {
        Response response = new Response();
        User user = userDao.findByUsername(request.getUsername());
        try {
            if (request.getUsername().isEmpty()) {
                logger.info("[]========== Field username is required ==========");
                return response;
            } else if (request.getPassword().isEmpty()) {
                logger.info("[]========== Field password is required ==========");
                return response;
            } else if (request.getOtp().isEmpty()) {
                logger.info("[]========== Field otp is required ==========");
                return response;
            }

            if (Objects.isNull(user)) {
                return new Response(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
            }

            int checkOtp = otpDao.checkOtp(request.getUsername(), request.getOtp(), 2);
            if (checkOtp == -1) {
                return new Response(ResponseCode.ERROR_SYSTEM, ResponseMessage.ERROR_SYSTEM);
            }

            if (checkOtp == 1) {
                return new Response(ResponseCode.OTP_NOT_FOUND, ResponseMessage.OTP_NOT_FOUND);
            }

            if (checkOtp == 2) {
                return new Response(ResponseCode.INVALID_OTP_CORRECT, ResponseMessage.INVALID_OTP_CORRECT);
            }

            if (checkOtp == 3) {
                return new Response(ResponseCode.OTP_INVALID_EXPIRED, ResponseMessage.OTP_INVALID_EXPIRED);
            }

            String passwordEncoder = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            userDao.forgotPassword(request.getUsername(), passwordEncoder);
            response.setCode(ResponseCode.SUCCESS);
            response.setMessage(ResponseMessage.SUCCESS);
            otpDao.saveOtp(request.getUsername(), request.getOtp(), 2, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response login(AuthRequest authRequest, String ip) {
        User user;
        Response response = new Response();
        try {
            if (authRequest.getUsername().isEmpty()) {
                logger.info("[]========== Field username is required ==========");
            } else if (authRequest.getPassword().isEmpty()) {
                logger.info("[]========== Field password is required ==========");
            }
            if (authRequest.getLoginType() == 0) {
                user = userDao.findByUsername(authRequest.getUsername());
                if (Objects.isNull(user)) {
                    return new Response(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
                }
                String userPassDb = user.getPassword();
                String userPassReq = authRequest.getPassword();

                if (!BCrypt.checkpw(userPassReq, userPassDb)) {
                    return new Response(ResponseCode.WRONG_PASSWORD, ResponseMessage.WRONG_PASSWORD);
                }
                userDao.updateLastLogin(user);
            } else if (authRequest.getLoginType() == 5) {
                user = userDao.findByUsername(authRequest.getUsername());
                int checkOtp = otpDao.checkOtp(authRequest.getUsername(), authRequest.getPassword(), 0);
                if (checkOtp == -1) {
                    return new Response(ResponseCode.ERROR_SYSTEM, ResponseMessage.ERROR_SYSTEM);
                }

                if (checkOtp == 1) {
                    return new Response(ResponseCode.OTP_NOT_FOUND, ResponseMessage.OTP_NOT_FOUND);
                }

                if (checkOtp == 2) {
                    return new Response(ResponseCode.INVALID_OTP_CORRECT, ResponseMessage.INVALID_OTP_CORRECT);
                }

                if (checkOtp == 3) {
                    return new Response(ResponseCode.OTP_INVALID_EXPIRED, ResponseMessage.OTP_INVALID_EXPIRED);
                }

                if (Objects.isNull(user) && checkOtp == 0) {
                    User userNew = new User();
                    userNew.setUsername(authRequest.getUsername());
                    userNew.setPassword(BCrypt.hashpw("", BCrypt.gensalt()));
                    userNew.setPhone(authRequest.getUsername());
                    userNew.setLoginType(LoginType.OTP);
                    userDao.save(userNew);
                    user = userDao.findByUsername(authRequest.getUsername());
                }
                userDao.updateLastLogin(user);
                otpDao.saveOtp(authRequest.getUsername(), authRequest.getPassword(), 0, 1);
            } else {
                String username = "";
                if (authRequest.getLoginType() == 1) {
                    System.out.println("======== check ip ======= " + ip);
                    ResultResponse responseAu = getMSISDNResponse(ip);
                    System.out.println("======= code ==== " + responseAu.getCode());
                    System.out.println("======= desc ==== " + responseAu.getDesc());
                    if (responseAu.getCode() != 0) {
                        return new Response(responseAu.getCode(), responseAu.getDesc());
                    }
                    username = responseAu.getDesc();
                }
                user = userDao.findByUsername(authRequest.getUsername());
                if (Objects.isNull(user)) {
                    user = new User();
                    user.setUsername(username);
                    user.setPhone(username);
                    userDao.save(user);
                } else {
                    userDao.updateLastLogin(user);
                }
            }
            String accessToken = jwt.generate(user, "ACCESS");
            System.out.println("accessToken: " + accessToken);
            String refreshToken = jwt.generate(user, "REFRESH");
            System.out.println("refreshToken: " + refreshToken);
            System.out.println("user: " + user.toString());

            UserInfoResponse userInfo = new UserInfoResponse(user);
            AuthResponse data = new AuthResponse(accessToken, refreshToken, userInfo);
            response.setCode(0);
            response.setMessage("Success");
            response.setData(data);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response sentOtp(String phone, int otpType) {
        String delayTime = otpDao.checkDelayTimeOTP(phone, otpType);
        if (delayTime != null) {
            String otpDelayMessage = "Vui lòng thử lại sau " + delayTime;
            return new Response(ResponseCode.OTP_DELAY, otpDelayMessage);
        }
        Response response = new Response();
        RestTemplate restTemplate = new RestTemplate();
        String otp = random(6);
        // SMS Builder
        ActionInforRequest actionInforRequest = new ActionInforRequest();
        actionInforRequest.setOtp(otp);
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setAction("OTP0");
        smsRequest.setActionInfo(actionInforRequest);
        smsRequest.setUnaccented(true);
        String content = CollectionsUtil.getMessageSms(smsRequest);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Accept-Language", "vi");
            MTsms mTsms = new MTsms(phone, content);
            HttpEntity<MTsms> request = new HttpEntity<>(mTsms, headers);
            String path = api_server + "/app/send-mt";
            MpsResponse responseData = restTemplate.postForObject(path, request, MpsResponse.class);
//            MpsResponse mpsResponse = new MpsResponse();
//            mpsResponse.setPriceCharge(responseData.getBody().getPriceCharge());
//            mpsResponse.setResult(responseData.getBody().getResult());
//            if (mpsResponse.getResult() != 0) {
//                return new Response(ResponseCode.WRONG_PASSWORD, ResponseMessage.WRONG_PASSWORD);
//            }
            if (responseData.getError_code() == 0) {
                otpDao.saveOtp(phone, otp, otpType, 0);
                response.setCode(responseData.getError_code());
                response.setMessage(ResponseMessage.SUCCESS);
            } else {
                response.setCode(responseData.getError_code());
                response.setMessage(ResponseMessage.SEND_OTP_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String random(int size) {
        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedToken.toString();
    }

    private ResultResponse getMSISDNResponse(String ip) {
        ResultResponse result = new ResultResponse();
        try {
            String xml = getXml(usernameSoap, passwordSoap, ip);
            System.out.println("======== xml ==== " + xml);
            HttpPost post = new HttpPost(urlSoap);
            post.setEntity(new StringEntity(xml));
            post.setHeader("SOAPAction", "");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            String res_xml = EntityUtils.toString(response.getEntity());
            result.setCode(Integer.parseInt(getFullNameFromXml(res_xml, "code")));
            result.setDesc(getFullNameFromXml(res_xml, "desc"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public static void callVAAA(String user, String pass, String ip) {
        String xml = SoapConstant.xmlSoapVaaa;
        xml = xml.replaceAll("USER", user).replaceAll("PASS", pass).replaceAll("IP", ip);
        try {
            FileWriter myWriter = new FileWriter(ResourceUtils.getFile("classpath:request.xml"));
            myWriter.write(xml);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getXml(String user, String pass, String ip) {
        String xml = SoapConstant.xmlSoapVaaa;
        xml = xml.replaceAll("USER", user).replaceAll("PASS", pass).replaceAll("IP", ip);
        return xml;
    }

    public static Document loadXMLString(String response) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(response));
        return db.parse(is);
    }

    public static String getFullNameFromXml(String response, String tagName) throws Exception {
        Document xmlDoc = loadXMLString(response);
        NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
        List<String> ids = new ArrayList<String>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node x = nodeList.item(i);
            ids.add(x.getFirstChild().getNodeValue());
            System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
        }
        return ids.get(0);
    }

    public Response saveToken(String phone, TokenRequest tokenRequest) {
        Response response = new Response();
        String phone_number = "all";
        try {
            if (tokenRequest.getToken().isEmpty()) {
                logger.info("[{}]========== Field token is required ==========", phone);
                return response;
            } else if (tokenRequest.getPhone().isEmpty()) {
                logger.info("[{}]========== Field phone is required ==========", phone);
                return response;
            }
            if (!phone.isEmpty()) {
                phone_number = "0" + phone;
            }
            if (Objects.isNull(tokenRequest)) {
                return response;
            }
            if (userDao.saveToken(phone_number, tokenRequest.getToken())) {
                response.setCode(0);
                response.setMessage(ResponseMessage.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response loginGoogle(String code) throws IOException {
        Response response = new Response();
        User user;
        String accessToken = "";
        ObjectMapper mapper = new ObjectMapper();
        String token_link = env.getProperty("google.link.get.token");
        String client_id = env.getProperty("google.app.id");
        String client_secret = env.getProperty("google.app.secret");
        String link_get_token = token_link;
        try {
            if (code.isEmpty()) {
                logger.info("[]========== Field code is required ==========");
                return response;
            }
            link_get_token = link_get_token + "?code=" + code + "&client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=https%3A//mecall.vn/account/login-callback&grant_type=authorization_code";
            String responseToken = Request.Post(link_get_token).execute().returnContent().asString();
            JsonNode node = mapper.readTree(responseToken).get("access_token");
            accessToken = node.textValue();
            logger.info("[] ========== data accessToken google: {}", accessToken);
            String link = env.getProperty("google.link.get.user.info") + accessToken;
            logger.info("[] ========== link get data user google: {}", link);
            String responseData = Request.Get(link).execute().returnContent().asString();
            JsonNode dataInfo = mapper.readTree(responseData);
            logger.info("[] ========== data user of google ==========: {}", dataInfo);
            String id = dataInfo.get("id").textValue();
            String name = dataInfo.get("name").textValue();
            String avatar = dataInfo.get("picture").textValue();
            int userId = userDao.saveSocialUser(id, name, avatar, 2);
            if (userId != 0) {
                user = userDao.findUserById(userId);
                userDao.updateLastLogin(user);
                String accessTokenNew = jwt.generate(user, "ACCESS");
                System.out.println("accessToken: " + accessTokenNew);
                String refreshToken = jwt.generate(user, "REFRESH");
                System.out.println("refreshToken: " + refreshToken);
                UserInfoResponse userInfo = new UserInfoResponse(user);
                AuthResponse data = new AuthResponse(accessTokenNew, refreshToken, userInfo);
                response.setCode(0);
                response.setMessage(ResponseMessage.SUCCESS);
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response loginFacebook(String code) throws IOException {
        Response response = new Response();
        User user;
        String accessToken = "";
        ObjectMapper mapper = new ObjectMapper();
        String token_link = env.getProperty("facebook.link.get.token");
        String client_id = env.getProperty("facebook.app.id");
        String client_secret = env.getProperty("facebook.app.secret");
        String link_get_token = token_link;
        try {
            if (code.isEmpty()) {
                logger.info("[]========== Field code is required ==========");
                return response;
            }
            String responseToken = Request.Post(link_get_token).bodyForm(Form.form().add("client_id", client_id).add("client_secret", client_secret).add("code", code).add("redirect_uri", env.getProperty("redirect.uri")).build()).execute().returnContent().asString();
            JsonNode node = mapper.readTree(responseToken).get("access_token");
            accessToken = node.textValue();
            String link = env.getProperty("facebook.link.get.user.info") + accessToken;
            String responseData = Request.Get(link).execute().returnContent().asString();
            JsonNode dataInfo = mapper.readTree(responseData);
            String id = dataInfo.get("id").textValue();
            String name = dataInfo.get("name").textValue();
            String avatar = dataInfo.get("picture").get("data").get("url").textValue();
            int userId = userDao.saveSocialUser(id, name, avatar, 3);
            if (userId != 0) {
                user = userDao.findUserById(userId);
                userDao.updateLastLogin(user);
                String accessTokenNew = jwt.generate(user, "ACCESS");
                System.out.println("accessToken: " + accessTokenNew);
                String refreshToken = jwt.generate(user, "REFRESH");
                System.out.println("refreshToken: " + refreshToken);
                UserInfoResponse userInfo = new UserInfoResponse(user);
                AuthResponse data = new AuthResponse(accessTokenNew, refreshToken, userInfo);
                response.setCode(0);
                response.setMessage(ResponseMessage.SUCCESS);
                response.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response loginTiktok(String code) throws IOException {
        Response response = new Response();
        User user;
        String accessToken = "";
        ObjectMapper mapper = new ObjectMapper();
        String token_link = env.getProperty("tiktok.link.get.token");
        String client_key = env.getProperty("tiktok.app.id");
        String client_secret = env.getProperty("tiktok.app.secret");
        String link_get_token = token_link;
        try {
            if (code.isEmpty()) {
                logger.info("[]========== Field code is required ==========");
                return response;
            }
            String responseToken = Request.Post(link_get_token).bodyForm(Form.form().add("client_key", client_key).add("client_secret", client_secret).add("code", code).add("grant_type", "authorization_code").build()).execute().returnContent().asString();
            JsonNode node = mapper.readTree(responseToken).get("data").get("access_token");
            accessToken = node.textValue();
            String link = env.getProperty("tiktok.link.get.user.info");

            String responseData = Request.Get(link + "?fields=open_id,avatar_url,display_name").addHeader("Authorization", "Bearer " + accessToken).execute().returnContent().asString();
            JsonNode dataInfo = mapper.readTree(responseData);
            String code_request = dataInfo.get("error").get("code").textValue();
            if ("OK".equalsIgnoreCase(code_request)) {
                String id = dataInfo.get("data").get("user").get("open_id").textValue();
                String name = dataInfo.get("data").get("user").get("display_name").textValue();
                String avatar = dataInfo.get("data").get("user").get("avatar_url").textValue();
                int userId = userDao.saveSocialUser(id, name, avatar, 4);
                if (userId != 0) {
                    user = userDao.findUserById(userId);
                    userDao.updateLastLogin(user);
                    String accessTokenNew = jwt.generate(user, "ACCESS");
                    System.out.println("accessToken: " + accessTokenNew);
                    String refreshToken = jwt.generate(user, "REFRESH");
                    System.out.println("refreshToken: " + refreshToken);
                    UserInfoResponse userInfo = new UserInfoResponse(user);
                    AuthResponse data = new AuthResponse(accessTokenNew, refreshToken, userInfo);
                    response.setCode(0);
                    response.setMessage(ResponseMessage.SUCCESS);
                    response.setData(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getToken(final String code, Integer type) throws ClientProtocolException, IOException {
        String token_link = "";
        String client_id = "";
        String client_secret = "";
        if (type == 0) {
            token_link = env.getProperty("google.link.get.token");
            client_id = env.getProperty("google.app.id");
            client_secret = env.getProperty("google.app.secret");
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), token_link, client_id, client_secret, code, env.getProperty("google.redirect.uri")).execute();
            logger.info("==========tokenResponse google==========");
            String accessToken = tokenResponse.getAccessToken();
            logger.info("==========token google==========: {}", accessToken);
            return accessToken;
        } else if (type == 1) {
            token_link = env.getProperty("facebook.link.get.token");
            client_id = env.getProperty("facebook.app.id");
            client_secret = env.getProperty("facebook.app.secret");
            String link = token_link;
            String response = Request.Post(link).bodyForm(Form.form().add("client_id", client_id).add("client_secret", client_secret).add("code", code).add("redirect_uri", env.getProperty("redirect.uri")).build()).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response).get("access_token");
            return node.textValue();
        } else if (type == 2) {
            token_link = env.getProperty("tiktok.link.get.token");
            client_id = env.getProperty("tiktok.app.id");
            client_secret = env.getProperty("tiktok.app.secret");
            String link = token_link;
            String response = Request.Post(link).bodyForm(Form.form().add("client_key", client_id).add("client_secret", client_secret).add("code", code).add("grant_type", "authorization_code").build()).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response).get("access_token");
            return node.textValue();
        }
        return "";
    }

    public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.user.info") + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
        System.out.println("Output data: " + googlePojo);
        return googlePojo;
    }

}
