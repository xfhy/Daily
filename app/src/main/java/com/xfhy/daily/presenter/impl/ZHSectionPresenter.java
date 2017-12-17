package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.ColumnDailyBean;
import com.xfhy.daily.presenter.ZHSectionContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author feiyang
 *         time create at 2017/11/22 14:24
 *         description
 */
public class ZHSectionPresenter extends RxPresenter<ZHSectionContract.View> implements
        ZHSectionContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<ColumnDailyBean.DataBean> mData;
    /**
     * 当前view所处的状态
     */
    private int mStep;

    public ZHSectionPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        addSubscribe(
                mZHDataManager.getColumnDailyList()
                        .map(new Function<ColumnDailyBean, List<ColumnDailyBean.DataBean>>() {
                            @Override
                            public List<ColumnDailyBean.DataBean> apply(ColumnDailyBean columnDailyBean)
                                    throws Exception {
                                return columnDailyBean.getData();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<List<ColumnDailyBean.DataBean>>(getView()) {
                            @Override
                            public void onNext(List<ColumnDailyBean.DataBean> dataBeans) {
                                if (dataBeans != null) {
                                    getView().loadSuccess(dataBeans);
                                    mData = dataBeans;
                                } else {
                                    getView().showErrorMsg("专栏列表加载失败....");
                                    getView().showEmptyView();
                                }
                                mStep = Constants.STATE_NORMAL;
                            }
                        })
        );
    }

    @Override
    public void refreshData() {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet();
    }

    @Override
    public List<ColumnDailyBean.DataBean> getData() {
        return mData;
    }

    @Override
    public int getSectionId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getId();
        }
        return 0;
    }

    @Override
    public String getSectionTitle(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getName();
        }
        return "";
    }
}
