package com.xfhy.daily.presenter.impl;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.IntRange;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.db.CacheBean;
import com.xfhy.androidbasiclibs.common.db.CacheDao;
import com.xfhy.androidbasiclibs.common.db.DBConstants;
import com.xfhy.androidbasiclibs.common.util.DateUtils;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.LatestDailyListBean;
import com.xfhy.daily.network.entity.zhihu.PastNewsBean;
import com.xfhy.daily.presenter.ZhihuDailyLatestContract;
import com.xfhy.daily.ui.fragment.zhihu.ZhihuLatestDailyFragment;

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
public class ZhihuDailyLatestPresenter extends AbstractPresenter<ZhihuDailyLatestContract.View>
        implements ZhihuDailyLatestContract.Presenter {
    /**
     * Retrofit帮助类
     */
    private RetrofitHelper mRetrofitHelper;
    /**
     * 该presenter的fragment
     */
    private ZhihuLatestDailyFragment mFragment;

    public ZhihuDailyLatestPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @Override
    public void setView(ZhihuDailyLatestContract.View view) {
        super.setView(view);
        mFragment = (ZhihuLatestDailyFragment) view;
    }

    @Override
    public void reqDailyDataFromNet() {
        //判断当前是否有网络   再决定走网络还是数据库缓存
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            //从网络获取最新数据
            mRetrofitHelper
                    .getZhiHuApi().getLatestDailyList()
                    .compose(mFragment.<LatestDailyListBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LatestDailyListBean>() {
                        @Override
                        public void accept(LatestDailyListBean s) throws Exception {
                            LogUtils.e(s.toString());
                            //显示最新数据
                            view.showLatestData(s);
                            //保存网络数据到数据库
                            saveDailyDataToDB(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            view.showErrorMsg("sorry,请求网络数据失败~");
                            view.showEmptyView();
                            LogUtils.e("从网络加载最新数据失败,原因:" + throwable.getLocalizedMessage());
                        }
                    });
        } else {
            LogUtils.e("目前没有网络,接下来从缓存获取知乎首页数据");
            reqDailyDataFromDB();
        }

    }

    @Override
    public void reqDailyDataFromDB() {
        //把访问数据库的操作放到子线程中
        Flowable.create(new FlowableOnSubscribe<CacheBean>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<CacheBean> e) throws Exception {
                List<CacheBean> cacheBeen = CacheDao.queryCacheByKey(DBConstants
                        .ZHIHU_LATEST_DAILY_KEY);
                if (cacheBeen != null && cacheBeen.size() > 0 && cacheBeen.get(0) != null) {
                    CacheBean cacheBean = cacheBeen.get(0);
                    e.onNext(cacheBean);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .compose(mFragment.<CacheBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheBean>() {
                    @Override
                    public void accept(CacheBean cacheBean) throws Exception {
                        LogUtils.e(cacheBean.toString());
                        //解析json数据
                        LatestDailyListBean latestDailyListBean = JSON.parseObject(cacheBean
                                .getJson(), LatestDailyListBean.class);
                        //判断数据是否为空
                        if (latestDailyListBean != null) {
                            //刷新界面
                            view.showLatestData(latestDailyListBean);
                        } else {
                            //无数据   显示空布局
                            view.showEmptyView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.getLocalizedMessage());
                        view.showErrorMsg("请求网络数据失败~");
                    }
                });
    }

    @Override
    public void saveDailyDataToDB(@android.support.annotation.NonNull LatestDailyListBean
                                          latestDailyListBean) {
        //缓存数据到数据库  用RxJava的IO线程去操作
        Flowable.just(latestDailyListBean)
                .compose(mFragment.<LatestDailyListBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LatestDailyListBean>() {
                    @Override
                    public void accept(LatestDailyListBean latestDailyListBean) throws Exception {
                        //缓存到数据库
                        CacheBean cacheBean = new CacheBean();
                        cacheBean.setKey(DBConstants.ZHIHU_LATEST_DAILY_KEY);
                        cacheBean.setJson(JSON.toJSONString(latestDailyListBean));
                        CacheDao.insertCache(cacheBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //缓存到数据库失败
                        LogUtils.e("知乎首页数据缓存到数据库失败,原因:" + throwable.getLocalizedMessage());
                    }
                });


    }

    @Override
    public void loadMoreData(@IntRange(from = 1) int pastDays) {
        //TODO 需要写一个Util方法  计算今天的日期-x天 表示的日期   格式:yyyyMMdd
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
        Date pastDate = DateUtils.getPastDate(new Date(SystemClock.currentThreadTimeMillis()),
                pastDays);
        final String groupTitle = DateUtils.getDateFormatText(pastDate, "MM月dd日 E");
        mRetrofitHelper.getZhiHuApi()
                .getPastNews(DateUtils.getDateFormatText(pastDate, "yyyyMMdd"))
                .compose(mFragment.<PastNewsBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PastNewsBean>() {
                    @Override
                    public void accept(PastNewsBean pastNewsBean) throws Exception {
                        if (pastNewsBean != null) {
                            view.showMoreData(groupTitle, pastNewsBean);
                        } else {
                            view.showErrorMsg("无更多数据~");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showErrorMsg("加载更多数据失败~请稍后重试");
                        LogUtils.e(throwable.getLocalizedMessage());
                    }
                });

    }
}
