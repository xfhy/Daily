package com.xfhy.androidbasiclibs.common.util;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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

    /**
     * 初始化OkHttp
     */
    public static void initOkHttp(Context context) {
        //缓存文件
        File cacheFile = context.getCacheDir();
        int cacheSize = 10 * 1024 * 1024;  //缓存的maxSize

        if (cacheFile != null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                    .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                    .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                    .pingInterval(20, TimeUnit.SECONDS) //WebSocket轮训间隔(单位:秒)
                    .cache(new Cache(cacheFile.getAbsoluteFile(), cacheSize))//设置缓存
                    .cookieJar(new CookieJar() {  //设置缓存自动管理
                        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                        @Override
                        public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie>
                                cookies) {
                            cookieStore.put(url, cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(url);
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder().build();
        }

    }

}
