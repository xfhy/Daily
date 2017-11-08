package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.network.entity.zhihu.DailyContentBean;

/**
 * @author feiyang
 * @time create at 2017/11/7 10:38
 * @description 知乎最新日报 详情规范
 */
public interface ZHDailyDetailsContract {

    interface Presenter extends BasePresenter<View> {
        /**
         * 从网络请求日报详情数据
         * @param id 日报id
         */
        void reqDailyContentFromNet(String id);

        /**
         * 请求额外信息
         * @param id 日报id
         */
        void reqDailyExtraInfoFromNet(String id);

        /**
         * 收藏当前文章
         * @param id 日报id
         */
        void collectArticle(String id);
    }

    interface View extends BaseView {

        /**
         * 用户按下返回键 处理
         */
        void goToBack();

        /**
         * 加载顶部图片
         *
         * @param url 图片地址
         */
        void loadTopPicture(String url);

        /**
         * 顶部图片来源
         *
         * @param source 来源
         */
        void setImageSource(String source);

        /**
         * 设置点赞数
         *
         * @param likeCount    点赞数
         * @param commentCount 评论数
         */
        void setExtraInfo(int likeCount, int commentCount);

        /**
         * 加载url
         * @param url url地址
         */
        void loadUrl(String url);

        /**
         * 加载成功
         * @param dailyContentBean 服务器返回的数据
         */
        void loadSuccess(DailyContentBean dailyContentBean);

        /**
         * 加载失败
         */
        void loadError();

    }

}
