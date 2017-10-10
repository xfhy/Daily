package com.xfhy.androidbasiclibs.common.db;

import com.xfhy.androidbasiclibs.NewsApplication;

import java.util.List;

/**
 * author feiyang
 * create at 2017/10/10 11:54
 * description：用户缓存数据库的增删改查
 */
public class CacheDao {

    /**
     * 插入cache  如果已经存在则替换
     *
     * @param cacheBean CacheBean数据
     */
    public static void insertCache(CacheBean cacheBean) {
        NewsApplication.getDaoSession().insertOrReplace(cacheBean);
    }

    /**
     * 删除缓存
     *
     * @param cacheBean CacheBean
     */
    public static void deleteCache(CacheBean cacheBean) {
        NewsApplication.getDaoSession().delete(cacheBean);
    }

    /**
     * 更新缓存
     *
     * @param cacheBean CacheBean
     */
    public static void updateCache(CacheBean cacheBean) {
        NewsApplication.getDaoSession().update(cacheBean);
    }

    /**
     * 查询指定key的缓存
     *
     * @param key 缓存的key  唯一标识
     * @return 返回查询到的key的缓存集合  一般来说,这里是返回1个,当然,可能数据库里面没有该key对应的缓存
     */
    public static List<CacheBean> queryCacheByKey(String key) {
        //CacheEntityDao是自动生成的里面是一些数据库操作
        //然后这里的Properties.Key也是自动生成的,意思是表里面的一个字段
        return NewsApplication.getDaoSession().queryBuilder(CacheBean.class).where(CacheEntityDao
                .Properties.Key.eq(key)).list();
    }

    /**
     * 查询全部缓存数据
     *
     * @return 缓存集合
     */
    public static List<CacheBean> queryAllCache() {
        return NewsApplication.getDaoSession().loadAll(CacheBean.class);
    }

}
