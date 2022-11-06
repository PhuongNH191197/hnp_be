package com.elcom.plus.mecall.config.log;

import com.elcom.plus.mecall.config.ConfigLog;
import org.apache.log4j.Logger;

public class Log {
    private static Logger log = null;

    public static Logger log() {
        if (log == null) {
            log = ConfigLog.ConfigLogger();
        }
        return log;
    }
}
