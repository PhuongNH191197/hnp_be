package com.elcom.plus.mecall.config;

import java.io.File;

public class Config {
    public static String PATH = "/opt/vrbt/bin/backend";//System.getProperty("user.home")
    public static String BASE_FOLDER = PATH + File.separator;
    public static String CONFIG_FOLDER_NAME = BASE_FOLDER + "config" + File.separator;
    public static String CONFIG_FOLDER_UPLOAD = BASE_FOLDER + "upload";
    public static String FOLDER_NAME_PROJECT = "mecall" + File.separator;
    public static String CONFIG_FILE_NAME = "configweb.properties";
    public static String CONFIG_FILE_NAME_LOG = "configlog.xml";

    public static String pathFileConfig = BASE_FOLDER + "config" + File.separator + FOLDER_NAME_PROJECT + CONFIG_FILE_NAME;
    public static String pathFileConfigLog = BASE_FOLDER + "config" + File.separator + FOLDER_NAME_PROJECT + CONFIG_FILE_NAME_LOG;

    public static String LOG;

    public static String DRIVER;
    public static String URL;
    public static String USER;
    public static String PASS;
}
