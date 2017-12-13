package com.xfhy.androidbasiclibs;

import android.app.Application;

import com.xfhy.androidbasiclibs.db.DaoMaster;
import com.xfhy.androidbasiclibs.db.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by feiyang on 2017/12/13 13:46
 * Description : Application的基类
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    /**
     * 获取Dao对象管理者  Dao对象中存在着增删改查等API
     */
    private static DaoSession sDaoSession;

    public static BaseApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initDatabase();
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
        sDaoSession = new DaoMaster(db).newSession();
    }

    /**
     * 获取Dao对象管理者
     *
     * @return DaoSession
     */
    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

}
