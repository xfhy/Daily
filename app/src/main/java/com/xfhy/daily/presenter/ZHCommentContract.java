package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.network.entity.zhihu.DailyCommentBean;

import java.util.List;

/**
 * author feiyang
 * time create at 2017/11/16 14:31
 * description 知乎评论 规范
 */
public interface ZHCommentContract {

    interface Presenter extends BasePresenter<View> {
        /**
         * 从网络请求日报长评论
         *
         * @param id 日报id
         */
        void reqLongComFromNet(String id);

        /**
         * 从网络请求日报短评论
         *
         * @param id 日报id
         */
        void reqShortComFromNet(String id);

        /**
         * 获取长评论列表
         */
        List<DailyCommentBean.CommentsBean> getLongComments();

        /**
         * 获取短评论列表
         */
        List<DailyCommentBean.CommentsBean> getShortComments();

    }

    interface View extends BaseView {
        /**
         * 加载长评论成功
         *
         * @param commentsBean 服务器返回的数据
         */
        void loadLongComSuccess(List<DailyCommentBean.CommentsBean> commentsBean);

        /**
         * 加载长评论失败
         *
         * @param errorMsg 错误信息
         */
        void loadLongComError(String errorMsg);

        /**
         * 加载短评论成功
         *
         * @param commentsBean 服务器返回的数据
         */
        void loadShortComSuccess(List<DailyCommentBean.CommentsBean> commentsBean);

        /**
         * 加载短评论失败
         *
         * @param errorMsg 错误信息
         */
        void loadShortComError(String errorMsg);

    }

}
