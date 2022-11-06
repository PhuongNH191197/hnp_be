package com.elcom.plus.auth.client.db;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {



    private String url = "jdbc:mysql://localhost:3306/WAP_AUTH";
//        private String url = "jdbc:mysql://103.170.122.123:3306/WAP_AUTH";

    private static final ConnectionManager instance = new ConnectionManager();
    public ConnectionManager(){
    }

    public static ConnectionManager getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            String username="aJ3kf1YcW/MSNGjX9dkBCA=="; //root
//            String username="rrm+L0IbpGsJ8FJ4OGXZCg=="; // hnp
            String password="cZTHA3Bvq/xkLHFk3KQg5g==";// 123456
//            String password="N9PQlWD2W7iDlnI397yHLgq4xSRAt/jn";// Anhphuong97
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println(url);
            System.out.println(decrypt(username));
            System.out.println(decrypt(password));
            Connection con = DriverManager.getConnection(url, decrypt(username), decrypt(password));
            if (con != null && !con.isClosed()) {
                con.setAutoCommit(false);
            }
            System.out.println("Connection established......");
            return con;
        } catch (Exception e) {
            System.err.println("Not connection ==>" + e.getMessage());
            System.err.println("Not connection ==>" + e);
        }
        return null;
    }

    public String decrypt(String value) {
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
