package com.xfhy.daily;

import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.xfhy.androidbasiclibs.BaseApplication;
import com.xfhy.androidbasiclibs.net.OkHttpUtils;
import com.xfhy.androidbasiclibs.util.UserInfoTools;

/**
 * author feiyang
 * create at 2017/9/15 12:55
 * description：整个应用程序的Application
 */
public class NewsApplication extends BaseApplication {

    private static NewsApplication sNewsApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sNewsApplication = this;

        initNightMode();
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
        initLeakCanary();
    }

    /**
     * 检测内存泄露
     */
    private void initLeakCanary() {
        LeakCanary.install(this);
        // Normal app init code...
    }

    /**
     * 初始化夜间模式
     */
    private void initNightMode() {
        // 判断用户之前是否开启夜间模式
        boolean nightMode = UserInfoTools.isNightMode(this);
        AppCompatDelegate.setDefaultNightMode(nightMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static NewsApplication getAppContext() {
        return sNewsApplication;
    }
}
