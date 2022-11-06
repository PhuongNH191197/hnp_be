package com.elcom.plus.auth.entity;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class mahoa {
    public static void main(String[] args) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("elcomviettelvrbt"); // Khóa bí mật để mã hóa
        config.setAlgorithm("PBEWithMD5AndDES"); // Thuật toán mã hóa
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        String encryptedString = encryptor.encrypt("Anhphuong97");
        System.out.println(encryptedString);
    }
}
