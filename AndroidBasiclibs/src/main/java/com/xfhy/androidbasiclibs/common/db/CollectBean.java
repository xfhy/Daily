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
     * 收藏的那个对象的唯一标识   比如知乎的是id  通过id可以唯一找到那篇文章
     */
    private String key;

    /**
     * 用于标识来源  比如:来源是知乎
     */
    private String from;

    /**
     * 时间戳   收藏的时间戳
     */
    private String collectionDate;

    @Generated(hash = 420494524)
    public CollectBean() {
    }

    @Generated(hash = 888474839)
    public CollectBean(Long id, String key, String from, String collectionDate) {
        this.id = id;
        this.key = key;
        this.from = from;
        this.collectionDate = collectionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
                ", key='" + key + '\'' +
                ", from='" + from + '\'' +
                ", collectionDate='" + collectionDate + '\'' +
                '}';
    }
}
