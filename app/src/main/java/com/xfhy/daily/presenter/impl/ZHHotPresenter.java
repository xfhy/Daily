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
import com.xfhy.daily.model.bean.HotDailyBean;
import com.xfhy.daily.presenter.ZHHotContract;

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
        if (DevicesUtils.hasNetworkConnected()) {

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
                    .doOnNext(new Consumer<List<HotDailyBean.RecentBean>>() {
                        @Override
                        public void accept(List<HotDailyBean.RecentBean> recentBeans) throws Exception {
                            saveDataToDB(recentBeans);
                        }
                    })
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
                        List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(DBConstants.ZHIHU_HOT_LIST_KEY);
                        if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                            CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                            e.onNext(cacheBean);
                        } else {
                            e.onError(new Exception(StringUtils.getStringByResId(BaseApplication
                                    .getApplication(), R.string.devices_offline)));
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<CacheBean>(getView(), "网络不给力~╭(╯^╰)╮") {
                            @Override
                            public void onNext(CacheBean cacheBean) {
                                //解析json数据
                                mData = JSON.parseArray(cacheBean.getJson(), HotDailyBean.RecentBean.class);
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
    public void saveDataToDB(List<HotDailyBean.RecentBean> dataBeans) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_HOT_LIST_KEY,
                JSON.toJSONString(dataBeans));
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
