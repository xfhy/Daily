package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.model.bean.ThemeDailyDetailsBean;

/**
 * @author xfhy
 *         create at 2017/11/19 13:56
 *         description：知乎主题详情页规范
 */
public interface ZHThemeDetailsContract {

    interface Presenter extends BasePresenter<View> {
        /**
         * 从网络请求主题列表
         */
        void reqDataFromNet(String number);

        /**
         * 从数据库请求主题列表
         */
        void reqDataFromDB();

        /**
         * 保存主题列表到数据库
         */
        void saveDataToDB(ThemeDailyDetailsBean themeDailyDetailsBean);

        /**
         * 获取主题列表数据
         */
        ThemeDailyDetailsBean getData();

        /**
         * 刷新数据
         * @param number 主题编号
         */
        void refreshData(String number);
    }

    interface View extends BaseView {
        /**
         * 请求主题列表数据成功
         */
        void loadSuccess(ThemeDailyDetailsBean themeDailyDetailsBean);

        /**
         * 获取当前主题id
         */
        int getmThemeId();
    }
}
