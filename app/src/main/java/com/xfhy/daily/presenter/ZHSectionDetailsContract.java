package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;

import java.util.List;

/**
 * @author xfhy
 *         create at 2017/11/26 12:36
 *         description：知乎通用list的规范
 */
public interface ZHSectionDetailsContract {

    interface Presenter extends BasePresenter<View> {

        /**
         * 从网络请求主题列表
         */
        void reqDataFromNet(String sectionId);

        /**
         * 获取专栏详情列表数据
         */
        List<ColumnDailyDetailsBean.StoriesBean> getData();

        /**
         * 刷新数据
         */
        void refreshData(String sectionId);

        /**
         * 获取点击item位置的文章id
         */
        int getDailyId(int position);

    }

    interface View extends BaseView {
        /**
         * 请求专栏列表数据成功
         */
        void loadSuccess(List<ColumnDailyDetailsBean.StoriesBean> dataBeans);

        /**
         * 获取当前专栏id
         */
        String getSectionId();

    }

}
