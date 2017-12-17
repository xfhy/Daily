package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.HotDailyBean;
import com.xfhy.daily.presenter.ZHHotContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/26 16:11
 *         description：知乎热门文章Presenter
 */
public class ZHHotPresenter extends RxPresenter<ZHHotContract.View> implements
        ZHHotContract.Presenter {
    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<HotDailyBean.RecentBean> mData;
    private int mStep;

    public ZHHotPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        addSubscribe(mZHDataManager.getHotDailyList()
                .map(new Function<HotDailyBean, List<HotDailyBean.RecentBean>>() {
                    @Override
                    public List<HotDailyBean.RecentBean> apply(HotDailyBean hotDailyBean)
                            throws Exception {
                        return hotDailyBean.getRecent();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<List<HotDailyBean.RecentBean>>(getView()) {
                    @Override
                    public void onNext(List<HotDailyBean.RecentBean> recentBeans) {
                        if (recentBeans != null) {
                            getView().loadSuccess(recentBeans);
                            mData = recentBeans;
                        } else {
                            getView().showErrorMsg("热门列表加载失败....");
                            getView().showEmptyView();
                        }
                        mStep = Constants.STATE_NORMAL;
                    }
                })
        );
    }

    @Override
    public List<HotDailyBean.RecentBean> getData() {
        return mData;
    }

    @Override
    public void refreshData() {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet();
    }

    @Override
    public int getDailyId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getNewsId();
        }
        return 0;
    }
}
