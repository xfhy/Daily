package com.xfhy.daily.model.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.xfhy.androidbasiclibs.adapter.entity.SectionEntity;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/26 21:03
 * description：最新日报列表
 */
public class LatestDailyListBean {


    /*
     *
     * 下面是json数据
     * date : 20170926
     * stories : [{"images":["https://pic1.zhimg.com/v2-6e35c40eae40c3b6ae0c3d0570961388.jpg"],
     * "type":0,"id":9632928,"ga_prefix":"092621","title":"在中东，电影起到了它应该起到的作用"},
     * {"images":["https://pic4.zhimg.com/v2-2d782b0ef999bbe4ea3f6f4b3079d19f.jpg"],"type":0,
     * "id":9627707,"ga_prefix":"092620","title":"下次吃到可口的饭菜除了感谢厨师，还要感谢选择坐在这里的你"},
     * {"images":["https://pic2.zhimg.com/v2-9eb448fe2f2fb58a05a0923b75c1efcd.jpg"],"type":0,
     * "id":9631314,"ga_prefix":"092619","title":"想要出国读最理想的大学，我不建议你去找留学中介"},
     * {"images":["https://pic4.zhimg.com/v2-4b2375360a9e40c32722b8e174a5d2ab.jpg"],"type":0,
     * "id":9632628,"ga_prefix":"092618","title":"劝你别老想着怎么用「努力工作」去感动老板了，没用"},
     * {"images":["https://pic1.zhimg.com/v2-8b4260d35c8ec5098845bae7e2170c9c.jpg"],"type":0,
     * "id":9632942,"ga_prefix":"092617","title":"「黄河变清了要发生大洪水」，这个标题党过分了"},
     * {"images":["https://pic1.zhimg.com/v2-2dc89c52ee87cf3685d11e30a22f6ad8.jpg"],"type":0,
     * "id":9632660,"ga_prefix":"092616","title":"地铁司机为什么每次都能把车停得那么准？"},
     * {"images":["https://pic1.zhimg.com/v2-f98c96261f90c7e53d247ac568354840.jpg"],"type":0,
     * "id":9632854,"ga_prefix":"092615","title":"赵薇老师，你还能不能有点明星样儿了？"},
     * {"images":["https://pic1.zhimg.com/v2-a70baa601177fa62c5497918492c5adc.jpg"],"type":0,
     * "id":9630951,"ga_prefix":"092614","title":"「不主动追求你，也不明确拒绝你」，这就是现代人的爱情"},
     * {"images":["https://pic2.zhimg.com/v2-53d44e78c49326cd364cf8b410ee0ad5.jpg"],"type":0,
     * "id":9632521,"ga_prefix":"092613","title":"听说同时吃它俩，像是在吃「蘸了肥皂水的苍蝇」\u2026\u2026我亲自试了试"},
     * {"images":["https://pic2.zhimg.com/v2-e338d3d8cd602c490f154290fd3b7729.jpg"],"type":0,
     * "id":9632523,"ga_prefix":"092612","title":"十万想法攻占地球：你看，有这么多想法的地球多美"},
     * {"images":["https://pic2.zhimg.com/v2-7260bbcd7a6400e360c44dd567fda4bd.jpg"],"type":0,
     * "id":9629244,"ga_prefix":"092612","title":"大误 · 木叶众人的谥号我都想好了"},
     * {"images":["https://pic3.zhimg.com/v2-95d18904934a5d94ec9c1b0e0604b4ce.jpg"],"type":0,
     * "id":9631032,"ga_prefix":"092611","title":"大学校长说：同学们，你们已经一只脚踏进了监狱"},
     * {"images":["https://pic2.zhimg.com/v2-2818944a0cbeb264b94a072d716d44d9.jpg"],"type":0,
     * "id":9630795,"ga_prefix":"092610","title":"为什么要「脱裤子放屁」？一不小心可能就得多赔 35 亿美元"},
     * {"images":["https://pic1.zhimg.com/v2-1453248d69fdd6e1336c0a679538c010.jpg"],"type":0,
     * "id":9626002,"ga_prefix":"092609","title":"你骑着共享单车经过的轨迹，都被我们用来做城市规划了"},
     * {"images":["https://pic2.zhimg.com/v2-8d164b361ef6a631e6bdfd3db429b359.jpg"],"type":0,
     * "id":9627387,"ga_prefix":"092608","title":"《星球大战》看过一百多遍，却没想过为什么都叫它星「球」"},
     * {"images":["https://pic1.zhimg.com/v2-9cb623ac9091ffd20d84d44f370dc974.jpg"],"type":0,
     * "id":9631247,"ga_prefix":"092607","title":"钱包到月末又快瘪了，感觉花钱的方式越来越刺激和意想不到"},
     * {"images":["https://pic2.zhimg.com/v2-229ef8a429fa6081c69b88130280cbfd.jpg"],"type":0,
     * "id":9631230,"ga_prefix":"092607","title":"「我已经没有了成长的能力」，这是一家 AI 创业公司的离场告白"},
     * {"images":["https://pic4.zhimg.com/v2-1785330ca9740f02c077d995c197eb4f.jpg"],"type":0,
     * "id":9631365,"ga_prefix":"092607","title":"消费贷的「原罪」：有人拆东墙补西墙，有人薅羊毛反被割韭菜"},
     * {"images":["https://pic2.zhimg.com/v2-4821873eb7e45c070c0d14a135c8599d.jpg"],"type":0,
     * "id":9631117,"ga_prefix":"092606","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic3.zhimg.com/v2-e885c8acf8ca274cda11dd8ce7760d26.jpg",
     * "type":0,"id":9632942,"ga_prefix":"092617","title":"「黄河变清了要发生大洪水」，这个标题党过分了"},
     * {"image":"https://pic3.zhimg.com/v2-ecfc08ebca9f42e29144d09050fb619e.jpg","type":0,
     * "id":9632660,"ga_prefix":"092616","title":"地铁司机为什么每次都能把车停得那么准？"},
     * {"image":"https://pic1.zhimg.com/v2-0d3e0c7d778083003e6f8029cf7cf570.jpg","type":0,
     * "id":9632854,"ga_prefix":"092615","title":"赵薇老师，你还能不能有点明星样儿了？"},
     * {"image":"https://pic1.zhimg.com/v2-5d0f06845b6cdd5e32aed0a960aede98.jpg","type":0,
     * "id":9632521,"ga_prefix":"092613","title":"听说同时吃它俩，像是在吃「蘸了肥皂水的苍蝇」\u2026\u2026我亲自试了试"},
     * {"image":"https://pic2.zhimg.com/v2-4d873d82642d347aa0e709b2e2f5be81.jpg","type":0,
     * "id":9630951,"ga_prefix":"092614","title":"「不主动追求你，也不明确拒绝你」，这就是现代人的爱情"}]
     */

