package com.elcom.plus.mecall.config.db;

import com.elcom.plus.mecall.config.Config;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.Properties;

public class LoadConfig {
    private FileInputStream fis = null;
    private Properties pro = null;
    private static String user = ConnectionManager.decrypt("RmfNnskXLJwRWbqYpvvklA==");
    private static String pass = ConnectionManager.decrypt("2so6E7oP9Orzd2sVX1vrcD4Cxf8aPOFi");

    public void loadConfig() {
        try {
            fis = new FileInputStream(Config.pathFileConfig);
            pro = new Properties();
            pro.load(new InputStreamReader(fis, "UTF-8"));
            pro.list(System.out);

//            Config.LOG = pro.getProperty("LOG").trim();
            Config.DRIVER = pro.getProperty("spring.datasource.driver-class-name").trim();
            Config.URL = pro.getProperty("spring.datasource.url").trim();
            Config.USER = pro.getProperty("spring.datasource.username").trim();
            Config.PASS = pro.getProperty("spring.datasource.password").trim();

        } catch (Exception e) {
            System.err.println("Not load config" + e.getMessage());
            System.err.println("Not load config" + e);
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
                System.err.println("Not close file config!" + ex.getMessage());
                System.err.println("Not close file config!" + ex);
            }
        }
    }

    public void checkConfig() {
        try {
            String fileUploadStr = Config.CONFIG_FOLDER_UPLOAD;
            File fileUpload = new File(fileUploadStr);
            if (!fileUpload.exists()) {
                fileUpload.mkdirs();
            }

            String fileStr = Config.CONFIG_FOLDER_NAME;
            File file = new File(fileStr);
            if (!file.exists()) {
                file.mkdirs();
            }

            fileStr += Config.FOLDER_NAME_PROJECT;
            file = new File(fileStr);
            if (!file.exists()) {
                file.mkdirs();
            }

            createDefaultConfigFile(file, fileStr);
            createDefaultConfigLogFile(file, fileStr);

        } catch (Exception e) {
            System.err.println("Not check config" + e);
        }
    }

    private void createDefaultConfigFile(File file, String fileStr) {
        String fileNew = fileStr + Config.CONFIG_FILE_NAME;
        BufferedWriter w;

        try {
            file = new File(fileNew);
            if (!file.exists()) {
                w = new BufferedWriter(new FileWriter(file));
                w.write("LOG=log");
                w.newLine();
                w.write("DRIVER=com.mysql.jdbc.Driver");
                w.newLine();
//                w.write("URL=jdbc:mysql://192.168.10.154:3306/CMP");
                w.write("URL=jdbc:mysql://192.168.10.2:3306/CMP");
                w.newLine();
                w.write("USER=" + user);
                w.newLine();
                w.write("PASS=" + pass);
                w.newLine();
                w.flush();
                w.close();
            }
        } catch (Exception ex) {
            System.err.println("Not build config" + ex);
        }
    }

    private void createDefaultConfigLogFile(File file, String fileStr) {
        String fileNew = fileStr + Config.CONFIG_FILE_NAME_LOG;
        String pathLog = Config.BASE_FOLDER + "log" + File.separator + Config.FOLDER_NAME_PROJECT + "log.log";
        pathLog = pathLog.replace("\\", "\\\\");

        BufferedWriter w;
        try {
            file = new File(fileNew);
            if (!file.exists()) {
                w = new BufferedWriter(new FileWriter(file));
                w.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                w.newLine();
                w.write("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");
                w.newLine();
                w.write("<log4j:configuration xmlns:log4j=\"http://jakarta.apache.org/log4j/\" debug=\"false\">");
                w.newLine();
                w.write("\t<appender name=\"LOG_FILE\" class=\"org.apache.log4j.DailyRollingFileAppender\">");
                w.newLine();
                w.write("\t\t<param name=\"Threshold\" value=\"INFO\" />");
                w.newLine();
                w.write("\t\t<param name=\"File\" value=\"" + pathLog + "\" />");
                w.newLine();
                w.write("\t\t<param name=\"Append\" value=\"true\" />");
                w.newLine();
                w.write("\t\t<param name=\"DatePattern\" value=\".yyyyMMdd_HH\" />");
                w.newLine();
                w.write("\t\t<layout class=\"org.apache.log4j.PatternLayout\">");
                w.newLine();
                w.write("\t\t\t<param name=\"ConversionPattern\" value=\"[%-5p] - [%-23d{ISO8601}] - [%-28t] - %m%n\" />");
                w.newLine();
                w.write("\t\t</layout>");
                w.newLine();
                w.write("\t</appender>");
                w.newLine();
                w.write("\t<category name=\"log\" additivity=\"false\">");
                w.newLine();
                w.write("\t\t<appender-ref ref=\"LOG_FILE\" />");
                w.newLine();
                w.write("\t</category>");
                w.newLine();
                w.write("</log4j:configuration>");
                w.newLine();

                w.flush();
                w.close();
            }
        } catch (Exception ex) {
            System.err.println("Not build config log" + ex);
        }
    }
}
