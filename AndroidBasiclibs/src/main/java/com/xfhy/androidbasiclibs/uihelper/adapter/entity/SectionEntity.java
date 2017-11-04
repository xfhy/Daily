package com.xfhy.androidbasiclibs.uihelper.adapter.entity;

import java.io.Serializable;

/**
 * author feiyang
 * create at 2017/10/24 13:35
 * description：所有分组布局实体类的父类
 */
public class SectionEntity<T> implements Serializable{
    /**
     * 是否是分组header
     */
    public boolean isHeader;
    /**
     * 包装的实体类,可以没有
     */
    public T t;
    /**
     * 分组header标题
     */
    public String header;

    public SectionEntity() {
        this.isHeader = false;
        this.header = null;
        this.t = null;
    }

    /**
     * @param isHeader true:是分组header false:不是
     */
    public SectionEntity(boolean isHeader){
        this.isHeader = isHeader;
        this.header = null;
        this.t = null;
    }

    public SectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }

    public SectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }



}
