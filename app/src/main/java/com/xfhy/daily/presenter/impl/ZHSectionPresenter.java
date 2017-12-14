package com.xfhy.daily.presenter.impl;

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
public class ZHSectionPresenter extends AbstractPresenter<ZHSectionContract.View> implements
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
            mZHDataManager.getColumnDailyList()
                    .compose(getView().bindLifecycle())
                    .map(new Function<ColumnDailyBean, List<ColumnDailyBean.DataBean>>() {
                        @Override
                        public List<ColumnDailyBean.DataBean> apply(ColumnDailyBean
                                                                            columnDailyBean)
                                throws Exception {
                            return columnDailyBean.getData();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ColumnDailyBean.DataBean>>() {
                        @Override
                        public void accept(List<ColumnDailyBean.DataBean> dataBeans)
                                throws Exception {
                            if (dataBeans != null) {
                                getView().loadSuccess(dataBeans);
                                mData = dataBeans;
                                saveDataToDB(dataBeans);
                            } else {
                                getView().showErrorMsg("专栏列表加载失败....");
                                getView().showEmptyView();
                            }
                            mStep = Constants.STATE_NORMAL;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("专栏列表加载失败 错误:" + throwable.getLocalizedMessage());
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
                        .ZHIHU_SECTION_LIST_KEY);
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
                .compose(getView().bindLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheBean>() {
                    @Override
                    public void accept(CacheBean cacheBean) throws
                            Exception {
                        //解析json数据
                        mData = JSON.parseArray(cacheBean.getJson(), ColumnDailyBean.DataBean
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

                        if (StringUtils.getStringByResId(BaseApplication.getApplication(), R.string
                                .devices_offline)
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
