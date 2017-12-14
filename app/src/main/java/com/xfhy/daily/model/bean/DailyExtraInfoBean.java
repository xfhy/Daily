package com.xfhy.daily.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author feiyang
 * create at 2017/9/27 15:28
 * description：日报额外信息
 */
public class DailyExtraInfoBean {


    /*
     * long_comments : 0
     * popularity : 33
     * short_comments : 14
     * comments : 14
     */

    /**
     * 长评论总数
     */
    @JSONField(name = "long_comments")
    private int longComments;
    /**
     * 点赞总数
     */
    private int popularity;
    /**
     * 短评论总数
     */
    @JSONField(name = "short_comments")
    private int shortComments;
    /**
     * 评论总数
     */
    private int comments;

    public int getLongComments() {
        return longComments;
    }

    public void setLongComments(int longComments) {
        this.longComments = longComments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShortComments() {
        return shortComments;
    }

    public void setShortComments(int shortComments) {
        this.shortComments = shortComments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "DailyExtraInfoBean{" +
                "longComments=" + longComments +
                ", popularity=" + popularity +
                ", shortComments=" + shortComments +
                ", comments=" + comments +
                '}';
    }
}
