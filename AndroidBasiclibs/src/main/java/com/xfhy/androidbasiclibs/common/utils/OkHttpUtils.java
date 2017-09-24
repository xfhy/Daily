package com.xfhy.androidbasiclibs.common.utils;

import okhttp3.OkHttpClient;

/**
 * Created by xfhy on 2017/9/24 22:18.
 * Description 网络模块的Utils
 */

public class OkHttpUtils {
    /**
     * 获取OkHttp客户端
     * 用于管理所有的请求，内部支持并发，所以我们不必每次请求都创建一个 OkHttpClient
     * 对象，这是非常耗费资源的
     */
    private static OkHttpClient okHttpClient = null;

    /**
     * 初始化OkHttp客户端
     */
    public static void initClient(OkHttpClient okHttpClient) {
        OkHttpUtils.okHttpClient = okHttpClient;
    }

    public static synchronized OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
