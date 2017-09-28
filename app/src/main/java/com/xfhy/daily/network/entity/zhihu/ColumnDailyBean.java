package com.xfhy.daily.network.entity.zhihu;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/28 14:55
 * description：专栏日报
 */
public class ColumnDailyBean {

    /**
     * 专栏列表
     */
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * description : 看别人的经历，理解自己的生活
         * id : 1
         * name : 深夜惊奇
         * thumbnail : http://pic3.zhimg.com/91125c9aebcab1c84f58ce4f8779551e.jpg
         */

        /**
         * 专栏描述
         */
        private String description;
        /**
         * 专栏id
         */
        private int id;
        /**
         * 专栏名称
         */
        private String name;
        /**
         * 获取图像的地址
         */
        private String thumbnail;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "description='" + description + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ColumnDailyBean{" +
                "data=" + data +
                '}';
    }
}
