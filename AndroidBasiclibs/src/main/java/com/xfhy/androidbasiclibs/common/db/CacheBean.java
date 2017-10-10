package com.xfhy.androidbasiclibs.common.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author feiyang
 * create at 2017/10/10 11:31
 * description：将每次网络请求到的json数据缓存到数据库
 */
@Entity
public class CacheBean {
    /**
     * 对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
     */
    @Id(autoincrement = true)
    private Long id;
    /**
     * 该请求的唯一标识   比如 知乎->最新日报  :  zhihu_latest_daily
     */
    @Unique
    private String key;
    /**
     * 网络返回的json数据
     */
    private String json;

    @Generated(hash = 651543594)
    public CacheBean(Long id, String key, String json) {
        this.id = id;
        this.key = key;
        this.json = json;
    }

    @Generated(hash = 1391258017)
    public CacheBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "CacheBean{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
