package com.xfhy.daily.presenter.impl;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.xfhy.androidbasiclibs.basekit.presenter.RxPresenter;
import com.xfhy.androidbasiclibs.common.CommonSubscriber;
import com.xfhy.androidbasiclibs.util.Constants;
import com.xfhy.androidbasiclibs.util.DateUtils;
import com.xfhy.daily.model.ZHDataManager;
import com.xfhy.daily.model.bean.LatestDailyListBean;
import com.xfhy.daily.model.bean.PastNewsBean;
import com.xfhy.daily.presenter.ZHDailyLatestContract;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

        addSubscribe(mZHDataManager.getLatestDailyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

                            @Override
                            public void onError(Throwable t) {
                                super.onError(t);
                                getView().loadMoreFailed();
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
