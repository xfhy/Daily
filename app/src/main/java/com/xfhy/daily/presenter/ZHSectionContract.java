package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.network.entity.zhihu.ColumnDailyBean;

import java.util.List;

/**
 * @author feiyang
 *         time create at 2017/11/22 13:56
 *         description 知乎专栏首页规范
 */
public interface ZHSectionContract {

    interface Presenter extends BasePresenter<View> {

        /**
         * 从网络请求主题列表
         */
        void reqDataFromNet();

        /**
         * 从数据库请求主题列表
         */
        void reqDataFromDB();

        /**
         * 保存专栏列表到数据库
         */
        void saveDataToDB(List<ColumnDailyBean.DataBean> dataBeans);

        /**
         * 获取专栏列表数据
         */
        List<ColumnDailyBean.DataBean> getData();

        /**
         * 刷新数据
         */
        void refreshData();

    }

    interface View extends BaseView {
        /**
         * 请求专栏列表数据成功
         */
        void loadSuccess(List<ColumnDailyBean.DataBean> dataBeans);
    }

}
