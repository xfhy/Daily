package com.xfhy.androidbasiclibs;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xfhy.androidbasiclibs.basekit.activity.BaseActivity;
import com.xfhy.androidbasiclibs.common.utils.LogUtils;
import com.xfhy.androidbasiclibs.common.utils.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
 * author feiyang
 * create at 2017/9/15 12:55
 * description：整个应用程序的Application
 */
public class NewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initLibrary();
    }

    /**
     * 初始化第三方库
     */
    private void initLibrary() {
        //日志打印
        Logger.addLogAdapter(new AndroidLogAdapter());
        //初始化OKHttp
        initOkHttp();
    }

    /**
     * 初始化OkHttp
     */
    private void initOkHttp() {
        //缓存
        File cache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;  //maxSize

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//设置缓存
                .cookieJar(new CookieJar() {  //设置缓存自动管理
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
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

        OkHttpUtils.initClient(okHttpClient);
    }

}
