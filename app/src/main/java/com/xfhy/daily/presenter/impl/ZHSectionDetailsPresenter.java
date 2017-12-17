package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;
import com.xfhy.daily.presenter.ZHSectionDetailsContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/26 12:49
 *         description：知乎专栏详情列表presenter
 */
public class ZHSectionDetailsPresenter extends RxPresenter<ZHSectionDetailsContract.View>
        implements ZHSectionDetailsContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<ColumnDailyDetailsBean.StoriesBean> mData;
    /**
     * 当前view所处的状态
     */
    private int mStep;

    public ZHSectionDetailsPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet(String sectionId) {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        addSubscribe(mZHDataManager.getColumnDailyDetailsList(sectionId)
                .map(new Function<ColumnDailyDetailsBean, List<ColumnDailyDetailsBean
                        .StoriesBean>>() {

                    @Override
                    public List<ColumnDailyDetailsBean.StoriesBean> apply
                            (ColumnDailyDetailsBean columnDailyDetailsBean) throws Exception {
                        return columnDailyDetailsBean.getStories();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<List<ColumnDailyDetailsBean.StoriesBean>>(getView()) {
                    @Override
                    public void onNext(List<ColumnDailyDetailsBean.StoriesBean> storiesBeans) {
                        if (storiesBeans != null) {
                            getView().loadSuccess(storiesBeans);
                            mData = storiesBeans;
                        } else {
                            getView().showErrorMsg("专栏列表详情加载失败....");
                            getView().showEmptyView();
                        }
                        mStep = Constants.STATE_NORMAL;
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mStep = Constants.STATE_ERROR;
                    }
                })
        );
    }

    @Override
    public void refreshData(String sectionId) {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet(sectionId);
    }

    @Override
    public List<ColumnDailyDetailsBean.StoriesBean> getData() {
        return mData;
    }

    @Override
    public int getDailyId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getId();
        }
        return 0;
    }
}
