package com.xfhy.daily.presenter.impl;

import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;

/**
 * author feiyang
 * create at 2017/9/30 16:52
 * description：知乎最新日报的presenter
 */
public class ZhihuDailyLatestPresenter extends AbstractPresenter<ZhihuDailyLatestContract.View>
        implements ZhihuDailyLatestContract.Presenter {

    @Override
    public void reqDailyDataFromNet() {

    }

    @Override
    public void reqDailyDataFromDB() {

    }

    @Override
    public boolean saveDailyDataToDB() {
        return false;
    }

    @Override
    public void refreshDataFromNet() {

    }
}
