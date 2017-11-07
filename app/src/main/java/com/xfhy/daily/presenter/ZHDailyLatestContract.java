package com.xfhy.daily.presenter;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.network.entity.zhihu.PastNewsBean;

/**
 * author feiyang
 * create at 2017/9/30 14:54
 * description：知乎 日报 板块的 规范
 */
public interface ZHDailyLatestContract {
    /**
     * 知乎 日报 板块的 presenter
     */
    interface Presenter extends BasePresenter<View> {

        /**
         * 刷新数据
         */
        void onRefresh();

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
         */
        void saveDailyDataToDB(@NonNull LatestDailyListBean latestDailyListBean);

        /**
         * 加载更多数据
         * @param pastDays 这里传入RecyclerView的分组个数,代表离今天过去了多少天  至少过去了1天
         */
        void loadMoreData(@IntRange(from = 1) int pastDays);
    }

    /**
     * 知乎 日报 板块的 View接口
     */
    interface View extends BaseView {
        /**
         * 获取最新的数据成功  传入最新的数据  显示
         *
         * @param latestDailyListBean 最新的数据
         */
        void showLatestData(LatestDailyListBean latestDailyListBean);

        /**
         * 显示加载成功的往期数据(当用户上拉列表时,如果把今天的日报刷完了,那么再往下拉时需要加载昨天的数据,
         * 依次类推,不断加载前一天的数据,然后加到RecyclerView中)
         *
         * @param groupTitle   组头标题(每一天的日报是一个RecyclerView分组)
         * @param pastNewsBean 需要加到RecyclerView末尾的数据
         */
        void loadMoreSuccess(String groupTitle, PastNewsBean pastNewsBean);

        /**
         * 加载更多失败
         */
        void loadMoreFailed();

    }
}
