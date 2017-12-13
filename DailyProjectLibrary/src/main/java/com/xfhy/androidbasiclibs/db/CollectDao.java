package com.xfhy.androidbasiclibs.db;

import com.xfhy.androidbasiclibs.BaseApplication;

import java.util.List;

/**
 * @author feiyang
 * @time create at 2017/11/7 12:19
 * @description 用户收藏数据库的增删改查
 */
public class CollectDao {

    /**
     * 插入收藏  如果已经存在则替换
     *
     * @param collectBean collectBean数据
     */
    public static void insertCollect(CollectBean collectBean) {
        BaseApplication.getDaoSession().insertOrReplace(collectBean);
    }

    /**
     * 删除收藏
     *
     * @param id 删除的时候只需要设置好该对象的id就行,
     */
    public static void deleteCache(Long id) {
        CollectBean collectBean = new CollectBean();
        collectBean.setId(id);
        BaseApplication.getDaoSession().delete(collectBean);
    }

    /**
     * 更新收藏
     *
     * @param collectBean collectBean
     */
    public static void updateCache(CollectBean collectBean) {
        BaseApplication.getDaoSession().update(collectBean);
    }

    /**
     * 查询指定from的收藏
     *
     * @param from 收藏的来源
     * @return 返回查询到的from的收藏集合
     * 注意:这里返回可能为空,当未查询到该from所对应的元组   GreenDao要抛出异常  此时返回null
     * 查询成功,这里是返回1个,当然,可能数据库里面没有该from对应的收藏
     */
    public static List<CollectBean> queryCacheByFrom(String from) {
        //CollectBeanDao是自动生成的里面是一些数据库操作
        //然后这里的Properties.Key也是自动生成的,意思是表里面的一个字段
        try {
            return BaseApplication.getDaoSession().getCollectBeanDao()
                    .queryBuilder().where(CollectBeanDao.Properties.From.eq(from)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据key进行查询  key是唯一标识
     *
     * @param key 唯一标识
     * @return 返回查询到的集合  可能为null
     */
    public static List<CollectBean> queryCacheByKey(String key) {
        //CollectBeanDao是自动生成的里面是一些数据库操作
        //然后这里的Properties.Key也是自动生成的,意思是表里面的一个字段
        try {
            return BaseApplication.getDaoSession().getCollectBeanDao()
                    .queryBuilder().where(CollectBeanDao.Properties.Key.eq(key)).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询全部收藏数据
     *
     * @return 收藏集合
     */
    public static List<CollectBean> queryAllCache(DaoSession daoSession) {
        return daoSession.loadAll(CollectBean.class);
    }

}
