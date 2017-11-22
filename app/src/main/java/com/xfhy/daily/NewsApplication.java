package com.xfhy.daily;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.xfhy.androidbasiclibs.common.db.DaoMaster;
import com.xfhy.androidbasiclibs.common.db.DaoSession;
import com.xfhy.androidbasiclibs.common.net.OkHttpUtils;
import com.xfhy.androidbasiclibs.common.util.UserInfoTools;

import org.greenrobot.greendao.database.Database;

/**
 * author feiyang
 * create at 2017/9/15 12:55
 * description：整个应用程序的Application
 */
public class NewsApplication extends Application {

    private static Context context;

    /**
     * 获取Dao对象管理者  Dao对象中存在着增删改查等API
     */
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initNightMode();
        initLibrary();
        initDatabase();

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
     * 数据库
     */
    private void initDatabase() {
        //创建数据库news.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "news.db", null);
        //获取可写数据库
        Database db = helper.getWritableDb();
        //获取Dao对象管理者
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * 获取Dao对象管理者
     *
     * @return DaoSession
     */
    public static DaoSession getDaoSession() {
        return daoSession;
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

    public static Context getAppContext() {
        return context;
    }
}
