package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.db.CacheBean;
import com.xfhy.androidbasiclibs.db.CacheDao;
import com.xfhy.androidbasiclibs.db.DBConstants;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.LogUtils;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.daily.NewsApplication;
import com.xfhy.daily.R;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.ThemeDailyDetailsBean;
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
public class ZHThemeDetailsPresenter extends AbstractPresenter<ZHThemeDetailsContract.View>
        implements ZHThemeDetailsContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private ThemeDailyDetailsBean mData;
    /**
     * 当前所处的状态
     */
    private int mStep;

    public ZHThemeDetailsPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet(String number) {
        view.onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getThemeDailyDetails(number)
                    .compose(view.bindLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ThemeDailyDetailsBean>() {
                        @Override
                        public void accept(ThemeDailyDetailsBean themeDailyDetailsBean)
                                throws Exception {
                            if (themeDailyDetailsBean != null) {
                                view.loadSuccess(themeDailyDetailsBean);
                                mData = themeDailyDetailsBean;
                                saveDataToDB(themeDailyDetailsBean);
                                LogUtils.e(themeDailyDetailsBean.toString());
                            } else {
                                view.showErrorMsg("主题列表加载失败....");
                                view.showEmptyView();
                            }
                            mStep = Constants.STATE_NORMAL;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("主题列表加载失败 错误:" + throwable.getLocalizedMessage());
                            view.showEmptyView();
                        }
                    });
        } else {
            reqDataFromDB();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromDB() {
        Flowable.create(new FlowableOnSubscribe<CacheBean>() {
            @Override
            public void subscribe(FlowableEmitter<CacheBean> e) throws
                    Exception {
                List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(NewsApplication
                        .getDaoSession(), DBConstants.ZHIHU_THEME_LIST_DETAILS_KEY + view
                        .getmThemeId());
                if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                    CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                    e.onNext(cacheBean);
                } else {
                    e.onError(new Exception(StringUtils.getStringByResId(mContext, R.string
                            .devices_offline)));
                }
            }
        }, BackpressureStrategy.BUFFER)
                .compose(view.bindLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheBean>() {
                    @Override
                    public void accept(CacheBean cacheBean) throws
                            Exception {
                        //解析json数据
                        mData = JSON.parseObject(cacheBean.getJson(), ThemeDailyDetailsBean.class);
                        //判断数据是否为空
                        if (mData != null) {
                            view.showContent();

                            //刷新界面
                            view.loadSuccess(mData);
                            mStep = Constants.STATE_NORMAL;
                        } else {
                            //无数据   显示空布局
                            view.showEmptyView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        String localizedMessage = throwable.getLocalizedMessage();
                        LogUtils.e(localizedMessage);

                        if (StringUtils.getStringByResId(mContext, R.string.devices_offline)
                                .equals(localizedMessage)) {
                            view.showOffline();
                            mStep = Constants.STATE_ERROR;
                        } else {
                            view.showErrorMsg(localizedMessage);
                        }
                    }
                });
    }

    @Override
    public void saveDataToDB(ThemeDailyDetailsBean themeDailyDetailsBean) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_THEME_LIST_DETAILS_KEY + view.getmThemeId(),
                NewsApplication
                        .getDaoSession(), JSON.toJSONString(themeDailyDetailsBean));
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
