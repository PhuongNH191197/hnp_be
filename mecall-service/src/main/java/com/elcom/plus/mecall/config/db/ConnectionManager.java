package com.elcom.plus.mecall.config.db;

import org.bouncycastle.asn1.cms.ecc.MQVuserKeyingMaterial;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    public static Connection getConnection() {
        try {
//            String url = "jdbc:mysql://192.168.10.154:3306/CMP";
            String url = "jdbc:mysql://192.168.10.2:3306/CMP";
            String user = "RmfNnskXLJwRWbqYpvvklA==";
            String pass = "2so6E7oP9Orzd2sVX1vrcD4Cxf8aPOFi";

            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Connection con = DriverManager.getConnection(url, decrypt(user), decrypt(pass));
            if (con != null && !con.isClosed()) {
                con.setAutoCommit(false);
            }
            System.out.println("Connection established......");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("elcomviettelvrbt");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String decrypt = encryptor.decrypt(value);
        return decrypt;
    }
}
