package com.xfhy.daily.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/28 15:23
 * description：热门日报
 * <p>
 * 请注意！此 API 仍可访问，但是其内容未出现在最新的『知乎日报』 App 中。
 */
public class HotDailyBean {

    /**
     * 日报列表
     */
    private List<RecentBean> recent;

    public List<RecentBean> getRecent() {
        return recent;
    }

    public void setRecent(List<RecentBean> recent) {
        this.recent = recent;
    }

    public static class RecentBean {
        /**
         * news_id : 9631117
         * url : http://news-at.zhihu.com/api/2/news/9631117
         * thumbnail : https://pic2.zhimg.com/v2-4821873eb7e45c070c0d14a135c8599d.jpg
         * title : 瞎扯 · 如何正确地吐槽
         */

        /**
         * 日报id
         */
        @JSONField(name = "news_id")
        private int newsId;
        /**
         * 日报详情url 可以直接使用的   返回{@link DailyContentBean}的是JSON数据
         */
        private String url;
        /**
         * 日报小图片  (用于列表展示时的小图片)
         */
        private String thumbnail;
        /**
         * 日报标题
         */
        private String title;

        public int getNewsId() {
            return newsId;
        }

        public void setNewsId(int newsId) {
            this.newsId = newsId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "RecentBean{" +
                    "newsId=" + newsId +
                    ", url='" + url + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HotDailyBean{" +
                "recent=" + recent +
                '}';
    }
}
