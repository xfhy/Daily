package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;

/**
 * author feiyang
 * create at 2017/9/30 14:54
 * description：知乎 日报 板块的 规范
 */
public interface ZhihuDailyLatestContract {
    /**
     * 知乎 日报 板块的 presenter
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 从网络请求日报数据
         */
        void reqDailyDataFromNet();

        /**
         * 从数据库获取日报数据(在断网情况下使用)
         */
        void reqDailyDataFromDB();

        /**
         * 保存日报数据到数据库中
         *
         * @return 保存是否成功  true:成功   false:失败
         */
        boolean saveDailyDataToDB();

        /**
         * 刷新数据(从网络)
         */
        void refreshDataFromNet();
    }

    /**
     * 知乎 日报 板块的 View接口
     */
    interface View extends BaseView {
        /**
         * 刷新成功  传入最新的数据  显示
         *
         * @param latestDailyListBean 最新的数据
         */
        void refreshSuccess(LatestDailyListBean latestDailyListBean);

        /**
         * 刷新失败
         *
         * @param error 刷新失败原因
         */
        void refreshError(String error);
    }
}
