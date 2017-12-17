package com.xfhy.daily.presenter;

import com.xfhy.androidbasiclibs.basekit.presenter.BasePresenter;
import com.xfhy.androidbasiclibs.basekit.view.BaseView;
import com.xfhy.daily.model.bean.ColumnDailyBean;

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
         * 获取专栏列表数据
         */
        List<ColumnDailyBean.DataBean> getData();

        /**
         * 刷新数据
         */
        void refreshData();

        /**
         * 根据RecyclerView的position获取专题id
         */
        int getSectionId(int position);

        /**
         * 根据RecyclerView的position获取专栏标题
         */
        String getSectionTitle(int position);

    }

    interface View extends BaseView {
        /**
         * 请求专栏列表数据成功
         */
        void loadSuccess(List<ColumnDailyBean.DataBean> dataBeans);
    }

}
