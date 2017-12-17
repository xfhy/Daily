package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.ThemeDailyDetailsBean;
import com.xfhy.daily.presenter.ZHThemeDetailsContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/19 14:08
 *         description：知乎主题详情页presenter
 */
public class ZHThemeDetailsPresenter extends RxPresenter<ZHThemeDetailsContract.View>
        implements ZHThemeDetailsContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private ThemeDailyDetailsBean mData;
    /**
     * 当前所处的状态
     */
    private int mStep;

    public ZHThemeDetailsPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet(String number) {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        addSubscribe(mZHDataManager.getThemeDailyDetails(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CommonSubscriber<ThemeDailyDetailsBean>(getView()) {
                    @Override
                    public void onNext(ThemeDailyDetailsBean themeDailyDetailsBean) {
                        if (themeDailyDetailsBean != null) {
                            getView().loadSuccess(themeDailyDetailsBean);
                            mData = themeDailyDetailsBean;
                        } else {
                            getView().showErrorMsg("主题列表加载失败....");
                            getView().showEmptyView();
                        }
                        mStep = Constants.STATE_NORMAL;
                    }
                })
        );
    }

    @Override
    public ThemeDailyDetailsBean getData() {
        return mData;
    }

    @Override
    public void refreshData(String number) {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet(number);
    }
}