    /**
     * 日期   eg:20170927
     */
    private String date;
    /**
     * 当日日报
     */
    private List<StoriesBean> stories;

    /**
     * 界面顶部 ViewPager 滚动显示的显示内容（子项格式同上）
     * （请注意区分此处的 `image` 属性与 `stories` 中的 `images` 属性）
     */
    @JSONField(name = "top_stories")
    private List<TopStoriesBean> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<TopStoriesBean> topStories) {
        this.topStories = topStories;
    }

    public static class StoriesBean extends SectionEntity {
        /*
         * images : ["https://pic1.zhimg.com/v2-6e35c40eae40c3b6ae0c3d0570961388.jpg"]
         * type : 0
         * id : 9632928
         * ga_prefix : 092621
         * title : 在中东，电影起到了它应该起到的作用
         */

        /**
         * 作用未知
         */
        private int type;
        /**
         * `url` 与 `share_url` 中最后的数字（应为内容的 id）
         */
        private int id;
        /**
         * 供 Google Analytics 使用
         */
        @JSONField(name = "ga_prefix")
        private String gaPrefix;
        /**
         * 新闻标题
         */
        private String title;
        /**
         * 图像地址（官方 API 使用数组形式。
         * 目前暂未有使用多张图片的情形出现，
         * 曾见无 `images` 属性的情况，请在使用中注意 ）
         */
        private List<String> images;

        public StoriesBean() {
        }

        public StoriesBean(boolean isHeader) {
            super(isHeader);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public void setGaPrefix(String gaPrefix) {
            this.gaPrefix = gaPrefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * 曾见无 `images` 属性的情况，请在使用中注意
         *
         * @return 图片地址集合
         */
        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "type=" + type +
                    ", id=" + id +
                    ", gaPrefix='" + gaPrefix + '\'' +
                    ", title='" + title + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public static class TopStoriesBean {
        /*
         * image : https://pic3.zhimg.com/v2-e885c8acf8ca274cda11dd8ce7760d26.jpg
         * type : 0
         * id : 9632942
         * ga_prefix : 092617
         * title : 「黄河变清了要发生大洪水」，这个标题党过分了
         */

        /**
         * 图片地址
         */
        private String image;
        private int type;
        /**
         * `url` 与 `share_url` 中最后的数字（应为内容的 id）
         */
        private int id;
        /**
         * 供 Google Analytics 使用
         */
        @JSONField(name = "ga_prefix")
        private String gaPrefix;
        /**
         * 标题
         */
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public void setGaPrefix(String gaPrefix) {
            this.gaPrefix = gaPrefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "TopStoriesBean{" +
                    "image='" + image + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", gaPrefix='" + gaPrefix + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LatestDailyListBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", topStories=" + topStories +
                '}';
    }
}
