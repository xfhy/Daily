package com.xfhy.androidbasiclibs.common.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author feiyang
 * @time create at 2017/11/7 11:33
 * @description 收藏
 */
@Entity
public class CollectBean {

    /**
     * 对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
     */
    @Id(autoincrement = true)
    private Long id;

    /**
     * 收藏的那个对象的json数据
     */
    private String json;

    /**
     * 用于标识来源  比如:来源是知乎
     */
    private String from;

    /**
     * 时间戳   收藏的时间戳
     */
    private String collectionDate;

    @Generated(hash = 276088480)
    public CollectBean(Long id, String json, String from, String collectionDate) {
        this.id = id;
        this.json = json;
        this.from = from;
        this.collectionDate = collectionDate;
    }

    @Generated(hash = 420494524)
    public CollectBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    @Override
    public String toString() {
        return "CollectBean{" +
                "id=" + id +
                ", json='" + json + '\'' +
                ", from='" + from + '\'' +
                ", collectionDate='" + collectionDate + '\'' +
                '}';
    }
}
