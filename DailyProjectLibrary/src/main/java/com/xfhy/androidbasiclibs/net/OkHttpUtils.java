package com.xfhy.androidbasiclibs.net;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
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
     * 最大缓存 10M
     */
    private final static int MAX_CACHE_SIZE = 10 * 1024 * 1024;
    /**
     * 读取超时 20s
     */
    private final static int READ_TIME_OUT = 20;
    /**
     * 连接超时
     */
    private final static int CONNECT_TIME_OUT = 15;

    /**
     * 初始化OkHttp客户端
     */
    public static void initClient(OkHttpClient okHttpClient) {
        OkHttpUtils.okHttpClient = okHttpClient;
    }

    public static synchronized OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 初始化OkHttp
     */
    public static void initOkHttp(Context context) {
        //拦截器
        RewriteCacheControlInterceptor mRewriteCacheControlInterceptor = new RewriteCacheControlInterceptor();

        //缓存文件
        File cacheFile = context.getCacheDir();
        //设置缓存大小
        Cache cache = new Cache(cacheFile, MAX_CACHE_SIZE);
        if (cacheFile != null) {
            okHttpClient = new OkHttpClient.Builder()
                    //超时设置
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    //错误重连
                    .retryOnConnectionFailure(true)
                    //拦截器
                    .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                    .addInterceptor(mRewriteCacheControlInterceptor)
                    //缓存
                    .cache(cache)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder().build();
        }

    }

}
