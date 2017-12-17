package com.xfhy.daily.presenter.impl;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.BaseApplication;
import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.db.CacheBean;
import com.xfhy.androidbasiclibs.db.CacheDao;
import com.xfhy.androidbasiclibs.db.DBConstants;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.daily.R;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.ThemeDailyDetailsBean;
import com.xfhy.daily.presenter.ZHThemeDetailsContract;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
        if (DevicesUtils.hasNetworkConnected()) {

            addSubscribe(mZHDataManager.getThemeDailyDetails(number)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<ThemeDailyDetailsBean>() {
                        @Override
                        public void accept(ThemeDailyDetailsBean themeDailyDetailsBean) throws Exception {
                            saveDataToDB(themeDailyDetailsBean);
                        }
                    })
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
        } else {
            reqDataFromDB();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromDB() {
        addSubscribe(
                Flowable.create(new FlowableOnSubscribe<CacheBean>() {
                    @Override
                    public void subscribe(FlowableEmitter<CacheBean> e) throws
                            Exception {
                        List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(DBConstants
                                .ZHIHU_THEME_LIST_DETAILS_KEY + getView()
                                .getmThemeId());
                        if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                            CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                            e.onNext(cacheBean);
                        } else {
                            e.onError(new Exception(StringUtils.getStringByResId(BaseApplication.getApplication(),
                                    R.string
                                            .devices_offline)));
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<CacheBean>(getView()) {
                            @Override
                            public void onNext(CacheBean cacheBean) {
                                //解析json数据
                                mData = JSON.parseObject(cacheBean.getJson(), ThemeDailyDetailsBean.class);
                                //判断数据是否为空
                                if (mData != null) {
                                    getView().showContent();

                                    //刷新界面
                                    getView().loadSuccess(mData);
                                    mStep = Constants.STATE_NORMAL;
                                } else {
                                    //无数据   显示空布局
                                    getView().showEmptyView();
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                getView().showOffline();
                                mStep = Constants.STATE_ERROR;
                            }
                        })
        );
    }

    @Override
    public void saveDataToDB(ThemeDailyDetailsBean themeDailyDetailsBean) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_THEME_LIST_DETAILS_KEY + getView().getmThemeId(), JSON
                .toJSONString(themeDailyDetailsBean));
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
