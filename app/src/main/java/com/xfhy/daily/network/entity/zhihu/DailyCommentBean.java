package com.xfhy.daily.network.entity.zhihu;

import java.util.List;

/**
 * author feiyang
 * create at 2017/9/27 17:34
 * description：日报评论   长评论和短评论都是一样的格式
 */
public class DailyCommentBean {

    /**
     * 长评论列表，形式为数组（请注意，其长度可能为 0）
     */
    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /*
         * author : andy小陆
         * content :
         * 《小仙有毒》里，温家的入室弟子考题有一年是“硕鼠”。温家大伯温吞海当年应试的毒方是采尽天下至甜至香之物，密炼熬成一碗甜羹，无毒且馥郁甘甜。但人一旦饮下此羹，尝到了那绝世甜香，之后哪怕是喝蜂蜜也会觉得腥臭苦涩无比，止不住的呕吐，从此世上可食之物就只剩一个味道：苦，最终竟把人活活饿死！正应了考题“硕鼠”。。
         * avatar : http://pic3.zhimg.com/4953f864a_im.jpg
         * time : 1479737963
         * id : 27279755
         * likes : 9
         * reply_to : {"content":"第二个机灵抖的还是有逻辑问题，不该说忘了，应该说没喝过啊我也不知道","status":0,"id":27275308,
         * "author":"2233155495"}
         */

        /**
         * 评论作者
         */
        private String author;
        /**
         * 评论的内容
         */
        private String content;
        /**
         * 用户头像图片的地址
         */
        private String avatar;
        /**
         * 评论时间
         */
        private int time;
        /**
         * 评论者的唯一标识符
         */
        private int id;
        /**
         * 评论所获『赞』的数量
         */
        private int likes;
        /**
         * 所回复的消息
         */
        private ReplyToBean reply_to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        @Override
        public String toString() {
            return "CommentsBean{" +
                    "author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", time=" + time +
                    ", id=" + id +
                    ", likes=" + likes +
                    ", reply_to=" + reply_to +
                    '}';
        }

        public static class ReplyToBean {
            /*
             * content : 第二个机灵抖的还是有逻辑问题，不该说忘了，应该说没喝过啊我也不知道
             * status : 0
             * id : 27275308
             * author : 2233155495
             */

            /**
             * 原消息的内容
             */
            private String content;
            /**
             * 消息状态，0为正常，非0为已被删除
             */
            private int status;
            /**
             * 被回复者的唯一标识符
             */
            private int id;
            /**
             * 被回复者
             */
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            @Override
            public String toString() {
                return "ReplyToBean{" +
                        "content='" + content + '\'' +
                        ", status=" + status +
                        ", id=" + id +
                        ", author='" + author + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "DailyCommentBean{" +
                "comments=" + comments +
                '}';
    }
}
