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
import com.xfhy.daily.model.bean.TopicDailyListBean;
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
public class ZHThemePresenter extends RxPresenter<ZHThemeContract.View> implements
        ZHThemeContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<TopicDailyListBean.OthersBean> mData;
    private int mStep;

    public ZHThemePresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected()) {

            addSubscribe(
                    mZHDataManager.getTopicDailyList()
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
                            .doOnNext(new Consumer<List<TopicDailyListBean.OthersBean>>() {
                                @Override
                                public void accept(List<TopicDailyListBean.OthersBean> othersBeans) throws Exception {
                                    saveDataToDB(othersBeans);
                                }
                            })
                            .subscribeWith(new CommonSubscriber<List<TopicDailyListBean.OthersBean>>(getView()) {
                                @Override
                                public void onNext(List<TopicDailyListBean.OthersBean> othersBeans) {
                                    if (othersBeans != null) {
                                        getView().loadSuccess(othersBeans);
                                        mData = othersBeans;
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

        addSubscribe(
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
                            e.onError(new Exception(StringUtils.getStringByResId(BaseApplication.getApplication(),
                                    R.string
                                            .devices_offline)));
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<CacheBean>(getView(), "网络不给力~╭(╯^╰)╮") {
                            @Override
                            public void onNext(CacheBean cacheBean) {
                                //解析json数据
                                mData = JSON.parseArray(cacheBean.getJson(), TopicDailyListBean.OthersBean.class);
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
                                super.onError(t);
                                getView().showOffline();
                                mStep = Constants.STATE_ERROR;
                            }
                        })
        );
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
