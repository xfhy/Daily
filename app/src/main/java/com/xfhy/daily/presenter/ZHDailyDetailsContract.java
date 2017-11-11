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
         *
         * @param id 日报id
         */
        void reqDailyContentFromNet(String id);

        /**
         * 请求额外信息
         *
         * @param id 日报id
         */
        void reqDailyExtraInfoFromNet(String id);

        /**
         * 收藏当前文章
         *
         * @param id 日报id
         */
        void collectArticle(String id);

        /**
         * 判断该文章是否已经被收藏
         *
         * @param id id
         * @return true:已经被收藏  false:未被收藏
         */
        boolean isCollected(String id);

        /**
         * 获取数据
         *
         * @return
         */
        DailyContentBean getData();

        /**
         * 判断是否设置了无图模式
         *
         * @return true:设置为无图模式了的  false:则相反
         */
        boolean getNoImageState();

        /**
         * 判断是否设置了自动缓存
         *
         * @return true:已设置自动缓存,未设置自动缓存
         */
        boolean getAutoCacheState();
    }

    interface View extends BaseView {

        /**
         * 用户按下返回键 处理
         */
        void goToBack();

        /**
         * 设置点赞数
         *
         * @param likeCount    点赞数
         * @param commentCount 评论数
         */
        void setExtraInfo(int likeCount, int commentCount);

        /**
         * 加载成功
         *
         * @param dailyContentBean 服务器返回的数据
         */
        void loadSuccess(DailyContentBean dailyContentBean);

        /**
         * 加载失败
         */
        void loadError();

        /**
         * 控制收藏按钮的选择状态
         *
         * @param state true:被选择了(收藏了的)  false:未被选择
         */
        void setCollectBtnSelState(boolean state);

    }

}
