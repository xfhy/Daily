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
import com.xfhy.daily.model.bean.ColumnDailyBean;
import com.xfhy.daily.presenter.ZHSectionContract;

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
 * @author feiyang
 *         time create at 2017/11/22 14:24
 *         description
 */
public class ZHSectionPresenter extends RxPresenter<ZHSectionContract.View> implements
        ZHSectionContract.Presenter {

    /**
     * 知乎数据管理类 model
     */
    private ZHDataManager mZHDataManager;
    private List<ColumnDailyBean.DataBean> mData;
    /**
     * 当前view所处的状态
     */
    private int mStep;

    public ZHSectionPresenter() {
        mZHDataManager = ZHDataManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        getView().onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected()) {

            addSubscribe(
                    mZHDataManager.getColumnDailyList()
                            .map(new Function<ColumnDailyBean, List<ColumnDailyBean.DataBean>>() {
                                @Override
                                public List<ColumnDailyBean.DataBean> apply(ColumnDailyBean columnDailyBean)
                                        throws Exception {
                                    return columnDailyBean.getData();
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(new Consumer<List<ColumnDailyBean.DataBean>>() {
                                @Override
                                public void accept(List<ColumnDailyBean.DataBean> dataBeans) throws Exception {
                                    saveDataToDB(dataBeans);
                                }
                            })
                            .subscribeWith(new CommonSubscriber<List<ColumnDailyBean.DataBean>>(getView()) {
                                @Override
                                public void onNext(List<ColumnDailyBean.DataBean> dataBeans) {
                                    if (dataBeans != null) {
                                        getView().loadSuccess(dataBeans);
                                        mData = dataBeans;
                                    } else {
                                        getView().showErrorMsg("专栏列表加载失败....");
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
                        List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(DBConstants.ZHIHU_SECTION_LIST_KEY);
                        if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                            CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                            e.onNext(cacheBean);
                        } else {
                            e.onError(new Exception(StringUtils.getStringByResId(BaseApplication.getApplication(),
                                    R.string.devices_offline)));
                        }
                    }
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new CommonSubscriber<CacheBean>(getView(), "网络不给力~╭(╯^╰)╮") {
                            @Override
                            public void onNext(CacheBean cacheBean) {
                                //解析json数据
                                mData = JSON.parseArray(cacheBean.getJson(), ColumnDailyBean.DataBean.class);
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
    public void saveDataToDB(List<ColumnDailyBean.DataBean> dataBeans) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_SECTION_LIST_KEY,
                JSON.toJSONString(dataBeans));
    }

    @Override
    public List<ColumnDailyBean.DataBean> getData() {
        return mData;
    }

    @Override
    public int getSectionId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getId();
        }
        return 0;
    }

    @Override
    public String getSectionTitle(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getName();
        }
        return "";
    }
}
