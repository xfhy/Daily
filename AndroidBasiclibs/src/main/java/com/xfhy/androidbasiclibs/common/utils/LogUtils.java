package com.xfhy.androidbasiclibs.common.utils;

import com.orhanobut.logger.Logger;

/**
 * author feiyang
 * create at 2017/9/22 10:27
 * description：Log日志工具类
 */
public class LogUtils {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    //控制这个即可控制Logger输出
    public static final int LEVEL = VERBOSE;

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            Logger.e(msg);
        }
    }

    /**
     * 格式化json并打印log
     * @param json json content
     */
    public static void json(String json) {
        if (LEVEL != NOTHING) {
            Logger.json(json);
        }
    }

    /**
     * 格式化xml并打印log
     * @param xml xml content
     */
    public static void xml(String xml) {
        if (LEVEL != NOTHING) {
            Logger.xml(xml);
        }
    }

}
