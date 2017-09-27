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
        OkHttpUtils.initOkHttp(getApplicationContext());
    }

}
