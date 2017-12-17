package com.xfhy.daily.presenter.impl;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.BaseApplication;
import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.db.CacheBean;
import com.xfhy.androidbasiclibs.db.CacheDao;
import com.xfhy.androidbasiclibs.db.DBConstants;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.androidbasiclibs.util.DateUtils;
import com.xfhy.androidbasiclibs.util.DevicesUtils;
import com.xfhy.androidbasiclibs.util.LogUtils;
import com.xfhy.androidbasiclibs.util.StringUtils;
import com.xfhy.daily.R;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.LatestDailyListBean;
import com.xfhy.daily.model.bean.PastNewsBean;
import com.xfhy.daily.presenter.ZHDailyLatestContract;

import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author feiyang
 * create at 2017/9/30 16:52
 * description：知乎最新日报的presenter
 */
public class ZHDailyLatestPresenter extends RxPresenter<ZHDailyLatestContract.View>
        implements ZHDailyLatestContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;

    /**
     * 当前界面的显示的数据
     */
    private LatestDailyListBean mData = null;
    /**
     * 当前所进行到的步骤
     */
    private int mStep;

    public ZHDailyLatestPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @Override
    public void onRefresh() {
        if (mStep == Constants.STATE_LOAD_MORE || mStep == Constants.STATE_LOADING) {
            return;
        }
        mStep = Constants.STATE_REFRESH;
        reqDailyDataFromNet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDailyDataFromNet() {
        mStep = Constants.STATE_LOADING;
        getView().onLoading();

        //判断当前是否有网络   再决定走网络还是数据库缓存
        if (DevicesUtils.hasNetworkConnected()) {
            addSubscribe(mZHDataManager.getLatestDailyList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<LatestDailyListBean>() {
                        @Override
                        public void accept(LatestDailyListBean latestDailyListBean) throws Exception {
                            LogUtils.e("保存网络数据到数据库");
                            //保存网络数据到数据库
                            saveDailyDataToDB(latestDailyListBean);
                        }
                    })
                    .subscribeWith(new CommonSubscriber<LatestDailyListBean>(getView()) {
                        @Override
                        public void onNext(LatestDailyListBean latestDailyListBean) {
                            mData = latestDailyListBean;
                            //显示最新数据
                            getView().showLatestData(mData);

                            LatestDailyListBean.StoriesBean header = new LatestDailyListBean
                                    .StoriesBean(true);
                            header.header = "今日热闻";
                            mData.getStories().add(0, header);

                            //显示内容区域
                            getView().showContent();
                            mStep = Constants.STATE_NORMAL;
                        }
                    })
            );
        } else {
            LogUtils.e("目前没有网络,接下来从缓存获取知乎首页数据");
            reqDailyDataFromDB();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDailyDataFromDB() {
        //把访问数据库的操作放到子线程中
        addSubscribe(Flowable.create(new FlowableOnSubscribe<CacheBean>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<CacheBean> e) throws Exception {
                List<CacheBean> cacheBeen = CacheDao.queryCacheByKey(DBConstants
                        .ZHIHU_LATEST_DAILY_KEY);
                if (cacheBeen != null && cacheBeen.size() > 0 && cacheBeen.get(0) != null) {
                    CacheBean cacheBean = cacheBeen.get(0);
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
                        mData = JSON.parseObject(cacheBean.getJson(), LatestDailyListBean.class);
                        //判断数据是否为空
                        if (mData != null) {
                            getView().showContent();

                            //刷新界面
                            getView().showLatestData(mData);
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
                }));

    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveDailyDataToDB(@android.support.annotation.NonNull final LatestDailyListBean
                                          latestDailyListBean) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_LATEST_DAILY_KEY, JSON.toJSONString
                (latestDailyListBean));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadMoreData(@IntRange(from = 1) int pastDays) {
        //需要写一个Util方法  计算今天的日期-x天 表示的日期   格式:yyyyMMdd
        //再计算该日期需要显示的文字  比如: 20171010 : 10月10日 星期二
        //-2天显示:10月09日 星期一
        /*
         * RecyclerView上拉时需要加载更多的数据
         * pastDays 这里传入RecyclerView的分组个数,代表离今天过去了多少天  至少过去了1天
         * 比如:今天是2017年10月11日,则显示今日热闻
         * 那么-1天是20171010,显示为:10月10日 星期二
         * -2是20171009,显示为:10月09日 星期一
         *
         * 1.根据格式化的日期(eg:20171010)去加载往期日报
         * 2.显示到view上
         */

        if (mStep == Constants.STATE_REFRESH) {
            return;
        }

        Date pastDate = DateUtils.getPastDate(new Date(System.currentTimeMillis()),
                pastDays);
        final String groupTitle = DateUtils.getDateFormatText(pastDate, "MM月dd日 E");
        addSubscribe(
                mZHDataManager.getPastNews(DateUtils.getDateFormatText(pastDate, "yyyyMMdd"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<PastNewsBean>(getView()) {
                            @Override
                            public void onNext(PastNewsBean pastNewsBean) {
                                if (pastNewsBean != null) {

                                    LatestDailyListBean.StoriesBean header = new LatestDailyListBean
                                            .StoriesBean(true);
                                    header.header = groupTitle;
                                    mData.getStories().add(header);

                                    getView().loadMoreSuccess(groupTitle, pastNewsBean);
                                } else {
                                    getView().showErrorMsg("无更多数据~");
                                    getView().loadMoreFailed();
                                }
                                mStep = Constants.STATE_NORMAL;
                            }
                        })
        );
    }

    @Override
    @Nullable
    public LatestDailyListBean getData() {
        return mData;
    }

    @Override
    public int getClickItemId(int position) {
        if (mData != null) {
            List<LatestDailyListBean.StoriesBean> stories = mData.getStories();
            if (stories != null && position < stories.size()) {
                return stories.get(position).getId();
            }
        }
        return 0;
    }

    @Override
    public int getHeaderClickItemId(int position) {
        if (mData != null) {
            List<LatestDailyListBean.TopStoriesBean> stories = mData.getTopStories();
            if (stories != null && position < stories.size()) {
                return stories.get(position).getId();
            }
        }
        return 0;
    }
}
