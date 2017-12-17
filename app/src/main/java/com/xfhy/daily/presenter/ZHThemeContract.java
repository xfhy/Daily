package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.model.bean.TopicDailyListBean;

import java.util.List;

/**
 * @author xfhy
 * @create at 2017/11/18 23:08
 * description：知乎主题fragment 规范
 */
public interface ZHThemeContract {

    interface Presenter extends BasePresenter<View> {

        /**
         * 从网络请求主题列表
         */
        void reqDataFromNet();

        /**
         * 获取主题列表数据
         */
        List<TopicDailyListBean.OthersBean> getData();

        /**
         * 刷新数据
         */
        void refreshData();

    }

    interface View extends BaseView {
        /**
         * 请求主题列表数据成功
         */
        void loadSuccess(List<TopicDailyListBean.OthersBean> othersBeans);
    }
}
