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
         * 从网络请求日报评论
         *
         * @param id 日报id
         */
        void reqLongComFromNet(String id);

        /**
         * 获取所有评论列表
         */
        List<DailyCommentBean.CommentsBean> getComments();

    }

    interface View extends BaseView {
        /**
         * 加载评论成功
         *
         * @param commentsBean 服务器返回的数据
         */
        void loadCommentSuccess(List<DailyCommentBean.CommentsBean> commentsBean);

        /**
         * 加载评论失败
         *
         * @param errorMsg 错误信息
         */
        void loadCommentError(String errorMsg);
    }

}
