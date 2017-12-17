package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.model.bean.HotDailyBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/26 16:04
 *         description：知乎热门文章 该API可能在不久的将来被干掉,最新的知乎日报已经没有采用此API了
 */
public interface ZHHotContract {

    interface Presenter extends BasePresenter<View> {

        /**
         * 从网络请求主题列表
         */
        void reqDataFromNet();

        /**
         * 获取专栏详情列表数据
         */
        List<HotDailyBean.RecentBean> getData();

        /**
         * 刷新数据
         */
        void refreshData();

        /**
         * 获取点击item位置的文章id
         */
        int getDailyId(int position);

    }

    interface View extends BaseView {
        /**
         * 请求专栏列表数据成功
         */
        void loadSuccess(List<HotDailyBean.RecentBean> dataBeans);
    }


}
