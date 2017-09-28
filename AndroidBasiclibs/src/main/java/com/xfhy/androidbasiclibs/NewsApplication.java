package com.xfhy.androidbasiclibs;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xfhy.androidbasiclibs.common.util.OkHttpUtils;

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
