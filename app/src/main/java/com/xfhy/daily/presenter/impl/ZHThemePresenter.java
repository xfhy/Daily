package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.BaseApplication;
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
import com.xfhy.daily.network.entity.zhihu.TopicDailyListBean;
import com.xfhy.daily.presenter.ZHThemeContract;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/18 23:32
 *         description：知乎主题presenter
 */
public class ZHThemePresenter extends AbstractPresenter<ZHThemeContract.View> implements
        ZHThemeContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private List<TopicDailyListBean.OthersBean> mData;
    private int mStep;

    public ZHThemePresenter() {
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected()) {
            mRetrofitHelper.getZhiHuApi().getTopicDailyList()
                    .compose(getView().bindLifecycle())
                    .map(new Function<TopicDailyListBean, List<TopicDailyListBean.OthersBean>>() {
                        @Override
                        public List<TopicDailyListBean.OthersBean> apply(TopicDailyListBean
                                                                                 topicDailyListBean)
                                throws Exception {
                            return topicDailyListBean.getOthers();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<TopicDailyListBean.OthersBean>>() {
                        @Override
                        public void accept(List<TopicDailyListBean.OthersBean> othersBeans)
                                throws Exception {
                            if (othersBeans != null) {
                                getView().loadSuccess(othersBeans);
                                mData = othersBeans;
                                saveDataToDB(othersBeans);
                            } else {
                                getView().showErrorMsg("主题列表加载失败....");
                                getView().showEmptyView();
                            }
                            mStep = Constants.STATE_NORMAL;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("主题列表加载失败 错误:" + throwable.getLocalizedMessage());
                            getView().showEmptyView();
                        }
                    });
        } else {
            reqDataFromDB();
        }
    }

    @Override
    public void refreshData() {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromDB() {
        Flowable.create(new FlowableOnSubscribe<CacheBean>() {
            @Override
            public void subscribe(FlowableEmitter<CacheBean> e) throws
                    Exception {
                List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(DBConstants
                        .ZHIHU_THEME_LIST_KEY);
                if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                    CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                    e.onNext(cacheBean);
                } else {
                    e.onError(new Exception(StringUtils.getStringByResId(BaseApplication.getApplication(), R.string
                            .devices_offline)));
                }
            }
        }, BackpressureStrategy.BUFFER)
                .compose(getView().bindLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheBean>() {
                    @Override
                    public void accept(CacheBean cacheBean) throws
                            Exception {
                        //解析json数据
                        mData = JSON.parseArray(cacheBean.getJson(), TopicDailyListBean.OthersBean
                                .class);
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        String localizedMessage = throwable.getLocalizedMessage();
                        LogUtils.e(localizedMessage);

                        if (StringUtils.getStringByResId(BaseApplication.getApplication(), R.string.devices_offline)
                                .equals(localizedMessage)) {
                            getView().showOffline();
                            mStep = Constants.STATE_ERROR;
                        } else {
                            getView().showErrorMsg(localizedMessage);
                        }
                    }
                });
    }

    @Override
    public void saveDataToDB(List<TopicDailyListBean.OthersBean> othersBeans) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_THEME_LIST_KEY, JSON.toJSONString(othersBeans));
    }

    @Override
    public List<TopicDailyListBean.OthersBean> getData() {
        return mData;
    }
}
