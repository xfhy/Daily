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
import com.xfhy.daily.model.bean.ColumnDailyDetailsBean;
import com.xfhy.daily.presenter.ZHSectionDetailsContract;

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
 *         create at 2017/11/26 12:49
 *         description：知乎专栏详情列表presenter
 */
public class ZHSectionDetailsPresenter extends RxPresenter<ZHSectionDetailsContract.View>
        implements ZHSectionDetailsContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<ColumnDailyDetailsBean.StoriesBean> mData;
    /**
     * 当前view所处的状态
     */
    private int mStep;

    public ZHSectionDetailsPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet(String sectionId) {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected()) {

            addSubscribe(mZHDataManager.getColumnDailyDetailsList(sectionId)
                    .map(new Function<ColumnDailyDetailsBean, List<ColumnDailyDetailsBean
                            .StoriesBean>>() {

                        @Override
                        public List<ColumnDailyDetailsBean.StoriesBean> apply
                                (ColumnDailyDetailsBean columnDailyDetailsBean) throws Exception {
                            return columnDailyDetailsBean.getStories();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<List<ColumnDailyDetailsBean.StoriesBean>>() {
                        @Override
                        public void accept(List<ColumnDailyDetailsBean.StoriesBean> storiesBeans) throws Exception {
                            saveDataToDB(storiesBeans);
                        }
                    })
                    .subscribeWith(new CommonSubscriber<List<ColumnDailyDetailsBean.StoriesBean>>(getView()) {
                        @Override
                        public void onNext(List<ColumnDailyDetailsBean.StoriesBean> storiesBeans) {
                            if (storiesBeans != null) {
                                getView().loadSuccess(storiesBeans);
                                mData = storiesBeans;
                            } else {
                                getView().showErrorMsg("专栏列表详情加载失败....");
                                getView().showEmptyView();
                            }
                            mStep = Constants.STATE_NORMAL;
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            mStep = Constants.STATE_ERROR;
                        }
                    })
            );
        } else {
            reqDataFromDB();
        }
    }

    @Override
    public void refreshData(String sectionId) {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet(sectionId);
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
                                .ZHIHU_SECTION_DETAILS_LIST_KEY + getView().getSectionId());
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
                                mData = JSON.parseArray(cacheBean.getJson(), ColumnDailyDetailsBean.StoriesBean.class);
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
    public void saveDataToDB(List<ColumnDailyDetailsBean.StoriesBean> dataBeans) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_SECTION_DETAILS_LIST_KEY + getView().getSectionId(),
                JSON.toJSONString(dataBeans));
    }

    @Override
    public List<ColumnDailyDetailsBean.StoriesBean> getData() {
        return mData;
    }

    @Override
    public int getDailyId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getId();
        }
        return 0;
    }
}
