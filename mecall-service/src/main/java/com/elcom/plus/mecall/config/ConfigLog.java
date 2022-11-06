package com.elcom.plus.mecall.config;

import org.apache.log4j.Logger;

public class ConfigLog {
    public static Logger ConfigLogger() {
        Logger logData = Logger.getLogger(Config.LOG);
        return logData;
    }
}
